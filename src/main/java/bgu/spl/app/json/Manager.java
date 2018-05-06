package bgu.spl.app.json;

import java.util.LinkedList;
import java.util.List;
import bgu.spl.app.DiscountSchedule;

/**
 * The Class Manager.
 * Json Class
 */
public class Manager {
	
	private DiscountSchedule[] discountSchedule;
	
	/**
	 * Manager Copy Constructor.
	 *
	 * @param manager the manager
	 */
	public Manager(Manager manager) {
		discountSchedule= new DiscountSchedule[manager.discountSchedule.length];
		for (int i=0 ; i<manager.discountSchedule.length ; i++){
			discountSchedule[i]=manager.discountSchedule[i];
		}
	}
	
	/**
	 * Gets the discount schedule list.
	 *
	 * @return the discount schedule list
	 */
	public List<DiscountSchedule> getDiscountScheduleList(){
		List<DiscountSchedule> dsList = new LinkedList<DiscountSchedule>();
		for (int i=0 ; i<discountSchedule.length ; i++){
			dsList.add(discountSchedule[i]);
		}
		return dsList;
	}
	
	

}
