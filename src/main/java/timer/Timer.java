/**
 * @author Osayimwen Justice Odia
 */
package timer;
import java.util.Date;


/**
 * An emulation of a timer in the context of the SYSC3303 Group assignment
 */
public class Timer {
	private long time;

	/**
	 * The Timer is used to measure and compare amount of time that has passed
	 */
	public Timer() {
		time = new Date().getTime();
	}
	
	/**
	 * 
	 * Checks if the specified number of time has passed
	 * @param seconds - seconds of time passed in the form of SS
	 * @return boolean - True or false if the amount of time has passed
	 */
	public boolean itsTime(int seconds) {
		return (new Date().getTime()-time) > seconds;
	}

	
	/**
	 * 
	 * Checks if the specified amount of time has passed.
	 * @param minutes - minutes parameter of time passed in the form of MM:SS
	 * @param seconds - seconds parameter of time passed in the form of MM:SS
	 * @return boolean - True or false if the amount of time has passed
	 */
	public boolean itsTime(int minutes, int seconds) {
		return (new Date().getTime()-time) > ((minutes*60) + seconds);
	}
	
	
	/**
	 * 
	 *Checks if the specified amount of time has passed.
	 * @param hours - hours of time passed in the form of HH:MM:SS
	 * @param minutes - minutes of time passed in the form of HH:MM:SS
	 * @param seconds - seconds of time passed in the form of HH:MM:SS
	 * @return boolean - True or false if the amount of time has passed
	 */
	public boolean itsTime(int hours, int minutes, int seconds) {
		return (new Date().getTime()-time) > ((hours*3600) + (minutes*60) + seconds);

	}
}
