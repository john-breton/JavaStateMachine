package floorSubsystem;

import java.util.ArrayDeque;
import java.util.Deque;

import scheduler.Scheduler;

/**
 * 
 * 
 * @author
 * @version Iteration 1 - February 1st, 2020
 */
public class Floor implements Runnable {

	private Deque<RequestData> requestData;
	private Scheduler scheduler;

	/**
	 *
	 */
	public Floor(Scheduler scheduler) {
		requestData = new ArrayDeque<>();
		this.scheduler = scheduler;
	}

	/**
	 *
	 */
	public int fetchRequests() {
		Parser parser = new Parser();
		requestData = parser.getRequestFromFile();
		return requestData.size();
	}

	/**
	 *
	 */
	public void displayAllRequests() {
		for (RequestData requestData : this.requestData) {
			System.out.println(requestData);
		}
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		int totalRequests = fetchRequests();
		int dataReceived = 0;
		while (true) {
			try {
				if (dataReceived < totalRequests) {
					scheduler.setRequest(requestData.pop());
					Thread.sleep(100);
					System.out.println("Floor received infromation from Scheduler: " + scheduler.getRequest().toString()
							+ "\nThat is success #" + ++dataReceived + "/" + totalRequests + "\n");
				} else 
					System.exit(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
