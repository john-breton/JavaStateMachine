package floorSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import timer.Timer;

/**
 * The Floor class is one of the three subsystems of the elevator program. The
 * floor class is responsible for fetching the requests from a document. Then in
 * a polling loop, it sends the fetched request to the scheduler. It also
 * continuously listens for any messages from the scheduler.
 * 
 * For Iteration 3, the floor will only send a request if the current amount of time matches
 * that of the request's time-stamp. It will wait for confirmation from the scheduler that the
 * elevator actually reached the destination floor.
 * 
 * @author Shoaib Khan, John Breton
 * @version Iteration 3 - March 6th, 2020
 */
public class Floor implements Runnable {

	/**
	 * ArrayDeque to store all the requests
	 */
	private Deque<RequestData> requestData;

	/**
	 * Used to calculate time that has passed (Simulates real life time).
	 */
	private Timer timer;

	/**
	 * The time the first request was sent (the start of the simulation).
	 */
	private String startTime;

	/**
	 * Time variables
	 */
	private int startHour, startMinute, startSecond, startMillisecond;
	
	/**
	 * Port to send packets to the scheduler
	 */
	private static final int PORT = 23;
	
	/**
	 * Variables to get track of total requests and received requests.
	 */
	private int totalRequests, dataReceived;
	
	/**
	 * Send packet
	 */
	private DatagramPacket sendPacket;
	
	/**
	 * Socket to send packet to the scheduler
	 */
	private DatagramSocket socket;


	/**
	 * Default constructor to initialize the class variables
	 *
	 */
	public Floor() {
		requestData = new ArrayDeque<>();
		timer = new Timer();
		dataReceived = 0;
		
		// Fetch all the requests
		totalRequests = fetchRequests();
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Error: Floor sub system could not be initialized.");
			System.exit(1);
		}
	}
	
	/**
	 * Method to fetch the request from the file. Internally calls the parser class
	 * to fetch the requests.
	 * 
	 * @return The number of requests that were parsed from the file, as an int.
	 */
	public int fetchRequests() {
		Parser parser = new Parser();
		requestData = parser.getRequestFromFile();
		startTime = Objects.requireNonNull(requestData.peek()).getTime();
		return requestData.size();
	}

	/**
	 * Method to display all the requests in the queue.
	 */
	public void displayAllRequests() {
		for (RequestData currData : this.requestData)
			System.out.println(currData);
	}

	/**
	 * Initialize variables to keep track of the time the first request was sent,
	 * for timing purposes.
	 */
	private void convertStartTime() {
		String[] temp = startTime.split("[:.]");
		startHour = Integer.parseInt(temp[0]);
		startMinute = Integer.parseInt(temp[1]);
		startSecond = Integer.parseInt(temp[2]);
		startMillisecond = Integer.parseInt(temp[3]);
	}
	
	/**
	 * Method to create a send packet
	 * @param message
	 */
	private void createPacket(byte[] message) {
		try {
			
			// Initialize and create a send packet
			sendPacket = new DatagramPacket(message, message.length, InetAddress.getLocalHost(), PORT);
			
		} catch (UnknownHostException e) {
			
			// Display an error message if the packet cannot be created.
			// Terminate the program.
			System.out.println("Error: Floor sub system could not create packet.");
			System.exit(1);
		}
	}
	
	/**
	 * Method to print the sending packing information
	 */
	private void printSendingPacketInfo() {
		System.out.println("-> Floor subsystem sending packet");
		System.out.println("-> Host Address: " + sendPacket.getAddress());
		System.out.println("-> Destination Port: " + sendPacket.getPort());
		System.out.print("-> Data (byte): ");
		for (byte b: sendPacket.getData()) {
			System.out.print(b);
		}
		
		String string = new String(sendPacket.getData());
		System.out.print("\n-> Data (String): " + string + "\n\n");
	}
	
	/**
	 * Method to send the packet to the scheduler
	 */
	private void sendPacket() {
		try {
			
			// Send the packet
			socket.send(sendPacket);
		} catch (IOException e) {
			
			// Display an error message if the packet cannot be sent.
			// Terminate the program.
			System.out.println("Error: Floor could not send the packet.");
			System.exit(1);
		}
	}
	
	/**
	 * Method to create, print and send a packet to the scheduler.
	 */
	private void sendRequestPacket() {
		if (!requestData.isEmpty()) {
			String[] time = requestData.peek().getTime().split("[:.]");
			// Wait until the correct amount of time has passed before sending the next
			// request (busy waiting loop).
			while (!timer.itsTime(Integer.parseInt(time[0]) - startHour,
					Integer.parseInt(time[1]) - startMinute, Integer.parseInt(time[2]) - startSecond,
					Integer.parseInt(time[3]) - startMillisecond));
			
			// Send request to the scheduler because it is now time to do so.
			RequestData request = requestData.pop();
			this.createPacket(request.toBytes());
			this.printSendingPacketInfo();
			this.sendPacket();
		}
	}
	

	/**
	 * Thread execution routine.
	 */
	@Override
	public void run() {
		System.out.println("Simulation started at time: " + startTime + "\n");

		// Keep track of the time the simulation is beginning at for timing purposes.
		convertStartTime();

		// In a continuous polling loop, try sending and receiving packets to/from the
		// scheduler till the data received is less than the total fetched requests
		
		// TODO: NEED TO CHANGE THE CONDITION LATER 
		// AS THE FLOOR WILL NOT RECEIVE REQUESTS ANYMORE
		while (dataReceived < totalRequests) {
			// Keep sending and receiving packets to/from the scheduler.
			this.sendRequestPacket();
			dataReceived++;
		}
	}
	
	/**
     * Entry point for the application.
     *
     * @param args The command-line arguments that are passed when compiling the
     *             application.
     */
	public static void main(String[] args) {
		System.out.println("---- FLOOR SUB SYSTEM ----- \n");
        Thread floorThread = new Thread(new Floor());
        floorThread.start();
	}
}