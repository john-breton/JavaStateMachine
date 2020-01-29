package elevatorSubsystem;

import floorSubsystem.RequestData;

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

    /**
     * Construct a new Elevator.
     */
    public Elevator() {
        workQueue = new ArrayDeque<RequestData>();
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public void run() {
        while (true) {
            // Insert try to retrieve data from scheduler here.
            // Insert try to pass to scheduler here.
        }
    }

}
