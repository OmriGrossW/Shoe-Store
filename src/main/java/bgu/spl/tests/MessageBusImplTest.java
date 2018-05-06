package bgu.spl.tests;

import static org.junit.Assert.*;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.After;
import org.junit.BeforeClass;
import bgu.spl.mics.*;
import bgu.spl.mics.impl.*;
import bgu.spl.app.services.*;
import bgu.spl.app.DiscountSchedule;
import bgu.spl.app.messages.*;


public class MessageBusImplTest {	
	private static MessageBusImpl bus;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bus = MessageBusImpl.getMessageBus();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSubscribeRequest() {
		MicroService m = new SellingService("achzaryan");
		bus.subscribeRequest(PurchaseOrderRequest.class, m);
		assertEquals(bus.getMessageMap().get(PurchaseOrderRequest.class).peek(),m);
	}
	@Test
	public void testSubscribeBroadcast() {
		MicroService m = new SellingService("dovale");
		bus.subscribeBroadcast(TickBroadcast.class, m);
		assertEquals(bus.getMessageMap().get(TickBroadcast.class).peek(),m);
	}
	@Test
	public void testSendBroadcast() {
		MicroService m = new SellingService("zamira");
		TickBroadcast t = new TickBroadcast(2);
		bus.subscribeBroadcast(TickBroadcast.class, m);
		bus.sendBroadcast(t);
		assertEquals(bus.getMicroServiceMap().get(m).peek(),t);
	}
	@Test
	public void testSendRequest() {
		MicroService m = new SellingService("ha-ah-ha");
		List<DiscountSchedule> list= new LinkedList<DiscountSchedule>();
		MicroService manager = new ManagementService(list);
		RestockRequest r = new RestockRequest("califa-shoe");
		bus.subscribeRequest(RestockRequest.class, manager);
		bus.sendRequest(r, m);
		assertEquals(bus.getMicroServiceMap().get(manager).poll(),r);
	}
	@Test
	public void testComplete() {
		Request req = new RestockRequest("kipi");
		Boolean result = true;
		MicroService m = new SellingService("tiryonL");
		bus.subscribeRequest(RestockRequest.class, m);
		bus.getRequesterMap().put(req, m);
		bus.complete(req, result);
		assertTrue(bus.getMicroServiceMap().get(m).peek().getClass()==RequestCompleted.class);
		// Checks that the stock exists
	}
	@Test
	public void testRegister() {
		MicroService m = new SellingService("achzaryan");
		bus.register(m);
		assertTrue(bus.getMicroServiceMap().containsKey(m));
	}
	@Test
	public void testUnregister() {
		MicroService m = new SellingService("dovale");
		bus.register(m);
		bus.unregister(m);
		assertFalse(bus.getMicroServiceMap().contains(m));
	}
	@Test
	public void testAwaitMessage() {
		MicroService m = new SellingService("zamira");
		MicroService manager = new ManagementService(new LinkedList<DiscountSchedule>());
		RestockRequest r = new RestockRequest("califa-shoe");
		bus.subscribeRequest(RestockRequest.class, manager);
		bus.sendRequest(r, m);
		try {
			assertEquals(bus.awaitMessage(manager),r);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
