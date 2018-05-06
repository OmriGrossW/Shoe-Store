package bgu.spl.app.services;

import bgu.spl.app.BuyResult;
import bgu.spl.app.Receipt;
import bgu.spl.app.ShoeStoreRunner;
import bgu.spl.app.Store;
import bgu.spl.app.messages.PurchaseOrderRequest;
import bgu.spl.app.messages.RestockRequest;
import bgu.spl.app.messages.TerminationBroadcast;
import bgu.spl.app.messages.TickBroadcast;
import bgu.spl.mics.MicroService;

/**
 * The Class SellingService, a MicroService who represents a seller in the {@link Store}.
 */
public class SellingService extends MicroService  {

	private int time;
	private BuyResult buyResult;
	
	/**
	 * SellingService Constructor
	 * a seller in the {@link Store}
	 * @param name the name of the seller
	 */
	public SellingService(String name) {
		super(name);
	}

	/**
	 * subscribes to requests and broadcasts, and for every subscription acts accordingly.  
	 */
	@Override
	protected void initialize() {
		ShoeStoreRunner.LOGGER.info("SellingService " + this.getName() + " has started.\n");
		subscribeBroadcast(TerminationBroadcast.class, terminate -> {
			this.terminate();
			ShoeStoreRunner.LOGGER.info(this.getName() + " has terminated.\n");
			Store.getStore().terminationCountDownDecrease();
			});
		subscribeBroadcast(TickBroadcast.class, currentTick -> {
			time=currentTick.getCurrentTick();
		});
		subscribeRequest(PurchaseOrderRequest.class, order -> {//receives a PurchaseOrderRequest and handles it
			Receipt r;
			buyResult = Store.getStore().take(order.getShoeType(), order.getOnlyDiscount());//checking in the store's inventory if the shoe is available for sale
			switch(buyResult){
				case DISCOUNTED_PRICE:
					r=new Receipt(this.getName(), order.getSenderName(), order.getShoeType(), true, this.time, order.getRequestTick(), 1);
					Store.getStore().file(r);
					complete(order,r);
					break;
				case REGULAR_PRICE:
					r=new Receipt(this.getName(), order.getSenderName(), order.getShoeType(), false, this.time, order.getRequestTick(), 1);
					Store.getStore().file(r);
					complete(order,r);
					break;
				case NOT_IN_STOCK:
					sendRequest(new RestockRequest(order.getShoeType()), answer -> {
						Receipt re;
						if (!answer){
							complete(order,null);
						}
						else{//a RestockRequest has finished successfully
							re=new Receipt(this.getName(), order.getSenderName(), order.getShoeType(), Store.getStore().isShoeInDiscount(order.getShoeType()), this.time, order.getRequestTick(), 1);
							Store.getStore().file(re);
							complete(order,re);
						}
					});
					break;
				case NOT_ON_DISCOUNT:
					complete(order,null);
					break;
			}
		});
		ShoeStoreRunner.countDown();
		ShoeStoreRunner.LOGGER.info("SellingService " + this.getName() + " is initialized.\n");
	}

}
