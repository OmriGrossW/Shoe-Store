package bgu.spl.app;

import bgu.spl.app.services.SellingService;

/**
 * The Enum BuyResult.
 * used in {@link SellingService} (implicitly in {@link Store}) to decide what action to do after a client asks to purchase a shoe.
 * checks according to the request if the shoe is available, if possible sells a shoe under discount.
 */
public enum BuyResult {
	/** shoe not in stock at all. 
	 * the expected response should be a restockRequest for the requested shoeType*/
	NOT_IN_STOCK,
	/** shoe not on discount, and the client wanted to buy it only if it is. */
	NOT_ON_DISCOUNT,
	/** sell the shoe on a regular price. */
	REGULAR_PRICE,
	/** sell the shoe on a discounted price. */
	DISCOUNTED_PRICE
 }
