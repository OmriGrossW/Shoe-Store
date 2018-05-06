package bgu.spl.app;

/**
 * The Class ShoeStorageInfo.
 */
public class ShoeStorageInfo {

 private String shoeType; //the type of the shoe
 private int amount; //the number of shoes of shoeType currently on the storage
 private int discountedAmount; //amount of shoes in this storage that can be sale in a discounted price
 
 /**
  * ShoeStorageInfo Constructor.
  *
  * @param shoeType the shoe type
  * @param amount the initial amount
  */
 public ShoeStorageInfo(String shoeType, int amount){
	 this.shoeType = shoeType;
	 this.amount = amount;
	 discountedAmount = 0;
 }
 
 /**
  * Shoe sold.
  * updates the store's inventory
  * @param discount indicates if the shoe was sold under discount
  */
 public void shoeSold(boolean discount){
	 if (discount){
		 discountedAmount = discountedAmount-1;
	 }
	 amount = amount-1;
 }
 
 /**
  * Adds the shoes.
  *
  * @param amountToAdd the amount to add
  */
 public void addShoes(int amountToAdd){
	 amount = amount + amountToAdd;
 }
 
 /**
  * Adds the to discounted shoes, if there are enough available.
  *
  * @param amountToAdd the amount to add
  */
 public void addToDiscount(int amountToAdd){
	 if (discountedAmount+amountToAdd>amount){
		 discountedAmount = amount;
	 }
	 else {
		 discountedAmount = discountedAmount + amountToAdd;
	 }
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
  * Gets the amount of shoes in stock.
  *
  * @return the amount
  */
 public int getamount(){
	 return  amount;
 }
 
 /**
  * Gets the discounted amount.
  *
  * @return the discounted amount
  */
 public int getDiscountedAmount(){
	 return  discountedAmount;
 }
 

 public String toString(){
	 return ("Shoe in stock: " + amount + " " + shoeType + " in stock, " + discountedAmount + " of them under discount.");
 }
}
