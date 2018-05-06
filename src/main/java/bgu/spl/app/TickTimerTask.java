package bgu.spl.app;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.app.services.TimeService;

/**
 * The Class TickTimerTask.
 */
public class TickTimerTask extends TimerTask{
	
	private TimeService timeService;
	private int duration;
	private AtomicInteger currentTick; //make atomic integer
	
	/**
	 * TickTimerTask Constructor.
	 *
	 * @param timeService the time service
	 * @param duration the duration of the timer
	 */
	public TickTimerTask(TimeService timeService, int duration){
		this.timeService = timeService;
		this.duration = duration;
		this.currentTick = new AtomicInteger(1);//make new atomic integer
	}


	@Override
	public void run() {
		if (currentTick.intValue()==duration+1){
			this.cancel();
			timeService.finished();
			
		}
		else{
			timeService.sendTickBroadcast(currentTick.intValue());
			ShoeStoreRunner.LOGGER.info("Current Tick: " + currentTick +".\n");
			currentTick.incrementAndGet();
	
		}
	}
	
	
	
}

