package floorSubsystem;

public class FloorSubSystem {
	private String time;
	private int currentFloor;
	private boolean isGoingUp;
	private int destiniationFloor;
	
	public FloorSubSystem() {
		time = "";
		currentFloor = 0;
		isGoingUp = false;
		destiniationFloor = 0;
	}
	
	public FloorSubSystem(String string) {
		String[] info = string.split(" ");
		time = info[0];
		currentFloor = Integer.parseInt(info[1]);
		isGoingUp = info[2].contains("Up");
		destiniationFloor = Integer.parseInt(info[3]);
	}
	
	public FloorSubSystem(String time, int currentFloor, boolean isGoingUp, int destiniationFloor) {
		this.time = time;
		this.currentFloor = currentFloor;
		this.isGoingUp = isGoingUp;
		this.destiniationFloor = destiniationFloor;
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
	
	public int getDestiniationFloor() {
		return destiniationFloor;
	}
	
	public void setDestiniationFloor(int destiniationFloor) {
		this.destiniationFloor = destiniationFloor;
	}
	
	@Override
	public String toString() {
		return "[" + time + "] Floor " + currentFloor + " --> Floor " + destiniationFloor + " | " + (isGoingUp ? "UP" : "DOWN");
	}
}
