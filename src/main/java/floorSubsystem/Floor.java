package floorSubsystem;

import java.util.ArrayList;
import scheduler.Scheduler;

/**
 * @author
 * @version Iteration 1 - February 1st, 2020
 */
public class Floor implements Runnable {
    private ArrayList<RequestData> requestData;
    private Scheduler scheduler;

    /**
     *
     */
    public Floor(Scheduler scheduler) {
        requestData = new ArrayList<>();
        this.scheduler = scheduler;
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
    public void startThread() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     *
     */
    public void notifyScheduler() {
    	try {
			scheduler.setRequest(requestData.get(0));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    /**
     * 
     */
    @Override
    public void run() {
    	while(true) {
    		this.notifyScheduler();
    	}
    }
}
