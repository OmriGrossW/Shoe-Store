package bgu.spl.app.json;


/**
 * The Class Time.
 * Json Class
 */
public class Time {

	private int speed;
	private int duration;
	
	/**
	 * Time Copy Constructor.
	 *
	 * @param time
	 */
	public Time(Time time) {
		speed = time.speed; 
		duration = time.duration;
	}
	
	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public int getSpeed(){
		return speed;
	}
	
	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public int getDuration(){
		return duration;
	}

}
