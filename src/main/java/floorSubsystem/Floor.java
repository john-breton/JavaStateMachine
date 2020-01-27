package floorSubsystem;

import java.util.ArrayList;

/**
 * 
 * @author 
 * @version Iteration 1 - February 1st, 2020
 */
public class Floor implements Runnable {
	private ArrayList<FloorSubSystem> floorSubSystems;
	
	public Floor() {
		floorSubSystems = new ArrayList<>();
	}
	
	public ArrayList<FloorSubSystem> getFloorSubSystems() {
		return floorSubSystems;
	}
	
	public void fetchFloorSubSystems() {
		Parser parser = new Parser();
		floorSubSystems = parser.getFloorSubSystems();
	}
	
	public void displayAllSubSystems() {
		for (FloorSubSystem floorSubSystem: floorSubSystems) {
			System.out.println(floorSubSystem);
		}
	}
	
	public void startThread() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void notifyScheduler() {
		// Add code here
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String args[]) {
		Floor floor = new Floor();
		floor.fetchFloorSubSystems();
		floor.displayAllSubSystems();
	}
}
