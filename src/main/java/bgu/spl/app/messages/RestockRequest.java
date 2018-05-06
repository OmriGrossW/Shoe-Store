package bgu.spl.app.messages;

import bgu.spl.mics.Request;

/**
 * RestockRequest Class.
 * sent from a seller to the store manager when someone is trying to buy a shoe that is not is stock.
 */
public class RestockRequest implements Request<Boolean> {
	 
	private String shoeType;
	
	/**
	 * RestockRequest Constructor.
	 *
	 * @param shoeType the requested shoe type 
	 */
	public RestockRequest(String shoeType){
		this.shoeType=shoeType;
	}
	
	/**
	 * Gets the shoe type.
	 *
	 * @return the shoe type
	 */
	public String getShoeType(){
		return shoeType;
	}
}
