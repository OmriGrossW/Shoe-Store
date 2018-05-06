package bgu.spl.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bgu.spl.app.BuyResult;
import bgu.spl.app.ShoeStorageInfo;
import bgu.spl.app.Store;

public class StoreTest {
	
	private static Store store;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		store = Store.getStore();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		ShoeStorageInfo[] stock = {new ShoeStorageInfo("red-califa", 7),
				new ShoeStorageInfo("black-kipi", 7),
				new ShoeStorageInfo("green-sneakers", 3),
				new ShoeStorageInfo("ruby-boots", 9)};
		store.load(stock);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetStore() {
		assertTrue(store.getStore()!=null);
	}

	@Test
	public void testLoad() {
		ShoeStorageInfo[] stock = {new ShoeStorageInfo("red-sandals", 7),
				new ShoeStorageInfo("green-boots", 2),
				new ShoeStorageInfo("black-sneakers", 1),
				new ShoeStorageInfo("pink-flip-flops", 0)};
		store.load(stock);
		assertEquals(store.take("black-sneakers",false), BuyResult.REGULAR_PRICE);
	}

	@Test
	public void testTake() {
		assertEquals(store.take("nike-air", false), BuyResult.NOT_IN_STOCK);
		assertEquals(store.take("black-kipi", false), BuyResult.REGULAR_PRICE);
	}


}
