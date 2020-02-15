package floorSubsystem;

import java.util.*;
import scheduler.Scheduler;
import timer.Timer;

/**
 * The Floor class is one of the three subsystems of the elevator program. The
 * floor class is responsible for fetching the requests from a document. Then in
 * a polling loop, it sends the fetched request to the scheduler. It also
 * continuously listens for any messages from the scheduler.
 * 
 * For iteration 1, the floor send/read request to/from the scheduler.
 * 
 * @author Shoaib Khan, John Breton
 * @version Iteration 2 - February 15th, 2020
 */
public class Floor implements Runnable {

	/**
	 * ArrayDeque to store all the requests
	 */
	private Deque<RequestData> requestData;

	/**
	 * Scheduler instance to fetch and send requests to the scheduler
	 */
	private Scheduler scheduler;

	/**
	 * Used to calculate time that has passed (Simulates real life time).
	 */
	private Timer timer;

	/**
	 * The time the first request was sent (the start of the simulation).
	 */
	private String startTime;

	/**
	 * 
	 */
	private int startHour, startMinute, startSecond, startMillisecond;

	/**
	 * Default constructor to initialize the class variables
	 * 
	 * @param scheduler The scheduler the Floor will communicate with.
	 */
	public Floor(Scheduler scheduler) {
		requestData = new ArrayDeque<>();
		this.scheduler = scheduler;
		timer = new Timer();
	}

	/**
	 * Method to fetch the request from the file. Internally calls the parser class
	 * to fetch the requests.
	 * 
	 * @return The number of requests that were parsed from the file, as an int.
	 */
	public int fetchRequests() {
		Parser parser = new Parser();
		requestData = parser.getRequestFromFile();
		startTime = requestData.peek().getTime();
		return requestData.size();
	}

	/**
	 * Method to display all the requests in the queue.
	 */
	public void displayAllRequests() {
		for (RequestData currData : this.requestData)
			System.out.println(currData);
	}

	/**
	 * Initialize variables to keep track of the time the first request was sent,
	 * for timing purposes.
	 */
	private void convertStartTime() {
		String[] temp = startTime.split(":|\\.");
		startHour = Integer.parseInt(temp[0]);
		startMinute = Integer.parseInt(temp[1]);
		startSecond = Integer.parseInt(temp[2]);
		startMillisecond = Integer.parseInt(temp[3]);
	}

	/**
	 * Thread execution routine.
	 */
	@Override
	public void run() {

		// Fetch all the requests
		int totalRequests = fetchRequests();

		System.out.println("The simulations is beginning at time " + startTime);

		// Keep track of the time the simulation is beginning at for timing purposes.
		convertStartTime();

		// Keep track of the data received.
		int dataReceived = 0;

		// In a continuous polling loop, try sending and receiving data to/from the
		// scheduler.
		while (true) {
			try {
				// If the data received is less than the total fetched requests
				// Keep sending and receiving data to/from the scheduler.
				if (dataReceived < totalRequests) {
					String time[] = requestData.peek().getTime().split(":|\\.");
					// Wait until the correct amount of time has passed before sending the next
					// request (busy waiting loop).
					while (!timer.itsTime(Integer.parseInt(time[0]) - startHour,
							Integer.parseInt(time[1]) - startMinute, Integer.parseInt(time[2]) - startSecond,
							Integer.parseInt(time[3]) - startMillisecond));
					// Send request to the scheduler because it is now time to do so.
					scheduler.setRequest(requestData.pop());

					// Wait 200 ms to prevent grabbing what was just given to the scheduler.
					// Will be removed in future iterations.
					Thread.sleep(200);

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
