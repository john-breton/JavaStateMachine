package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
	
	private static final int FLOOR_SEND_PORT = 23;
	
	private static final int ELEVATOR_RECEIVE_PORT = 60;
	
	private static final int ELEVATOR_SEND_PORT = 61;
	
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
			floorSocketReceiver = new DatagramSocket(FLOOR_SEND_PORT);
			elevatorSocketReceiver = new DatagramSocket(ELEVATOR_SEND_PORT);
			sendSocket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Error: Scheduler sub system cannot be initialized.");
			System.exit(1);
		}
	}

	private void receivePacket(boolean fromFloor) {
		byte[] request = new byte[DATA_SIZE];
		receivePacket = new DatagramPacket(request, request.length);
		try {
			// Receive a packet
			if (fromFloor) {
				floorSocketReceiver.receive(receivePacket);
			}
			
			else {
				elevatorSocketReceiver.receive(receivePacket);
			}
			
		} catch (IOException e) {
			
			// Display an error if the packet cannot be received
			// Terminate the program
			System.out.println("Error: Scheduler cannot receive packet.");
			System.exit(1);
		}
	}
	
	/**
	 * Method to create a send packet
	 * @param message
	 */
	private void createPacket(byte[] message) {
		try {
			
			// Initialize and create a send packet
			sendPacket = new DatagramPacket(message, message.length, InetAddress.getLocalHost(), ELEVATOR_RECEIVE_PORT);
			
		} catch (UnknownHostException e) {
			
			// Display an error message if the packet cannot be created.
			// Terminate the program.
			System.out.println("Error: Scheduler could not create packet.");
			System.exit(1);
		}
	}
	
	/**
	 * Method to send the packet to the scheduler
	 */
	private void sendPacket() {
		try {
			
			// Send the packet
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			
			// Display an error message if the packet cannot be sent.
			// Terminate the program.
			System.out.println("Error: Scheduler could not send the packet.");
			System.exit(1);
		}
	}
	
	private void sendPacketToElevator() {
		this.createPacket(receivePacket.getData());
		this.printPacketInfo(true);
		this.sendPacket();
	}
	
	private void printPacketInfo(boolean sending) {
		String symbol = sending ? "->" : "<-";
		String title = sending ? "sending" : "receiving";
		DatagramPacket packetInfo = sending ? sendPacket : receivePacket;
		System.out.println(symbol + " Scheduler: " + title + " Packet");
		System.out.println(symbol + " Address: " + packetInfo.getAddress());
		System.out.println(symbol + " Port: " + packetInfo.getPort());
		System.out.print(symbol + " Data (byte): ");
		for (byte b: packetInfo.getData()) {
			System.out.print(b);
		}
		
		String string = new String(packetInfo.getData());
		System.out.print("\n" + symbol + " Data (String): " + string + "\n\n");
	}
	
	/**
	 * Thread execution routine.
	 */
	@Override
	public void run() {
		while (true) {
			this.receivePacket(true);
			this.printPacketInfo(false);
			this.sendPacketToElevator();
			this.receivePacket(false);
			this.printPacketInfo(false);
			System.out.println("Elevator moved to the floor");
			System.out.println("---------------------------------------------------------------------");
		}
	}
	
	public static void main(String[] args) {
		System.out.println("---- SCHEDULER SUB SYSTEM ----- \n");
		Thread thread = new Thread(new Scheduler());
		thread.start();
	}
}
