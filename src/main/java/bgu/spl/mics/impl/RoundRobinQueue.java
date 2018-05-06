package bgu.spl.mics.impl;

import java.util.concurrent.*;

import org.junit.experimental.theories.internal.SpecificDataPointsSupplier;

import bgu.spl.mics.MicroService;

public class RoundRobinQueue {
	private LinkedBlockingQueue<MicroService> exeQueue;
	private LinkedBlockingQueue<MicroService> waitQueue;
	
	/**
	 * empty constructor.
	 */
	public RoundRobinQueue(){ 
		exeQueue = new LinkedBlockingQueue<MicroService>();
		waitQueue = new LinkedBlockingQueue<MicroService>();
	}
	/**
	 * adds (@param m) to the round-robin queue
	 * 
	 */
	public synchronized void add(MicroService m){
		try {
			exeQueue.put(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns and removes the next {@link MicroService} in the queue by round robin fashion.
	 * @return the next {@link MicroService} in the queue.
	 */
	public synchronized MicroService poll(){
		if (this.isEmpty()) {
			throw new NullPointerException("The RRQueue is Empty");
		}
		MicroService mTemp = exeQueue.poll();
		try {
			waitQueue.put(mTemp);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (exeQueue.isEmpty()){
			exeQueue = waitQueue;
			waitQueue = new LinkedBlockingQueue<MicroService>();
		}
		return mTemp;	
	}
	
	/**
	 * 
	 * @return {@link LinkedBlockingQueue} contains the {@link MicroService} instances of the specific {@link RoundRobinQueue}. 
	 */
	public LinkedBlockingQueue<MicroService> getRRQueue(){
		LinkedBlockingQueue<MicroService> q = new LinkedBlockingQueue<MicroService>(exeQueue);
		LinkedBlockingQueue<MicroService> tempQ = new LinkedBlockingQueue<MicroService>(waitQueue);
		while(!tempQ.isEmpty()){
			q.add(tempQ.poll());
		}
		return q;
	}
	
	/**
	 * removes {@link MicroService} from the queue.
	 * @param m the {@link MicroService} to be removed from the queue.
	 */
	public void remove(MicroService m){
		if (exeQueue.contains(m)) {
			exeQueue.remove(m);
		}
		else if (waitQueue.contains(m)) {
			waitQueue.remove(m);
		}
		
	}
	/**
	 * 
	 * @return true if the queue is empty, false otherwise.
	 */
	public boolean isEmpty(){
		return (exeQueue.isEmpty() && waitQueue.isEmpty());
	}
	
	/**
	 * @return the next {@link MicroService} in the queue, if the queue is empty, returns null.
	 */
	public MicroService peek() {
		if (!exeQueue.isEmpty()){
			return exeQueue.peek();
		}
		if (!waitQueue.isEmpty()){
			return waitQueue.peek();
		}
		return null;
	}
}
