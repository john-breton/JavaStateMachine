package floorSubsystem;

/**
 * RequestData class is a data type to store the request from the request
 * document.
 * 
 * @auther Shoaib Khan
 * @version Iteration 2 - February 15th, 2020
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
     * 
     * @param string A String in following format: hh:mm:ss.mmm
     */
    public RequestData(String string) {
        // Parse the string and initialize all the variables.
        String[] info = string.split(" ");
        time = info[0];
        currentFloor = Integer.parseInt(info[1]);
        isGoingUp = info[2].contains("Up");
        destinationFloor = Integer.parseInt(info[3]);
    }
    
//    public RequestData(byte[] byte) {
//    	
//    }

    /**
     * Overloaded constructor to initialize the class variables
     * 
     * @param time             The time of the RequestData, as a string in the
     *                         format: hh:mm:ss.mmm
     * @param currentFloor     The current floor for the RequestData, as an int
     * @param isGoingUp        True if the request is wanting to go up, false
     *                         otherwise
     * @param destinationFloor The destination floor for the RequestData, as an int
     */
    public RequestData(String time, int currentFloor, boolean isGoingUp, int destinationFloor) {
        this.time = time;
        this.currentFloor = currentFloor;
        this.isGoingUp = isGoingUp;
        this.destinationFloor = destinationFloor;
    }

    /**
     * Return time of the request
     * 
     * @return time The time of the RequestData, as a string in the format:
     *         hh:mm:ss.mmm
     */
    public String getTime() {
        return time;
    }

    /**
     * Return the floor where the request was made
     * 
     * @return currentFloor The current floor for the RequestData, as an int
     */
    public int getCurrentFloor() {
        return currentFloor;
    }

    /**
     * Return if the elevator is going to move up or down
     * 
     * @return isGoingUp True if the elevator is moving up, false otherwise.
     */
    public boolean getIsGoingUp() {
        return isGoingUp;
    }

    /**
     * Return the destination floor of the request
     * 
     * @return destinationFloor The destination floor, as an integer.
     */
    public int getDestinationFloor() {
        return destinationFloor;
    }
    
    public byte[] toBytes() {
    	byte[] bytes = new byte[26];
    	int counter = 0;
    	
    	for (byte b: time.getBytes()) {
    		bytes[counter++] = b;
    	}    	
    	bytes[counter++] = (byte) ' ';
    	
    	String curFloor = currentFloor + "";
    	for (byte b: curFloor.getBytes()) {
    		bytes[counter++] = b;
    	}
    	bytes[counter++] = (byte) ' ';
    	
    	String direction = isGoingUp ? "Up" : "Down";
    	for (byte b: direction.getBytes()) {
    		bytes[counter++] = b;
    	}
    	bytes[counter++] = (byte) ' ';
    	
    	String destFloor = destinationFloor + "";
    	for (byte b: destFloor.getBytes()) {
    		bytes[counter++] = b;
    	}
    	bytes[counter++] = (byte) ' ';
    	
    	return bytes;
    }

    /**
     * Return the RequestData as a human readable formatted string.
     * 
     * @return The string representation of the RequestData.
     */
    @Override
    public String toString() {
        return "[" + time + "] Floor " + currentFloor + " --> Floor " + destinationFloor + " | "
                + (isGoingUp ? "UP" : "DOWN");
    }
}
