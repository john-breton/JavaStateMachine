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

    // The queue used to keep track of the work for the Elevator.
    private Deque<RequestData> workQueue;

    // A reference to where the Elevator is expecting to receive work from.
    private Scheduler scheduler;

    // The current request the Elevator is handling.
    private RequestData currentRequest;

    /**
     * Construct a new Elevator.
     */
    public Elevator(Scheduler scheduler) {
        workQueue = new ArrayDeque<RequestData>();
        this.scheduler = scheduler;
    }

    /**
     * Return the current movement of the Elevator, as a String.
     * 
     * @return The string representation of the movement the Elevator is currently
     *         completing.
     */
    @Override
    public String toString() {
        return "At time " + currentRequest.getTime() + ", the elevator is moving "
                + (currentRequest.getIsGoingUp() ? "up" : "down") + " from floor " + currentRequest.getCurrentFloor()
                + " to floor " + currentRequest.getDestinationFloor();
    }

    /**
     * Thread execution routine.
     */
    @Override
    public void run() {
        while (true) {
            try {
                // Sleep the thread to ensure it does not grab what it just sent to the
                // scheduler (A hack solution for the purposes of Iteration 1).
                Thread.sleep(100);
                // Try to add to the Elevator's workQueue, assuming the Scheduler has something
                // for the Elevator to grab.
                workQueue.add(scheduler.getRequest());
                // For Iteration 1, we immediately return what we grab from the Scheduler, so we
                // pop the workQueue.
                currentRequest = workQueue.pop();
                // Confirmation that data has been received is printed to the console.
                System.out.println("Elevator received information from Scheduler: " + currentRequest.toString());
                System.out.println(toString());
                // Try to give what we just grabbed back to the Scheduler so that it can be
                // passed along to the Floor.
                scheduler.setRequest(currentRequest);
            } catch (InterruptedException e) {
                // In future Iterations, we will make use of a log file to track when exceptions
                // occur.
                e.printStackTrace();
            }
        }
    }

}
