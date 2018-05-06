package bgu.spl.app.messages;

import bgu.spl.app.*;
import bgu.spl.mics.Request;


/**
 * PurchaseOrderRequest Class.
 * sent to a seller by a client when he wants to buy a shoe from the {@link Store}.
 */
public class PurchaseOrderRequest implements Request<Receipt> {
	
	private String senderName;
	private String shoeType;
	private boolean onlyDiscount;
	private int requestTick;
	
	/**
	 * PurchaseOrderRequest Constructor.
	 *
	 * @param senderName the request sender name
	 * @param shoeType the shoe type
	 * @param onlyDiscount the only discount
	 * @param requestTick the request tick
	 */
	public	PurchaseOrderRequest(String senderName, String shoeType, boolean onlyDiscount, int requestTick){
		this.senderName=senderName;
		this.shoeType=shoeType;
		this.onlyDiscount=onlyDiscount;
		this.requestTick=requestTick;
	}
	
	/**
	 * Gets the sender name.
	 *
	 * @return the sender name
	 */
	public String getSenderName() {
        return senderName;
    }
	
	/**
	 * Gets the shoe type.
	 *
	 * @return the shoe type
	 */
	public String getShoeType(){
		return shoeType;
	}
	
	/**
	 * Gets the only discount.
	 *
	 * @return the only discount
	 */
	public boolean getOnlyDiscount(){
		return onlyDiscount;
	}
	
	/**
	 * Gets the request tick.
	 *
	 * @return the request tick
	 */
	public int getRequestTick(){
		return requestTick;
	}
}
