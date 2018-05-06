package bgu.spl.app.services;

import bgu.spl.app.ManufacturingInstance;
import bgu.spl.app.Receipt;
import bgu.spl.app.ShoeStoreRunner;
import bgu.spl.app.Store;
import bgu.spl.app.messages.ManufacturingOrderRequest;
import bgu.spl.app.messages.TerminationBroadcast;
import bgu.spl.app.messages.TickBroadcast;
import bgu.spl.mics.MicroService;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;;
/**
 * ShoeFactoryService Class.
 *
 */
public class ShoeFactoryService extends MicroService {
	
	private int time;
	private int currentManufacturingFinishTick;
	private LinkedBlockingQueue<ManufacturingInstance> manufactureQueue=new LinkedBlockingQueue<ManufacturingInstance>();
	private ConcurrentHashMap<String, ManufacturingInstance> lastOrderInManufacturingQueueByShoeType = new ConcurrentHashMap<>();
	
	/**
	 * ShoeFactoryService Constructor
	 * @param name the name of the Factory
	 */
	public ShoeFactoryService(String name) {
		super(name);
		}
	
	/**
	 * subscribes to requests and broadcasts, and for every subscription acts accordingly.   
	 */
	@Override
	protected void initialize() {
		ShoeStoreRunner.LOGGER.info("ShoeFactoryService " + this.getName() + " has started.\n");
		subscribeBroadcast(TerminationBroadcast.class, terminate -> {
			Iterator<ManufacturingInstance> it = manufactureQueue.iterator();
				while (it.hasNext()){//when needs to terminate announces that all of the ManufacturingInstances in queue cannot be completed
					String shoe = it.next().getShoeType();
					ShoeStoreRunner.LOGGER.warning("No time left to manufacture " + shoe + ".\n");
				}
			manufactureQueue.clear();
			this.terminate();
			ShoeStoreRunner.LOGGER.info(this.getName() + " has terminated.\n");
			Store.getStore().terminationCountDownDecrease();
			});
		
		subscribeBroadcast(TickBroadcast.class, currentTick -> {
			time=currentTick.getCurrentTick();
			if (!manufactureQueue.isEmpty() && manufactureQueue.peek().getRequestTime()!=time){ //checks if there are ManufacturingInstances in queue to handle
				if (manufactureQueue.peek().getToBeProduced()==0){ //checks if the ManufacturingInstance in the top of the queue is done
					ManufacturingInstance ins = manufactureQueue.poll();
					ManufacturingOrderRequest r = ins.getRequest();
					Store.getStore().add(r.getShoeType(), ins.getAvailableShoes());
					ShoeStoreRunner.LOGGER.info(r.getAmount() + " " + r.getShoeType()+ " delivered by " + this.getName() + " and were added to the store's stock.\n");
					complete(r, new Receipt(this.getName(), "Store", r.getShoeType(), false, time, r.getTime(), r.getAmount()));
					if(!ins.getRequestersList().isEmpty()){
						Iterator<ManufacturingOrderRequest> it = ins.getRequestersList().iterator();
						while(it.hasNext()){
							complete(it.next(), new Receipt(this.getName(), "Store", r.getShoeType(), true, time, r.getTime(), r.getAmount()));
						}
					}
					if (!manufactureQueue.isEmpty()){//after completing the request, starts producing the next one.
						manufactureQueue.peek().decreaseToBeProduced();
					}
				}
				else {
					manufactureQueue.peek().decreaseToBeProduced();
				}
			}
		});
		
		subscribeRequest(ManufacturingOrderRequest.class, order -> {
			if (lastOrderInManufacturingQueueByShoeType.containsKey(order.getShoeType())){ //Checking if there is a pending order of the same shoe type with available shoes 
				lastOrderInManufacturingQueueByShoeType.get(order.getShoeType()).addToRequestersList(order);
				lastOrderInManufacturingQueueByShoeType.get(order.getShoeType()).decreaseAvailableShoes();
				if (lastOrderInManufacturingQueueByShoeType.get(order.getShoeType()).getAvailableShoes()==0){
					lastOrderInManufacturingQueueByShoeType.remove(order.getShoeType());
				}
				ShoeStoreRunner.LOGGER.info("1 " + order.getShoeType() + " was reserved for a client.\n");
			}
			else{ //Adding a new ManufactureInstance into the manufacture queue
				if (manufactureQueue.isEmpty()){
					currentManufacturingFinishTick = time + 1;
				}
				currentManufacturingFinishTick = currentManufacturingFinishTick+order.getAmount();
				ManufacturingInstance ins = new ManufacturingInstance(order, currentManufacturingFinishTick);
				manufactureQueue.add(ins);
				if (manufactureQueue.peek().getAmount()>1){
					lastOrderInManufacturingQueueByShoeType.put(order.getShoeType(), ins);
				}
				ShoeStoreRunner.LOGGER.info(this.getName() + " recieved a request for manufacturing " + order.getAmount() + " " + order.getShoeType()+ ".\n");
			}
		});
		
		ShoeStoreRunner.countDown();
		ShoeStoreRunner.LOGGER.info("ShoeFactoryService " + this.getName() + " is initialized.\n");
	}

}
