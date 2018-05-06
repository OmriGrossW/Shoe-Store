package bgu.spl.app.json;

import java.util.Set;
import java.util.List;

import bgu.spl.app.PurchaseSchedule;

/**
 * The Class Client.
 * Json Class
 */
public class Client {
	
	private String name;
	private Set<String> wishList;
	private List<PurchaseSchedule> purchaseSchedule;
	
	/**
	 * Client Copy Constructor.
	 *
	 * @param client the client
	 */
	public Client(Client client){
		
		name=client.name;
		wishList=client.wishList;
		purchaseSchedule=client.purchaseSchedule;
		System.out.println(client.purchaseSchedule.get(0).getShoeType());
		
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets the purchase schedule list.
	 *
	 * @return the purchase schedule list
	 */
	public List<PurchaseSchedule> getPurchaseScheduleList(){
		return purchaseSchedule;
	}
	
	/**
	 * Gets the wish list.
	 *
	 * @return the wish list
	 */
	public Set<String> getWishList(){
		return wishList;
	}

}
