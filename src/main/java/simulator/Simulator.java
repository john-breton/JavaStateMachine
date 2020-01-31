package simulator;

import elevatorSubsystem.Elevator;
import floorSubsystem.Floor;
import scheduler.Scheduler;

/**
 * 
 * @author Shoaib Khan
 *
 */
public class Simulator {
    
    /**
     * Entry point for the application.
     *
     * @param args The command-line arguments that are passed when compiling the application.
     */
    public static void main(String[] args) {
        System.out.println("---> Simulation Started <--- \n");
        Scheduler scheduler = new Scheduler();
        Thread floor = new Thread(new Floor(scheduler), "Floor");
        Thread elevator = new Thread(new Elevator(scheduler), "Elevator");
        
        floor.start();
        elevator.start();
    }
}