package bgu.spl.app;

/**
 * The Class DiscountSchedule.
 * a planned discount on a specific shoe in the {@link Store}.
 */
public class DiscountSchedule {
	
	private String shoeType; //the type of shoe to purchase
	private int amount; //the amount of items to put on discount
	private int tick; //the tick number to send the PurchaseOrderRequest at
	
	/**
	 * DiscountSchedule Constructor.
	 *
	 * @param shoeType the shoe type
	 * @param amount the amount
	 * @param tick the tick
	 */
	public DiscountSchedule(String shoeType, int amount, int tick){
		this.shoeType = shoeType;
		this.amount = amount;
		this.tick = tick;
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
	 * Gets the tick.
	 *
	 * @return the tick
	 */
	public int getTick(){
		return tick;
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
