package floorSubsystem;

/**
 * RequestData class is a data type to store the request from the request document.
 * 
 * @auther Shoaib Khan
 */
public class RequestData {
	
	/**
	 * Time of the request
	 */
    private String time;
    
    /**
     * Floor when the request was made
     */
    private int currentFloor;
    
    /**
     * If the elevator should be moving up or down
     */
    private boolean isGoingUp;
    
    /**
     * The destination floor of the request
     */
    private int destinationFloor;

    /**
     * Default constructor to initialize the class variables to a default value.
     */
    public RequestData() {
        time = "";
        currentFloor = 0;
        isGoingUp = false;
        destinationFloor = 0;
    }
    
    /**
     * Overloaded constructor to initialize the class variables from a string
     * @param string - RequestData string
     */
    public RequestData(String string) {
    	// Parse the string and initialize all the variables.
        String[] info = string.split(" ");
        time = info[0];
        currentFloor = Integer.parseInt(info[1]);
        isGoingUp = info[2].contains("Up");
        destinationFloor = Integer.parseInt(info[3]);
    }

    /**
     * Overloaded constructor to initialize the class variables
     * @param time
     * @param currentFloor
     * @param isGoingUp
     * @param destinationFloor
     */
    public RequestData(String time, int currentFloor, boolean isGoingUp, int destinationFloor) {
        this.time = time;
        this.currentFloor = currentFloor;
        this.isGoingUp = isGoingUp;
        this.destinationFloor = destinationFloor;
    }

    /**
     * Method to get the time of the request
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * Method to set the time of the request
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Method to get the floor where the request was made
     * @return currentFloor
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Method to set the floor where the request was made
     * @param currentFloor
     */
    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    /**
     * Method to return if the elevator is going to moving up or down
     * @return isGoingUp
     */
    public boolean getIsGoingUp() {
        return isGoingUp;
    }

    /**
     * Method to set if the elevator is going up
     * @param isGoingUp
     */
    public void setIsGoingUp(boolean isGoingUp) {
        this.isGoingUp = isGoingUp;
    }

    /**
     * Method to get the destination floor of the request
     * @return destinationFloor
     */
    public int getDestinationFloor() {
        return destinationFloor;
    }

    /**
     * Method to set the destination floor of the request
     * @param destinationFloor
     */
    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    /**
     * Method to display the class.
     */
    @Override
    public String toString() {
        return "[" + time + "] Floor " + currentFloor + " --> Floor " + destinationFloor + " | " + (isGoingUp ? "UP" : "DOWN");
    }
}
