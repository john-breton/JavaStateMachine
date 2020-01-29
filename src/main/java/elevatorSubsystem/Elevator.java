package elevatorSubsystem;

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
	 * Construct a new Elevator
	 */
	public Elevator() {
		workQueue = new ArrayDeque<>();
	}

	private void checkScheduler() {

	}

	private void passToScheduler(ArrayList data) {

	}

	@Override
	public void run() {
		// TODO: Complete run behaviour for the class.
	}

}
