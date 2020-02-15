package simulator;

import elevatorSubsystem.Elevator;
import floorSubsystem.Floor;
import scheduler.Scheduler;

/**
 * The Simulator class is responsible to running the multi-threaded application
 * It contains the classes Thread, Scheduler, Floor and Elevator
 * 
 * @author Shoaib Khan, John Breton
 * @version Iteration 1 - February 1st, 2020
 */
public class Simulator {

    /**
     * Entry point for the application.
     *
     * @param args The command-line arguments that are passed when compiling the
     *             application.
     */
    public static void main(String[] args) {
        System.out.println("---> Simulation Started <--- \n");
        Elevator elevator = new Elevator();
        Scheduler scheduler = new Scheduler(elevator);
        elevator.setScheduler(scheduler);
        Thread floorThread = new Thread(new Floor(scheduler), "Floor");
        Thread schedulerThread = new Thread(scheduler, "Scheduler");
        Thread elevatorThread = new Thread(elevator, "Elevator");

        floorThread.start();
        schedulerThread.start();
        elevatorThread.start();
    }
}