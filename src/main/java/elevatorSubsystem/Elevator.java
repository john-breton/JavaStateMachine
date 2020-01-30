package elevatorSubsystem;

import floorSubsystem.RequestData;
import scheduler.Scheduler;

import java.util.*;

/**
 * The Elevator class represents nodes within the elevatorSubsystem package.
 * Each Elevator serves as one node, continually polling the scheduler for pending work.
 * For Iteration 1, once an Elevator receives work from the scheduler, it takes the
 * information and immediately tries to sends it back to the scheduler.
 *
 * @author John Breton
 * @version Iteration 1 - February 1st, 2020
 */
public class Elevator implements Runnable {
    private Deque workQueue;
    private Scheduler scheduler;

    /**
     * Construct a new Elevator.
     */
    public Elevator(Scheduler scheduler) {
        workQueue = new ArrayDeque<RequestData>();
        this.scheduler = scheduler;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return "";
    }
    
    /**
     * 
     */
    public void startThread() {
    	Thread thread = new Thread(this);
    	thread.start();
    }
    
    /**
     * 
     */
    public void receiveRequests() {
    	try {
			if (scheduler.getRequest() != null) {
				System.out.println("Elevator reached information from Scheduler: " + scheduler.getRequest());
				System.exit(0);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    /**
     * 
     */
    @Override
    public void run() {
        while (true) {
        	this.receiveRequests();
        }
    }

}
