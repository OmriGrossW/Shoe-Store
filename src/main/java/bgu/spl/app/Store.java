package bgu.spl.app;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * The Class Store.
 * a singleton Class that stores the stock and receipts of the store. 
 */
public class Store {
	
	private static Store str = new Store();
	private ConcurrentHashMap<String, ShoeStorageInfo> stockMap = new ConcurrentHashMap<>();
	private ConcurrentLinkedQueue<String> shoeList = new ConcurrentLinkedQueue<String>();
	private ConcurrentLinkedQueue<Receipt> receiptList = new ConcurrentLinkedQueue<Receipt>();
	private Object buyResultLocker = new Object();
	private CountDownLatch printRecieptSignal;

	
	/**
	 * Empty Constructor
	 */
	private Store(){} //empty constructor
	
	/**
	 * Gets the singleton instance of the store.
	 *
	 * @return the store singleton instance.
	 */
	public static Store getStore(){
		return str;
	}
	
	
	
	/**
	 * Loads the store with the initial storage.
	 *
	 * @param storage the storage array contains {@link ShoeStorageInfo}s
	 */
	public void load(ShoeStorageInfo[] storage){
		for (int i=0 ; i<storage.length ; i++ ){
			stockMap.put(storage[i].getShoeType(),storage[i]);
			shoeList.add(storage[i].getShoeType());
		}
		
	}
	
	/**
	* Take is used to determine which action to do when a client asks to buy a shoe from the store.
	*NOT_IN_STOCK:
	*  shoe not in stock at all,
	*  the expected response should be a restockRequest for the requested shoeType.
	*NOT_ON_DISCOUNT:
    *  shoe not on discount, and the client wanted to buy it only if it is,
    *  no response expected.
    *REGULAR_PRICE:
	*  sell the shoe on a regular price,
	*  the expected response is to sell the shoe to the client.
	*DISCOUNTED_PRICE:
	*  sell the shoe on a discounted price,
	*  the expected response is to sell the shoe to the client with a discount.
	 *
	 * @param shoeType the shoe type
	 * @param onlyDiscount the only discount indicator
	 * @return the buy result, the ENUM.
	 */
	public  BuyResult take(String shoeType , boolean onlyDiscount){
		synchronized (buyResultLocker){
			if (stockMap.get(shoeType)!=null){
				if(stockMap.get(shoeType).getamount()>0){	
					if (isShoeInDiscount(shoeType)){
						stockMap.get(shoeType).shoeSold(true);
						return BuyResult.DISCOUNTED_PRICE;
					}
					else{
						if (onlyDiscount){
							return BuyResult.NOT_ON_DISCOUNT;
						}
						stockMap.get(shoeType).shoeSold(false);
						return BuyResult.REGULAR_PRICE;
					}
				}
			}
			if (onlyDiscount){
				return BuyResult.NOT_ON_DISCOUNT;
			}
				return BuyResult.NOT_IN_STOCK;
		}	
	}
	
	/**
	 * Adds the given amount to the given shoe type in the store's stock.
	 *
	 * @param shoeType the shoe type
	 * @param amount the amount
	 */
	public void add(String shoeType , int amount){
		if (!stockMap.containsKey(shoeType)){
			stockMap.put(shoeType, new ShoeStorageInfo(shoeType, 0));
			shoeList.add(shoeType);
		}
		stockMap.get(shoeType).addShoes(amount);
		
	}
	
	/**
	 * Adds the given amount of discounted shoes if possible to the given shoe type in the store's stock.
	 *
	 * @param shoeType the shoe type
	 * @param amount the amount
	 */
	public void addDiscount(String shoeType , int amount){
		if (stockMap.containsKey(shoeType)){
			stockMap.get(shoeType).addToDiscount(amount);
		}
	}
	
	/**
	 * Files a receipt in the store.
	 *
	 * @param receipt the receipt
	 */
	public void file(Receipt receipt){
			receiptList.add(receipt);
	}
	
	/**
	 * Prints the store's stock and receipts lists, and exit.
	 */
	public void printAndExit(){
		try {
			printRecieptSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int recieptAmount= receiptList.size();
		Iterator<String> itS = shoeList.iterator();
		Iterator<Receipt> itR = receiptList.iterator();			
		System.out.println("Store Stock:\n");
		while (itS.hasNext()){
			System.out.println(stockMap.get(itS.next()));
			
		}
		System.out.println("\n\nReciepts:\nTotal amount: " + recieptAmount + "\n");
		while (itR.hasNext()){
			System.out.println(itR.next().print());
		}
		System.exit(0);
	}
	
	/**
	 * Checks if is shoe in discount.
	 *
	 * @param shoeType the shoe type
	 * @return true, if is shoe in discount
	 */
	public boolean isShoeInDiscount(String shoeType){
		if(stockMap.get(shoeType).getDiscountedAmount()>0){
			return true;
		}
		return false;
	}

	/**
	 * Termination countdown decrease.
	 */
	public void terminationCountDownDecrease() {
		printRecieptSignal.countDown();
	}

	/**
	 * Sets the prints the reciept signal.
	 *
	 * @param numOfThredsToCountDown the new prints the reciept signal
	 */
	public void setPrintRecieptSignal(int numOfThredsToCountDown) {
		printRecieptSignal=new CountDownLatch(numOfThredsToCountDown);
	}
	
	
	
	

}
