package scheduler;

import java.util.*;

import elevatorSubsystem.Elevator;
import floorSubsystem.RequestData;

/**
 * The Scheduler class in one the three subsystems of the elevator program. The
 * scheduler is responsible for scheduling requests to ensure maximum throughput
 * and minimize waiting time.
 * 
 * For Iteration 1: The scheduler receives request from the floor and passes it
 * to the elevator. Then, it receives request from the elevator and passes it
 * back to the floor.
 * 
 * @author John Breton, Shoaib Khan
 * @version Iteration 2 - February 15th, 2020
 */
public class Scheduler implements Runnable {

    /**
     * Deque to store all the requests
     */
    private Deque<RequestData> requestData;
    
    /**
     * Notification request to store request from the elevator
     */
    private RequestData notifiedRequest;
    
    /**
     * Elevator instance to add the request to its work queue
     */
    private Elevator elevator; 


    /**
     * Default constructor to initialize the class variables.
     */
    public Scheduler() {
        requestData = new ArrayDeque<>();
        notifiedRequest = null;
        elevator = null;
    }
    
    /**
     * Overloaded constructor to initialize the class variables.
     */
    public Scheduler(Elevator elevator) {
        requestData = new ArrayDeque<>();
        notifiedRequest = null;
        this.elevator = elevator;
    }

    /**
     * Method to set and store the requests from the elevator and floor threads.
     * 
     * @param requestData The request being sent from either the elevator or floor
     *                    thread
     * @throws InterruptedException Thrown if a thread is interrupted while
     *                              accessing the method
     */
    public synchronized void setRequest(RequestData requestData) throws InterruptedException {

        // If a request is already pending.
        if (!this.requestData.isEmpty()) {
            // Make the thread that is making the request to wait.
            this.wait();
        }

        // Print out a message to notify where the request is coming is from and what
        // the request it.
        System.out.println(
                "\nScheduler received information from " + Thread.currentThread().getName() + ": " + requestData);
        
        elevator.addToQueue(requestData);

        // Notify the all the other threads to start sending and receiving again.
        notifyAll();
    }
    
    /**
     * Method to notify the scheduler
     * @param requestData
     * @throws InterruptedException
     */
    public synchronized void notifyScheduler(RequestData requestData) throws InterruptedException {
    	notifiedRequest = requestData;
    	 System.out.println(
                 "Scheduler received information from " + Thread.currentThread().getName() + ": " + requestData);
    	 notifyAll();
    }
    
    /**
     * Method to get all the notified request from the elevator
     * @return
     */
    public synchronized RequestData getNotifiedRequest() {
    	if (notifiedRequest != null) {
    		RequestData request = notifiedRequest;
    		notifiedRequest = null;
    		return request;
    	}
    	return notifiedRequest;
    }

    /**
     * Return the most current pending request from the queue.
     * 
     * @return The RequestData being retrieved from the queue.
     * @throws InterruptedException Thrown if a thread is interrupted while
     *                              accessing the method
     */
    public synchronized RequestData getRequest() throws InterruptedException {

        // If there are no pending requests
        if (requestData.isEmpty()) {

            // Make the thread that is making the request to wait.
            this.wait();
        }

        // Notify the all the other threads to start sending and receiving again.
        notifyAll();

        // Return the request
        return requestData.pop();
	}

	/**
	 * Return the most current pending request from the queue.
	 * 
	 * @return The RequestData being retrieved from the queue.
	 * @throws InterruptedException Thrown if a thread is interrupted while
	 *                              accessing the method
	 */
	public synchronized RequestData getRequest() throws InterruptedException {

		// If there are no pending requests
		if (requestData.isEmpty()) {

			// Make the thread that is making the request to wait.
			this.wait();
		}

		// Notify the all the other threads to start sending and receiving again.
		notifyAll();

		// Return the request
		return requestData.pop();
	}

	/**
	 * Thread execution routine.
	 */
	@Override
	public void run() {
		while (true) {
			// This is where the scheduler would actually schedule requests.
			// But that isn't required for Iteration 1, so instead it does nothing.
		}

	}
}
