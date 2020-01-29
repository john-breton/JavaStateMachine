package floorSubsystem;

public class RequestData {
    private String time;
    private int currentFloor;
    private boolean isGoingUp;
    private int destinationFloor;

    public RequestData() {
        time = "";
        currentFloor = 0;
        isGoingUp = false;
        destinationFloor = 0;
    }

    public RequestData(String string) {
        String[] info = string.split(" ");
        time = info[0];
        currentFloor = Integer.parseInt(info[1]);
        isGoingUp = info[2].contains("Up");
        destinationFloor = Integer.parseInt(info[3]);
    }

    public RequestData(String time, int currentFloor, boolean isGoingUp, int destinationFloor) {
        this.time = time;
        this.currentFloor = currentFloor;
        this.isGoingUp = isGoingUp;
        this.destinationFloor = destinationFloor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public boolean getIsGoingUp() {
        return isGoingUp;
    }

    public void setIsGoingUp(boolean isGoingUp) {
        this.isGoingUp = isGoingUp;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public void setDestinationFloor(int destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    @Override
    public String toString() {
        return "[" + time + "] Floor " + currentFloor + " --> Floor " + destinationFloor + " | " + (isGoingUp ? "UP" : "DOWN");
    }
}
