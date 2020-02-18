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
 * @author John Breton, Shoaib Khan
 * @version Iteration 2 - February 15th, 2020
 */
public class Elevator implements Runnable {

	/**
	 * The queue used to keep track of the work for the Elevator.
	 */
	private Deque<RequestData> workQueue;

	/**
	 * A reference to where the Elevator is expecting to receive work from.
	 */
	private Scheduler scheduler;

	/**
	 * The current request the Elevator is handling.
	 */
	private RequestData currentRequest;

	/**
	 * Experimental value for iteration 2.
	 * Value taken from iteration 0 calculations
	 * Time it takes to move 1 floor at full acceleration 
	 */
	private static final double TIME_PER_FLOOR = 2.29;

	/**
	 * Construct a new Elevator.
	 */
	public Elevator() {
		workQueue = new ArrayDeque<RequestData>();
	}

	/**
	 * Overloaded constructor to set the class variables
	 * 
	 * @param scheduler
	 */
	public Elevator(Scheduler scheduler) {
		workQueue = new ArrayDeque<RequestData>();
		this.scheduler = scheduler;
	}

	/**
	 * Method to set the scheduler
	 * 
	 * @param scheduler
	 */
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Method to add a request to the elevator queue.
	 * @param requestData
	 */
	public void addToQueue(RequestData requestData) {
		workQueue.add(requestData);
	}

	/**
	 * Method to move the elevator to a particular floor
	 * @param floor
	 */
	private void move(int floor) {
		try {
			System.out.println("Elevator: Floor " + floor);
			
			// Wait while the elevator is moving in between floors
			Thread.sleep((long) (TIME_PER_FLOOR * 1000));
		} catch (InterruptedException e) {
			System.out.println("Error: Elevator cannot not move");
		}
	}

	/**
	 * Method to move floors
	 * @return
	 */
	private boolean moveFloors() {
		
		// Get the current and the destination floors
		int currentFloor = currentRequest.getCurrentFloor();
		int destinationFloor = currentRequest.getDestinationFloor();

		// If the elevator is moving up
		if (currentFloor < destinationFloor) {
			int count = currentFloor;
			while (count <= destinationFloor) {
				this.move(count);
				count++;
			}
			return true;
		}

		// If the elevator is moving down
		else if (currentFloor > destinationFloor) {
			int count = currentFloor;
			while (count >= destinationFloor) {
				this.move(count);
				count--;
			}
			return true;
		}

		// If the destination floor is the same as the current floor
		else {
			System.out.println("Elevator is already at the floor");
			return false;
		}
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
			System.out.print("");
			if (!workQueue.isEmpty()) {
				currentRequest = workQueue.pop();
				System.out.println("Elevator received information from Scheduler: " + currentRequest.toString());
				System.out.println(toString());
				if (this.moveFloors()) {
					try {
						scheduler.notifyScheduler(currentRequest);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
