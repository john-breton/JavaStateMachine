/**
 * @author Osayimwen Justice Odia
 * @version Iteration 2 - February 15th, 2020
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
	 * Checks if the specified amount of time has passed.
	 * 
	 * @param hours        - hours of time passed in the form of HH:MM:SS.mm
	 * @param minutes      - minutes of time passed in the form of HH:MM:SS.mm
	 * @param seconds      - seconds of time passed in the form of HH:MM:SS.mm
	 * @param milliseconds - milliseconds parameter of time passed in the form of
	 *                     HH:MM:SS.mm
	 * @return boolean - True or false if the amount of time has passed
	 */
	public boolean itsTime(int hours, int minutes, int seconds, int milliseconds) {
		return (new Date().getTime() - time) > ((hours * 3600000) + (minutes * 60000) + (seconds * 1000)
				+ milliseconds);

	}
}