package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

import elevatorSubsystem.Elevator;
import floorSubsystem.RequestData;

/**
 * The Scheduler class in one the three subsystems of the elevator program. The
 * scheduler is responsible for scheduling requests to ensure maximum throughput
 * and minimize waiting time.
 * 
 * For Iteration 2: The scheduler receives request from the floor and passes it
 * to the elevator. It will also receive a notification when the elevator
 * arrives at its destination floor for any passed request.
 * 
 * @author John Breton, Shoaib Khan
 * @version Iteration 2 - February 15th, 2020
 */
public class Scheduler implements Runnable {

	/**
	 * Deque to store all the requests
	 */
	private Deque<RequestData> requestData;

	/**
	 * Notification request to store request from the elevator
	 */
	private RequestData notifiedRequest;
	
	private int floorAddress;
	
	
	private static final int FLOOR_PORT = 23;
	
	private static final int DATA_SIZE = 26;
	
	// Packets for sending and receiving
	private DatagramPacket sendPacket, receivePacket;
	
	// Sockets for sending and receiving
	private DatagramSocket floorSocketReceiver, elevatorSocketReceiver, sendSocket;
	
	/**
	 * Default constructor to initialize the class variables.
	 */
	public Scheduler() {
		requestData = new ArrayDeque<>();
		notifiedRequest = null;
		
		try {
			floorSocketReceiver = new DatagramSocket(FLOOR_PORT);
			sendSocket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Error: Scheduler sub system cannot be initialized.");
			System.exit(1);
		}
	}
	

//	/**
//	 * Method to set and store the requests from the elevator and floor threads.
//	 * 
//	 * @param requestData The request being sent from either the elevator or floor
//	 *                    thread
//	 * @throws InterruptedException Thrown if a thread is interrupted while
//	 *                              accessing the method
//	 */
//	public synchronized void setRequest(RequestData requestData) throws InterruptedException {
//
//		// If a request is already pending.
//		if (!this.requestData.isEmpty()) {
//			// Make the thread that is making the request to wait.
//			this.wait();
//		}
//
//		// Print out a message to notify where the request is coming is from and what
//		// the request it.
//		System.out.println(
//				"\nScheduler received information from " + Thread.currentThread().getName() + ": " + requestData);
//
//		elevator.addToQueue(requestData);
//
//		// Notify the all the other threads to start sending and receiving again.
//		notifyAll();
//	}
//
//	/**
//	 * Method to notify the scheduler
//	 * 
//	 * @param requestData The request being sent from either the elevator or floor
//	 *                    thread
//	 * @throws InterruptedException Thrown if a thread is interrupted while
//	 *                              accessing the method
//	 */
//	public synchronized void notifyScheduler(RequestData requestData) throws InterruptedException {
//		notifiedRequest = requestData;
//		System.out.println(
//				"Scheduler received information from " + Thread.currentThread().getName() + ": " + requestData);
//		notifyAll();
//	}
//
//	/**
//	 * Method to get all the notified request from the elevator
//	 * 
//	 * @return The RequestData that was passed by the Elevator once it reached its
//	 *         destination floor
//	 */
//	public synchronized RequestData getNotifiedRequest() {
//		if (notifiedRequest != null) {
//			RequestData request = notifiedRequest;
//			notifiedRequest = null;
//			return request;
//		}
//		return notifiedRequest;
//	}

	public void receivePacketFromFloor() {
		byte[] request = new byte[DATA_SIZE];
		receivePacket = new DatagramPacket(request, request.length);
		try {
			System.out.println("Scheduler: Waiting for packet");

			// Receive a packet
			floorSocketReceiver.receive(receivePacket);
			
			// Store the floor port address
			floorAddress = receivePacket.getPort();
		} catch (IOException e) {
			
			// Display an error if the packet cannot be received
			// Terminate the program
			System.out.println("Error: Scheduler cannot receive packet.");
			System.exit(1);
		}
	}
	
	/**
	 * Method to display the information of the receive packet and the contents of the data
	 * @param requestType
	 */
	private void printReceivedPacketInfo() {
		System.out.println("<- Scheduler: Received Packet");
		System.out.println("<- Sender Address: " + receivePacket.getAddress());
		System.out.println("<- Sender Port: " + receivePacket.getPort());
		System.out.print("<- Data (byte): ");
		for (byte b: receivePacket.getData()) {
			System.out.print(b);
		}
		
		String string = new String(receivePacket.getData());
		System.out.print("\n<- Data (String): " + string + "\n\n");
	}
	
	/**
	 * Thread execution routine.
	 */
	@Override
	public void run() {
		while (true) {
			this.receivePacketFromFloor();
			this.printReceivedPacketInfo();
			
		}

	}
	
	public static void main(String[] args) {
		Thread thread = new Thread(new Scheduler());
		thread.start();
	}
}
