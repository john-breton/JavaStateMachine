package floorSubsystem;

import java.util.ArrayDeque;
import scheduler.Scheduler;

/**
 * The Floor class is one of the three subsystems of the elevator program. 
 * The floor class is responsible for fetching the requests from a document.
 * Then in a polling loop, it sends the fetched request to the scheduler. 
 * It also continuously listens for any messages from the scheduler. 
 * 
 * For iteration 1, the floor send/read request to/from the scheduler.
 * 
 * @author Shoaib Khan
 * @version Iteration 1 - February 1st, 2020
 */
public class Floor implements Runnable {

	/**
	 * ArrayDeque to store all the requests
	 */
	private ArrayDeque<RequestData> requestData;
	
	/**
	 * Scheduler instance to fetch and send requests to the scheduler
	 */
	private Scheduler scheduler;

	/**
	 * Default constructor to initialize the class variables
	 * @param 
	 */
	public Floor(Scheduler scheduler) {
		requestData = new ArrayDeque<>();
		this.scheduler = scheduler;
	}

	/**
	 * Method to fetch the request from the file. 
	 * Internally calls the parser class to fetch the requests. 
	 */
	public int fetchRequests() {
		Parser parser = new Parser();
		requestData = parser.getRequestFromFile();
		return requestData.size();
	}

	/**
	 * Method to display all the requests in the queue. 
	 */
	public void displayAllRequests() {
		for (RequestData requestData : this.requestData) {
			System.out.println(requestData);
		}
	}

	/**
	 * Thread execution routine.
	 */
	@Override
	public void run() {
		
		// Fetch all the requests
		int totalRequests = fetchRequests();
		
		// Keep track of the data received.
		int dataReceived = 0;
		
		// In a continuous polling loop, try sending and receiving data to/from the scheduler.  
		while (true) {
			try {
				// If the data received is less than the total fetched requests
				// Keep sending and receiving data to/from the scheduler.
				if (dataReceived < totalRequests) {
					// Send request to the scheduler.
					scheduler.setRequest(requestData.pop());
					
					// Wait 100 ms. 
					// THIS IS JUST USED FOR ITERATION 1.
					Thread.sleep(100);
					
					// Display message from the scheduler.
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
