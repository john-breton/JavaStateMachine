package floorSubsystem;

import java.util.ArrayDeque;
import java.util.Deque;

import scheduler.Scheduler;

/**
 * 
 * 
 * @author
 * @version Iteration 1 - February 1st, 2020
 */
public class Floor implements Runnable {
    private Deque<RequestData> requestData;
    private Scheduler scheduler;

    /**
     *
     */
    public Floor(Scheduler scheduler) {
        requestData = new ArrayDeque<>();
        this.scheduler = scheduler;
    }

    /**
     *
     */
    public void fetchRequests() {
        Parser parser = new Parser();
        requestData = parser.getRequestFromFile();
    }

    /**
     *
     */
    public void displayAllRequests() {
        for (RequestData requestData : this.requestData) {
            System.out.println(requestData);
        }
    }

    /**
     *
     */
    public void notifyScheduler() {
        try {
            scheduler.setRequest(requestData.pop());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    @Override
    public void run() {
        this.fetchRequests();
        while (true) {
            this.notifyScheduler();
            try {
                scheduler.setRequest(requestData.pop());
                scheduler.getRequest();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
