package scheduler;

import floorSubsystem.RequestData;

/**
 * @author
 * @version Iteration 1 - February 1st, 2020
 */
public class Scheduler {
    private RequestData requestData;
	
	public Scheduler() {
		requestData = null;
	}
	
	public synchronized void setRequest(RequestData requestData) throws InterruptedException {
		if (this.requestData != null) {
			this.wait();
			return;
		}
		
		this.requestData = requestData;
		System.out.println("Scheduler reached information from Floor: " + requestData);
	}
	
	public synchronized RequestData getRequest() throws InterruptedException {
		if (this.requestData == null) {
			this.wait();
			return null;
		}
		
		return requestData;
	}
	
	// NEED TO MAKE THIS INTO A THREAD AS WELL
}
