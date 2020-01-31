package scheduler;

import java.util.ArrayDeque;

import floorSubsystem.RequestData;

/**
 * Javadoc comments go here
 * 
 * @author
 * @version Iteration 1 - February 1st, 2020
 */
public class Scheduler {

    private ArrayDeque<RequestData> requestData;

    /**
     * 
     */
    public Scheduler() {
        requestData = new ArrayDeque<>();
    }

    /**
     * 
     * @param requestData
     * @throws InterruptedException
     */
    public synchronized void setRequest(RequestData requestData) throws InterruptedException {
        if (!this.requestData.isEmpty()) {
            this.wait();
        }

        this.requestData.add(requestData);
        System.out.println(
                "Scheduler received information from " + Thread.currentThread().getName() + ": " + requestData);
        notifyAll();
    }

    /**
     * 
     * @return
     * @throws InterruptedException
     */
    public synchronized RequestData getRequest() throws InterruptedException {
        if (requestData.isEmpty()) {
            this.wait();
        }
        
        RequestData temp = requestData.pop();
        notifyAll();
        return temp;
    }

    // NEED TO MAKE THIS INTO A THREAD AS WELL
}
