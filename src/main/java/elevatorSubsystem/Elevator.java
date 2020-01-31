package elevatorSubsystem;

import floorSubsystem.RequestData;
import scheduler.Scheduler;

import java.util.*;

/**
 * The Elevator class represents nodes within the elevatorSubsystem package.
 * Each Elevator serves as one node, continually polling the scheduler for
 * pending work. For Iteration 1, once an Elevator receives work from the
 * scheduler, it takes the information and immediately tries to sends it back to
 * the scheduler.
 *
 * @author John Breton
 * @version Iteration 1 - February 1st, 2020
 */
public class Elevator implements Runnable {
    
    private Deque<RequestData> workQueue;
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
    @Override
    public void run() {
        while (true) {
            try {
                workQueue.add(scheduler.getRequest());
                System.out.println("Elevator received information from Scheduler: " + workQueue.peek().toString());
                RequestData currentRequest = workQueue.pop();
                System.out.println("At time " + currentRequest.getTime() + ", the elevator is moving "
                        + (currentRequest.getIsGoingUp() ? "up" : "down") + " from floor " + currentRequest.getCurrentFloor()
                        + " to floor " + currentRequest.getDestinationFloor());
                scheduler.setRequest(currentRequest);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
