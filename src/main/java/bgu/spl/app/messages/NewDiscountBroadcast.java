package bgu.spl.app.messages;

import bgu.spl.mics.Broadcast;


/**
 * NewDiscountBroadcast Class.
 * sent by the store manager to all of the clients.
 */
public class NewDiscountBroadcast implements Broadcast {
	
	private String shoeType;
	private int amount;
	private int requestTick;
	
	/**
	 * NewDiscountBroadcast Constructor.
	 *
	 * @param shoeType the shoe type
	 * @param amount the amount to be set under discount (if there are in stock)
	 * @param requestTick the request tick
	 */
	public	NewDiscountBroadcast(String shoeType, int amount, int requestTick){
		this.shoeType=shoeType;
		this.amount=amount;
		this.requestTick=requestTick;
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
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public int getAmount(){
		return amount;
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
