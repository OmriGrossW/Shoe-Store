package bgu.spl.mics.impl;

import bgu.spl.app.ShoeStoreRunner;
import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Request;
import bgu.spl.mics.RequestCompleted;

import java.util.Iterator;
import java.util.concurrent.*;
/**
 * The message-bus is a shared object used for communication between
 * {@link MicroService}s.
 * It is implemented as a thread-safe singleton.
 * The message-bus is shared between all the {@link MicroService}s in the system.
 */
public class MessageBusImpl implements MessageBus {
	
	private static MessageBusImpl msgBus = new MessageBusImpl();
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message> > microServiceMap = new ConcurrentHashMap<>(); 
	private ConcurrentHashMap<Class <? extends Message>, RoundRobinQueue> messageMap  = new ConcurrentHashMap<>();
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Class <? extends Message>> > subscribedMessagesMap  = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Request<?>, MicroService> requestersMap  = new ConcurrentHashMap<>();
	private Object locker = new Object();
	
	private MessageBusImpl(){}
	
	/**
	 * Gets the singleton instance of the MessageBus.
	 * @return the singleton message bus instance.
	 */
	public static MessageBusImpl getMessageBus(){
		return msgBus;
	}
	
	/**
     * subscribes {@code m} to receive {@link Request}s of type {@code type}.
     * <p>
     * @param type the type to subscribe to
     * @param m    the subscribing micro-service
     */		
	@Override
	public void subscribeRequest(Class<? extends Request> type, MicroService m) {
		synchronized (locker) {
			allocateMapsFields(type,m);
			subscribedMessagesMap.get(m).add(type);
			messageMap.get(type).add(m);
		}
	}
	
	/**
     * subscribes {@code m} to receive {@link Broadcast}s of type {@code type}.
     * <p>
     * @param type the type to subscribe to
     * @param m    the subscribing micro-service
     */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (locker) {
			allocateMapsFields(type,m);
			subscribedMessagesMap.get(m).add(type);
			messageMap.get(type).add(m);
		}
	}
	
	/**
     * Notifying the MessageBus that the request {@code r} is completed and its
     * result was {@code result}.
     * When this method is called, the message-bus will implicitly add the
     * special {@link RequestCompleted} message to the queue
     * of the requesting micro-service, the RequestCompleted message will also
     * contain the result of the request ({@code result}).
     * <p>
     * @param <T>    the type of the result expected by the completed request
     * @param r      the completed request
     * @param result the result of the completed request
     */
	@Override
	public <T> void complete(Request<T> r, T result) {
		RequestCompleted<T> requestCompleted=new RequestCompleted<T>(r, result);
		synchronized(r){
			microServiceMap.get(requestersMap.get(r)).add(requestCompleted);
			requestersMap.remove(r);
		}
	}
	
	/**
     * add the {@link Broadcast} {@code b} to the message queues of all the
     * micro-services subscribed to {@code b.getClass()}.
     * <p>
     * @param b the message to add to the queues.
     */
	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (locker) {
			if (messageMap.get(b.getClass())!=null){
				LinkedBlockingQueue<MicroService> q = messageMap.get(b.getClass()).getRRQueue();
				Iterator<MicroService> it = q.iterator();
				while(it.hasNext()){
					microServiceMap.get(it.next()).add(b);
					
				}
			}
			else {
				System.out.println("not existing Broadcast Queue "+b.toString()+" in messageMap");
			}
		}
	}
	
	/**
     * add the {@link Request} {@code r} to the message queue of one of the
     * micro-services subscribed to {@code r.getClass()} in a round-robin
     * fashion.
     * <p>
     * @param r         the request to add to the queue.
     * @param requester the {@link MicroService} sending {@code r}.
     * @return true if there was at least one micro-service subscribed to
     *         {@code r.getClass()} and false otherwise.
     */
	@Override
	public boolean sendRequest(Request<?> r, MicroService requester) {
		synchronized (locker) { //synchronizing to prevent two different threads to create new RoundRobinQueue to the same request type
			if (messageMap.get(r.getClass())==null){
				messageMap.put(r.getClass(),new RoundRobinQueue());
			}
			register(requester);
			if(!microServiceMap.containsKey(requester) || messageMap.get(r.getClass()).isEmpty()){
				return false;
			}
			synchronized (r) {
				requestersMap.put(r, requester);
				MicroService m = messageMap.get(r.getClass()).poll();
				microServiceMap.get(m).add(r);
			}
			return true;
			}

		}
	
	/**
     * allocates a message-queue for the {@link MicroService} {@code m}.
     * <p>
     * @param m the micro-service to create a queue for.
     */
	@Override
	public void register(MicroService m) {
		synchronized (m){
			if (!microServiceMap.containsKey(m)){
			microServiceMap.put(m, new LinkedBlockingQueue<>());
			}
		}	
	}
	
	  /**
     * remove the message queue allocated to {@code m} via the call to
     * {@link #register(bgu.spl.mics.MicroService)} and clean all references
     * related to {@code m} in this message-bus. If {@code m} was not
     * registered, nothing happens.
     * <p>
     * @param m the micro-service to unregister.
     */
	@Override
	public void unregister(MicroService m) {
		synchronized (m) {
			if (microServiceMap.containsKey(m)){
				if(subscribedMessagesMap.get(m)!=null){
					while (!subscribedMessagesMap.get(m).isEmpty()){
						messageMap.get(subscribedMessagesMap.get(m).poll().getClass()).remove(m);
					}
					subscribedMessagesMap.remove(m); 
			}
				microServiceMap.remove(m);
				ShoeStoreRunner.LOGGER.info(m.getName() + " was unregistered.\n");
			}
		}
	}
	
	 /**
     * using this method, a <b>registered</b> micro-service can take message
     * from its allocated queue.
     * This method is blocking -meaning that if no messages
     * are available in the micro-service queue it
     * waits until a message became available.
     * The method throws {@link IllegalStateException} in the case
     * where {@code m} was never registered.
     * <p>
     * @param m the micro-service requesting to take a message from its message
     *          queue
     * @return the next message in the {@code m}'s queue (blocking)
     * @throws InterruptedException if interrupted while waiting for a message
     *                              to became available.
     */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
			if (!microServiceMap.containsKey(m)){
				throw new IllegalStateException("Micro service doesn't exist");
			}
			return microServiceMap.get(m).take();
	}
	
	/**
	 * initializes the fields for {@code m} in the subscribedMessagesMap 
	 * and initializes the round robin queue in the messageMap for messages of type {@code type}
	 * @param type
	 * @param m
	 */
	private void allocateMapsFields(Class<? extends Message> type, MicroService m){
		register(m);
		if (subscribedMessagesMap.get(m)==null){
			subscribedMessagesMap.put(m, new LinkedBlockingQueue<Class <? extends Message>>());
		}
		if (messageMap.get(type)==null){
			messageMap.put(type, new RoundRobinQueue());
		}	
	}
	
	/**
	 * this function is used only for tests
	 * @return messageMap 
	 */
	public ConcurrentHashMap<Class <? extends Message>, RoundRobinQueue> getMessageMap() {
		return messageMap;
	}
	
	/**
	 * this function is used only for tests
	 * @return microServiceMap 
	 */
	public ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> getMicroServiceMap() {
			return microServiceMap;
	}
	
	/**
	 * this function is used only for tests
	 * @return requestersMap 
	 */
	public ConcurrentHashMap<Request<?>, MicroService> getRequesterMap() {
		return requestersMap;
	}

}


