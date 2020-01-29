package floorSubsystem;

import java.util.ArrayList;

/**
 * @author
 * @version Iteration 1 - February 1st, 2020
 */
public class Floor implements Runnable {
    private ArrayList<RequestData> requestData;

    /**
     *
     */
    public Floor() {
        requestData = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public ArrayList<RequestData> getRequestData() {
        return requestData;
    }

    /**
     *
     */
    public void fetchFloorSubSystems() {
        Parser parser = new Parser();
        requestData = parser.getFloorSubSystems();
    }

    /**
     *
     */
    public void displayAllSubSystems() {
        for (RequestData requestData : this.requestData) {
            System.out.println(requestData);
        }
    }

    /**
     *
     */
    public void startThread() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     *
     */
    public void notifyScheduler() {
        // Add code here
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
    }

    /**
     * Entry point for the application.
     *
     * @param args The command-line arguments that are passed when compiling the application.
     */
    public static void main(String args[]) {
        Floor floor = new Floor();
        floor.fetchFloorSubSystems();
        floor.displayAllSubSystems();
    }
}
