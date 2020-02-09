/**
 * 
 */
package timer;

/**
 * @author osayimwense
 *
 */
public class Timer {

	/**
	 * 
	 */
	public Timer() {
		
	}
	
	
	public void count(int seconds) {
		
		try {
			Thread.sleep(1000*seconds);
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
