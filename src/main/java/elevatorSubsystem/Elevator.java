package elevatorSubsystem;

import java.util.ArrayList;

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
        IDLE, MOVINGUP, MOVINGDOWN, ARRIVED
    }

    private State state;

    /**
     * The queue used to keep track of the work for the Elevator.
     */
    private ArrayList<RequestData> workQueue;

    /**
     * The current request the Elevator is handling.
     */
    protected RequestData currentRequest;

    /**
     * Experimental value for iteration 2. Value taken from iteration 0 calculations
     * Time it takes to move 1 floor at full acceleration
     */
    private static final double TIME_PER_FLOOR = 1;

    private static final int DATA_SIZE = 26;

    private int currentFloor;

    /**
     * Construct a new Elevator.
     */
    public Elevator() {
        state = State.IDLE;
        currentFloor = 1;
        workQueue = new ArrayList<>();

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
    public synchronized void addToQueue(RequestData requestData, int index) {
        workQueue.add(index, requestData);
        notifyAll();
    }

    public String getStatus() {
        StringBuilder status;
        status = new StringBuilder(state.toString() + "|");
        status.append(currentFloor).append("|");

        if (currentRequest != null) {
            status.append(currentRequest.getDestinationFloor()).append("|");
        } else {
            status.append("0|");
        }

        for (RequestData data : workQueue) {
            status.append(data.getSimpleForm());
        }

        if (workQueue.isEmpty()) {
            status.append("empty");
        }

        return status.toString();
    }

    /**
     * Advances the elevator to the next state.
     */
    private void goToNextState(boolean up) {

        if (getState() == State.ARRIVED)
            state = State.IDLE;
        else if (getState() == State.IDLE && up) {
            state = State.MOVINGUP;
        } else if (getState() == State.MOVINGUP) {
            state = State.ARRIVED;
        } else if (getState() == State.MOVINGDOWN) {
            state = State.ARRIVED;
        } else
            state = State.MOVINGDOWN;
        if (getState() == State.IDLE) {
            System.out.println("Elevator " + Thread.currentThread().getName() + " state has been updated to: " + getState() + "\n");
        } else {
            System.out.println("Elevator " + Thread.currentThread().getName() + " state has been updated to: " + getState());
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
            System.out.println("Elevator " + Thread.currentThread().getName() + ": Floor " + floor);

            // Wait while the elevator is moving in between floors
            Thread.sleep((long) (TIME_PER_FLOOR * 1000));
            currentFloor = floor;
        } catch (InterruptedException e) {
            System.out.println("Error: Elevator cannot not move");
        }
    }

    /**
     * Method to move floors
     *
     * @return
     */
    public boolean moveFloors(int currentFloor, int destinationFloor) {

        if (this.currentFloor != currentFloor) {
//        	goToNextState(this.currentFloor < currentFloor);
        	this.moveFloors(this.currentFloor, currentFloor - 1);	
        	goToNextState(true);
        }
        
        // If the elevator is moving up
        if (currentFloor < destinationFloor) {

            int count = currentFloor;
            goToNextState(true);
            while (count <= destinationFloor) {
                this.move(count);
                count++;
            }
            goToNextState(true);
            // Simulate the doors opening and closing
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        // If the elevator is moving down
        else if (currentFloor > destinationFloor) {
            goToNextState(false);
            int count = currentFloor;
            while (count >= destinationFloor) {
                this.move(count);
                count--;
            }
            goToNextState(true);
            // Simulate the doors opening and closing
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        // If the destination floor is the same as the current floor
        else {
            System.out.println("Elevator is already at the floor");
            return false;
        }
    }

    public synchronized void haveWork() throws InterruptedException {
        while (workQueue.isEmpty())
            wait();
    }

    /**
     * Return the current movement of the Elevator, as a String.
     *
     * @return The string representation of the movement the Elevator is currently
     * completing.
     */
    @Override
    public String toString() {
        return "At time " + currentRequest.getTime() + ", the elevator is moving "
                + (currentRequest.getIsGoingUp() ? "up" : "down") + " from floor " + currentRequest.getCurrentFloor()
                + " to floor " + currentRequest.getDestinationFloor();
    }

    private void doWork() {
        try {
            haveWork();
            currentRequest = workQueue.remove(0);
            System.out.println("Elevator received information from Scheduler: " + currentRequest.toString());
            System.out.println(toString());
            if (this.moveFloors(currentRequest.getCurrentFloor(), currentRequest.getDestinationFloor())) {
                System.out.println("-> Elevator " + Thread.currentThread().getName() + " has moved to the floor " + currentRequest.getDestinationFloor());
                goToNextState(true);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Thread execution routine.
     */
    @Override
    public void run() {
        while (true) {
            this.doWork();
        }
    }
}
