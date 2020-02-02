package scheduler;

import java.util.*;

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
 * @version Iteration 1 - February 1st, 2020
 */
public class Scheduler implements Runnable {

    /**
     * Deque to store all the requests
     */
    private Deque<RequestData> requestData;

    /**
     * Default constructor to initialize the class variables.
     */
    public Scheduler() {
        requestData = new ArrayDeque<>();
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

        // Add the request in the queue.
        this.requestData.add(requestData);

        // Print out a message to notify where the request is coming is from and what
        // the request it.
        System.out.println(
                "Scheduler received information from " + Thread.currentThread().getName() + ": " + requestData);

        // Notify the all the other threads to start sending and receiving again.
        notifyAll();
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
