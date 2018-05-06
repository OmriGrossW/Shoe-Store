package bgu.spl.app;

import java.util.concurrent.LinkedBlockingQueue;

import bgu.spl.app.messages.ManufacturingOrderRequest;
import bgu.spl.app.services.ShoeFactoryService;

/**
 * The Class ManufacturingInstance.
 * used in the {@link ShoeFactoryService}, stores the information needed for production.
 */
public class ManufacturingInstance {
	
	private ManufacturingOrderRequest request;
	private LinkedBlockingQueue<ManufacturingOrderRequest> reservedShoesRequesters = new LinkedBlockingQueue<ManufacturingOrderRequest>(); // stores the requests (reservers) that were added to a specific manufacturing instance.
	private int toBeProduced;
	private int availableShoes;
	private int finishTick;
	
	/**
	 * ManufacturingInstance Constructor.
	 *
	 * @param request the request
	 * @param finishTick the finish tick
	 */
	public ManufacturingInstance(ManufacturingOrderRequest request, int finishTick){
		this.request = request;
		toBeProduced = request.getAmount();
		availableShoes = toBeProduced-1;
		this.finishTick = finishTick;
	}
	
	/**
	 * Adds a request to the reserved shoes requesters list.
	 *
	 * @param request the request to add to the list
	 */
	public void addToRequestersList(ManufacturingOrderRequest request){
		reservedShoesRequesters.add(request);
	}
	
	/**
	 * Gets the reservedShoesRequesters list.
	 *
	 * @return the reserved shoes requesters list
	 */
	public LinkedBlockingQueue<ManufacturingOrderRequest> getRequestersList() {
		return reservedShoesRequesters;
	}
	
	/**
	 * Gets the request that initialized this instance.
	 *
	 * @return the request
	 */
	public ManufacturingOrderRequest getRequest() {
		return request;
	}

	/**
	 * Gets the shoe type.
	 *
	 * @return the shoe type
	 */
	public String getShoeType(){
		return request.getShoeType();
	}
	
	/**
	 * Gets the request amount.
	 *
	 * @return the amount
	 */
	public int getAmount(){
		return request.getAmount();
	}
	
	/**
	 * Gets the amount to be produced.
	 *
	 * @return the to be produced
	 */
	public int getToBeProduced(){
		return toBeProduced;
	}	

	/**
	 * Gets the finish tick.
	 *
	 * @return the finish tick
	 */
	public int getFinishTick() {
		return finishTick;
	}
	
	/**
	 * Gets the request time.
	 *
	 * @return the request time
	 */
	public int getRequestTime(){
		return request.getTime();
	}
	
	/**
	 * Gets the amount of available shoes to be reserved from the specific ManufacturingInstance.
	 *
	 * @return the amount of available shoes
	 */
	public int getAvailableShoes() {
		return availableShoes;
	}
	
	/**
	 * decreases the amount to be produced.
	 */
	public void decreaseToBeProduced(){
		toBeProduced = toBeProduced-1;
	}
	
	/**
	 * decreases the available shoes amount.
	 */
	public void decreaseAvailableShoes() {
		availableShoes = availableShoes-1;
	}



	
}
