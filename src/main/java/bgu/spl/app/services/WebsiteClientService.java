package bgu.spl.app.services;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import bgu.spl.app.PurchaseSchedule;
import bgu.spl.app.ShoeStoreRunner;
import bgu.spl.app.Store;
import bgu.spl.app.messages.NewDiscountBroadcast;
import bgu.spl.app.messages.PurchaseOrderRequest;
import bgu.spl.app.messages.TerminationBroadcast;
import bgu.spl.app.messages.TickBroadcast;
import bgu.spl.mics.MicroService;
/**
 *	WebsiteClientService Class is a MicroService representing a client that wishes to buy shoes from the store in different ways. 
 *
 */
public class WebsiteClientService extends MicroService {
	
	private int time;
	private ConcurrentHashMap<Integer, LinkedBlockingQueue<PurchaseSchedule>> purchaseScheduleMap = new ConcurrentHashMap<>();
	private Set<String> wishList;
	
	/**
	 * WebsiteClientService Constructor. 
	 * @param name the client's name
	 * @param purchaseSchedule a list of scheduled purchases that the client will make.
	 * @param wishList a list of shoes that the client will buy only if available under discount.
	 */
	public WebsiteClientService(String name , List<PurchaseSchedule> purchaseSchedule , Set<String> wishList) {
		super(name);
		Iterator<PurchaseSchedule> it = purchaseSchedule.iterator();
		while (it.hasNext()){
			PurchaseSchedule ps = it.next();
			if (!purchaseScheduleMap.containsKey(ps.getTick())){
				purchaseScheduleMap.put(ps.getTick(),new LinkedBlockingQueue<PurchaseSchedule>()); 	
			}
			purchaseScheduleMap.get(ps.getTick()).add(ps);
		}
		this.wishList=wishList;
	}
	
	/**
	 * checks if the client has finished buying shoes according to his wishlist and purchaseSchedule list
	 * @return true if both lists are empty, false otherwise.
	 */
	private boolean finishedSpendingMoney(){
		return (purchaseScheduleMap.isEmpty() && wishList.isEmpty());
	}
	
	/**
	 * subscribes to requests and broadcasts, and for every subscription acts accordingly.   
	 */
	@Override
	protected void initialize() {
		ShoeStoreRunner.LOGGER.info("WebClientService " + this.getName() + " has started.\n");
		subscribeBroadcast(TerminationBroadcast.class, terminate -> {
			this.terminate();
			ShoeStoreRunner.LOGGER.info(this.getName() + " has terminated.\n");
			Store.getStore().terminationCountDownDecrease();
			});
		subscribeBroadcast(TickBroadcast.class, currentTick -> { 
			time=currentTick.getCurrentTick();
			if (purchaseScheduleMap.containsKey(time)){ //every passing tick checks for scheduled purchase requests and sends them
				while(!purchaseScheduleMap.get(time).isEmpty()){
					PurchaseOrderRequest poRequest = new PurchaseOrderRequest(this.getName(), purchaseScheduleMap.get(time).poll().getShoeType(), false, time);
					sendRequest(poRequest, finished -> {
					if (finished==null){
						ShoeStoreRunner.LOGGER.info("Purchase request from " + this.getName() + " asking for " + poRequest.getShoeType() + " is not availiable at this time.\n");
					}
					else {
						ShoeStoreRunner.LOGGER.info("Shoe Sold: " + poRequest.getShoeType() + " sold to " + this.getName() + ".\n");
					}
					if (finishedSpendingMoney()){
						terminate();
						ShoeStoreRunner.LOGGER.info(this.getName() + " has terminated.\n");
						Store.getStore().terminationCountDownDecrease();
					}
					
				});
				}
				purchaseScheduleMap.remove(time); //removes from purchaseScheduleMap the list of scheduled purchase requests of the current tick (done after sending all of them).
			}
		});
		
		subscribeBroadcast(NewDiscountBroadcast.class, discount -> {
			if(wishList.contains(discount.getShoeType())){ //checks if the shoe that is now under discount is in the client's wishList, and if so sends a PurchaseOrderRequest
				sendRequest(new PurchaseOrderRequest(this.getName(), discount.getShoeType(), true, time), finished -> {
					if (finished!=null){
						wishList.remove(finished.getShoeType());
						ShoeStoreRunner.LOGGER.info("Shoe Sold from WishList: " + finished.getShoeType() + " sold to " + this.getName() + " at discounted price and successfully removed from his WishList.\n");
						if (finishedSpendingMoney()){
							terminate();
							ShoeStoreRunner.LOGGER.info(this.getName() + " has terminated.\n");
							Store.getStore().terminationCountDownDecrease();
						}
					}
				});
			}
		});
		ShoeStoreRunner.countDown();
		ShoeStoreRunner.LOGGER.info("WebClientService " + this.getName() + " is initialized.\n");
	}
}
