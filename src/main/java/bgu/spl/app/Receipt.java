package bgu.spl.app;

/**
 * The Class Receipt.
 */
public class Receipt {
	
	private String seller; //the name of the service which issued the receipt
	private String customer; //the name of the service this receipt issued to
	private String shoeType; //the shoe type bought
	private boolean discount; //indicating if the shoe was sold at a discounted price
	private int issuedTick; //tick in which this receipt was issued
	private int requestTick; //tick in which the customer requested to buy the shoe
	private int amountSold; //the amount of shoes sold
	
	/**
	 * Receipt Constructor.
	 *
	 * @param seller the seller
	 * @param customer the customer
	 * @param shoeType the shoe type
	 * @param discount indicates if the shoe was sold under discount
	 * @param issuedTick the issued tick
	 * @param requestTick the request tick
	 * @param amountSold the amount sold
	 */
	public Receipt(String seller, String customer, String shoeType, boolean discount, int issuedTick, int requestTick, int amountSold){
		this.seller=seller;
		this.customer=customer;
		this.shoeType=shoeType;
		this.discount=discount;
		this.issuedTick=issuedTick;
		this.requestTick=requestTick;
		this.amountSold=amountSold;
	}
	
	/**
	 * Prints the receipt's output.
	 *
	 * @return the string
	 */
	public String print(){
		String isDiscount = " without a discount";
		if (discount){
			isDiscount = " with a discount";
		}
		return ("Reciept:\n"
				+ "\t" + customer + " requested to buy " + amountSold + " " + shoeType + " at tick: " + requestTick + ".\n" 
				+ "\t" + " sold by " + seller + isDiscount + " at tick: " + issuedTick + ".");
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
		return amountSold; 
	}
	
	/**
	 * Gets the discount.
	 *
	 * @return the discount
	 */
	public boolean getDiscount(){
		return discount; 
	}
	
	/**
	 * Gets the seller.
	 *
	 * @return the seller
	 */
	public String getSeller(){
		return seller;
	}
}
