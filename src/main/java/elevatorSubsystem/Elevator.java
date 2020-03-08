package elevatorSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Deque;

//import elevatorSubsystem.ElevatorSubsystem.State;
import floorSubsystem.RequestData;

/**
 * The Elevator class represents nodes within the elevatorSubsystem package.
 * Each Elevator serves as one node, continually polling the scheduler for
 * pending work. For Iteration 1, once an Elevator receives work from the
 * scheduler, it takes the information and immediately tries to sends it back to
 * the scheduler.
 *
 * @author John Breton, Shoaib Khan
 * @version Iteration 3 - March 6th, 2020
 */
public class Elevator implements Runnable {

    private enum State {
        IDLE, MOVINGUP, MOVINGDOWN, ARRIVED;
    }

    private static State state;

    /**
     * The queue used to keep track of the work for the Elevator.
     */
    private Deque<RequestData> workQueue;

    /**
     * The current request the Elevator is handling.
     */
    protected RequestData currentRequest;

    /**
     * Experimental value for iteration 2. Value taken from iteration 0 calculations
     * Time it takes to move 1 floor at full acceleration
     * 
     */
    private static final double TIME_PER_FLOOR = 1;

    private static final int DATA_SIZE = 26;
    
    private int currentFloor; 

    /**
     * Construct a new Elevator.
     */
    public Elevator() {
        state = State.IDLE;
        currentFloor = 0;
        workQueue = new ArrayDeque<RequestData>();
        
        // To test
        // Add data in the work queue
        // workQueue.add(new RequestData("14:05:15.0 2 Up 4"));
        // workQueue.add(new RequestData("14:05:25.0 4 Down 3"));
    }

    /**
     * Method to add a request to the elevator queue.
     * 
     * @param requestData
     */
    public void addToQueue(RequestData requestData) {
        workQueue.add(requestData);
    }
    
    public String getStatus() {
    	String status;
    	status = state.toString() + "|";
    	status += currentFloor + "|";
    	
    	if(currentRequest != null) {
    		status += currentRequest.getDestinationFloor() + "|";
    	}
    	
    	else {
    		status += "0|"; 
    	}
    	
    	for (RequestData data: workQueue) {
    		status += data.getSimpleForm();
    	}
    	
    	if (workQueue.isEmpty()) {
    		status += "empty";
    	}
    	
    	return status;
    }

    /**
     * Advances the elevator to the next state.
     */
    private void goToNextState() {

        if (getState() == State.ARRIVED)
            state = State.IDLE;
        else if (getState() == State.IDLE) {
            state = State.MOVINGUP;
        } else if (getState() == State.MOVINGUP) {
            state = State.MOVINGDOWN;
        } else if (getState() == State.MOVINGDOWN) {
            state = State.ARRIVED;
        }

    }

    /**
     * Get the current state of the elevator.
     * 
     * @return The current state of the elevator.
     */
    public State getState() {
        return state;
    }

    /**
     * Method to move the elevator to a particular floor
     * 
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
     * 
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

//    private void addRequestToQueue(RquestData data) {
//        RequestData data = new RequestData(receivePacket.getData());
//        this.workQueue.add(data);
//    }

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

    private void doWork() {
        if (!workQueue.isEmpty()) {
            currentRequest = workQueue.pop();
            System.out.println("Elevator received information from Scheduler: " + currentRequest.toString());
            System.out.println(toString());
            if (this.moveFloors()) {
                System.out.println();
            }
        }
    }

    /**
     * Thread execution routine.
     */
    @Override
    public void run() {
//        while (true) {
//            this.receivePacketFromScheduler();
//            this.doWork();
//            System.out.println("---------------------------------------------------------------------");
//        }
    }
}
