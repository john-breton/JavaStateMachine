/**
 * 
 */
package timer;
import java.util.Date;


/**
 * @author osayimwense
 *
 */
public class Timer {
	private long time;


	/**
	 * 
	 */
	public Timer() {
		time = new Date().getTime();

	}
	
	public boolean itsTime(int seconds) {
		return (new Date().getTime()-time) > seconds;
	}

	public boolean itsTime(int minutes, int seconds) {
		return (new Date().getTime()-time) > ((minutes*60) + seconds);
	}
	
	public boolean itsTime(int hours, int minutes, int seconds) {
		return (new Date().getTime()-time) > ((hours*3600) + (minutes*60) + seconds);

	}
}
