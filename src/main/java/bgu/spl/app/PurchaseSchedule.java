package bgu.spl.app;


/**
 * The Class PurchaseSchedule.
 * a planned shoe purchase.
 */

public class PurchaseSchedule {
	
	private String shoeType; //the type of shoe to purchase
	private int tick; //the tick number to send the PurchaseOrderRequest at
	
	/**
	 * PurchaseSchedule Constructor.
	 *
	 * @param shoeType the shoe type
	 * @param tick the tick
	 */
	public PurchaseSchedule(String shoeType, int tick){
		this.shoeType = shoeType;
		this.tick = tick;
	}
	
	/**
	 * PurchaseSchedule Copy Constructor.
	 *
	 * @param ps 
	 */
	public PurchaseSchedule(PurchaseSchedule ps){
		new PurchaseSchedule(ps.shoeType, ps.tick);
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
	 * Gets the request tick.
	 *
	 * @return the tick
	 */
	public int getTick(){
		return tick;
	}
}
