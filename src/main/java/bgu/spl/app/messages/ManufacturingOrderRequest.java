package bgu.spl.app.messages;


import bgu.spl.app.*;
import bgu.spl.mics.Request;

/**
 * ManufacturingOrderRequest Class.
 * sent by the store manager to an available factory.
 * 
 */
public class ManufacturingOrderRequest implements Request<Receipt> {
	
	private String shoeType;
	private int time;
	private int amount;
	
	/**
	 * ManufacturingOrderRequest Constructor.
	 *
	 * @param shoeType the shoe type
	 * @param amount the amount to be produced
	 * @param time the time the request was sent
	 */
	public	ManufacturingOrderRequest(String shoeType, int amount, int time){
		this.shoeType=shoeType;
		this.time=time;
		this.amount=amount;
		
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
	 * Gets the time.
	 *
	 * @return the time
	 */
	public int getTime(){
		return time;
	}
	
	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public int getAmount(){
		return amount;
	}
	
}
