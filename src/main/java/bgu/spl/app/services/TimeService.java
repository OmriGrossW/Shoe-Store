package bgu.spl.app.services;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.app.ShoeStoreRunner;
import bgu.spl.app.Store;
import bgu.spl.app.TickTimerTask;
import bgu.spl.app.messages.TerminationBroadcast;
import bgu.spl.app.messages.TickBroadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.impl.MessageBusImpl;
/**
 * TimeService Class is a MicroService used for the time manage. 
 *
 */
public class TimeService extends MicroService {
	
	private AtomicInteger speed;
	private AtomicInteger duration;
	private Timer timer;
	
	/**
	 * TimeService Constructor
	 * @param speed the number of milliseconds for every tick
	 * @param duration the last tick to be done
	 */
	public TimeService(int speed , int duration) {
		super("Timer");
		this.speed=new AtomicInteger(speed);
		this.duration=new AtomicInteger(duration);
		timer = new Timer();
	}
	
	/**
	 * 
	 * @param currentTick the tick to send to all of the micro-services
	 */
	public void sendTickBroadcast(int currentTick) {
		sendBroadcast(new TickBroadcast(currentTick));
	}
	
	/**
	 * sets the new timer by the given speed, using a TickTimerTask to send the tickBroadcast and to finish on the given duration. 
	 */
	@Override
	protected void initialize() {
		ShoeStoreRunner.LOGGER.info("TimeService has started.\n");
		TickTimerTask task = new TickTimerTask(this, duration.get());
		timer.schedule(task, 0, speed.get());
		MessageBusImpl.getMessageBus().register(this);
		ShoeStoreRunner.LOGGER.info(this.getName() + " is initialized.\n");
	}
	
	/**
	 * used by TickTimerTask, activated when the current tick has passed the given duration. 
	 */
	public void finished(){
		timer.cancel();
		ShoeStoreRunner.LOGGER.info("Duration passed, the timer stopped!\n");
		sendBroadcast(new TerminationBroadcast());
		terminate();
		ShoeStoreRunner.LOGGER.info(this.getName() + " has terminated.\n");
		Store.getStore().printAndExit();
	}
	

}


