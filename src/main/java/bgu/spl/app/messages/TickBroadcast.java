package bgu.spl.app.messages;

import bgu.spl.mics.Broadcast;

/**
 *  TickBroadcast Class is a broadcast who updates the time for every service.
 */
public class TickBroadcast implements Broadcast {

	private int currentTick;
	
	/**
	 * TickBroadcast Constructor.
	 *
	 * @param currentTick the current tick
	 */
	public TickBroadcast(int currentTick){
		this.currentTick=currentTick;
	}
	
	/**
	 * Gets the current tick.
	 *
	 * @return the current tick
	 */
	public int getCurrentTick() {
		return currentTick;
	}
	
}
