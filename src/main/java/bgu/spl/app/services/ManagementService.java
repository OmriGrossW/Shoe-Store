package bgu.spl.app.services;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import bgu.spl.app.Store;
import bgu.spl.app.messages.ManufacturingOrderRequest;
import bgu.spl.app.messages.NewDiscountBroadcast;
import bgu.spl.app.messages.RestockRequest;
import bgu.spl.app.messages.TerminationBroadcast;
import bgu.spl.app.messages.TickBroadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.app.DiscountSchedule;
import bgu.spl.app.ShoeStoreRunner;
import bgu.spl.tests.*;
/**
 * ManagementService Class is a MicroService representing the {@link Store}'s manager.
 *
 */
public class ManagementService extends MicroService {

	private int time;
	private ConcurrentHashMap<Integer, LinkedBlockingQueue<DiscountSchedule>> discountScheduleMap = new ConcurrentHashMap<>();
	/**
	 * ManagementService Constructor
	 * @param discountList a list of scheduled discounts that the manager will send on their time.
	 */
	public ManagementService(List<DiscountSchedule> discountList) {
		super("Manager");
		Iterator<DiscountSchedule> it = discountList.iterator();
		while (it.hasNext()){
			DiscountSchedule ds = it.next();
			if (!discountScheduleMap.containsKey(ds.getTick())){
				discountScheduleMap.put(new Integer(ds.getTick()), new LinkedBlockingQueue<>());
			}
			try {
				discountScheduleMap.get(ds.getTick()).put(ds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * subscribes to requests and broadcasts, and for every subscription acts accordingly.  
	 */
	@Override
	protected void initialize() {
		ShoeStoreRunner.LOGGER.info("Store Manager has started.\n");
		subscribeBroadcast(TerminationBroadcast.class, terminate -> {
			this.terminate();
			ShoeStoreRunner.LOGGER.info(this.getName() + " has terminated.\n");
			Store.getStore().terminationCountDownDecrease();
			});
		subscribeBroadcast(TickBroadcast.class, currentTick -> {
			time=currentTick.getCurrentTick();
			if (discountScheduleMap.get(time)!=null){//checks if there are DiscountSchedule instances planned for the current tick, and if so executes them.
				Iterator<DiscountSchedule> it = discountScheduleMap.get(time).iterator();
				while(it.hasNext()){
					DiscountSchedule discount = it.next();
					sendBroadcast(new NewDiscountBroadcast(discount.getShoeType(), discount.getAmount(), time));
					Store.getStore().addDiscount(discount.getShoeType(), discount.getAmount());
					ShoeStoreRunner.LOGGER.info("A New Discount Broadcast of " + discount.getAmount() + " " + discount.getShoeType() +" was announced.\n");
				}
			}
		});
		
		subscribeRequest(RestockRequest.class, req ->{
			ShoeStoreRunner.LOGGER.info("Restock request recieved for brand: " + req.getShoeType() + "\n");	
			if(!sendRequest(new ManufacturingOrderRequest(req.getShoeType(), (time%5)+1, time), receipt->{//receives a restockRequest and sends a ManufacturingOrderRequest to an available shoeFactory.
				if(!receipt.getDiscount()){	// a flag, if true means no need to add receipt - it is a shoe that was reserved on other requesters manufacturing request
						Store.getStore().file(receipt);
						ShoeStoreRunner.LOGGER.info("Delivery of " + receipt.getAmount() + " " + receipt.getShoeType() + " arrived from " + receipt.getSeller() + " at tick " + time +".\n");
					}
				updateDiscountForNewArrivals(req.getShoeType());
				complete(req,true);
			})){
				complete(req,false);
			}
		});
		ShoeStoreRunner.countDown();
		ShoeStoreRunner.LOGGER.info(this.getName() + " is initialized.\n");
	}
	
	/**
	 * Checks ,for shoes who have just arrived from the factory, if there is a discount planned for today on them - before selling them to the reserving costumers. 
	 * @param ShoeType the shoe type to check
	 */
	private void updateDiscountForNewArrivals(String ShoeType){
		if (discountScheduleMap.get(time)!=null){
			Iterator<DiscountSchedule> it = discountScheduleMap.get(time).iterator();
			while(it.hasNext()){
				DiscountSchedule discount = it.next();
				if(ShoeType.equals(discount.getShoeType())){
						Store.getStore().addDiscount(discount.getShoeType(), discount.getAmount());
						discountScheduleMap.get(time).remove(discount);
				}
			}
		}
	}
		
}
