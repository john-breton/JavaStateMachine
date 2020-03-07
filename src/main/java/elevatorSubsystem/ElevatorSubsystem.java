package elevatorSubsystem;

import floorSubsystem.RequestData;
import scheduler.Scheduler;
//import scheduler.Scheduler.State;
//import scheduler.Scheduler.State;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * The ElevatorSubsystem class serves as the communication intermediary between
 * the Scheduler and the Elevators within the system.
 * 
 * @author Osayimwen Justice Odia, John Breton
 * @version Iteration 3 - March 6th, 2020
 */
public class ElevatorSubsystem {
	
	/**
     * The queue used to keep track of the work for the Elevator.
     */
    private Deque<RequestData> workQueue;
    
	private static final int SCHEDULER_SEND_PORT = 60;
    
    private static final int SCHEDULER_RECEIVE_PORT = 61;

    private static final int DATA_SIZE = 26;

    // Packets for sending and receiving
    private DatagramPacket sendPacket, receivePacket;

    private DatagramSocket receiveSocket, sendSocket;
	
	private enum State {
        IDLE, MOVINGUP,MOVINGDOWN,ARRIVED;
    }
	
	private static State state;
    
    public ElevatorSubsystem() {
    	
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
			System.out.println("Error: Elevator could not send the packet.");
			System.exit(1);
		}
	}

	private void sendPacketToScheduler() {
		this.createPacket(receivePacket.getData());
		this.printPacketInfo(true);
		this.sendPacket();
	}

	private void receivePacketFromScheduler() {
		byte[] request = new byte[DATA_SIZE];
		receivePacket = new DatagramPacket(request, request.length);
		try {
			// Receive a packet
			receiveSocket.receive(receivePacket);
			this.printPacketInfo(false);
			this.addRequestToQueue();
		} catch (IOException e) {

			// Display an error if the packet cannot be received
			// Terminate the program
			System.out.println("Error: Elevator cannot receive packet.");
			System.exit(1);
		}
	}

	private void printPacketInfo(boolean sending) {
		String symbol = sending ? "->" : "<-";
		String title = sending ? "sending" : "receiving";
		DatagramPacket packetInfo = sending ? sendPacket : receivePacket;
		System.out.println(symbol + " Scheduler: " + title + " Packet");
		System.out.println(symbol + " Address: " + packetInfo.getAddress());
		System.out.println(symbol + " Port: " + packetInfo.getPort());
		System.out.print(symbol + " Data (byte): ");
		for (byte b : packetInfo.getData()) {
			System.out.print(b);
		}

		String string = new String(packetInfo.getData());
		System.out.print("\n" + symbol + " Data (String): " + string + "\n\n");
	}

	private void addRequestToQueue() {
		RequestData data = new RequestData(receivePacket.getData());
		this.workQueue.add(data);
	}

	/**
	 * Method to create a send packet
	 * 
	 * @param message
	 */
	private void createPacket(byte[] message) {
		try {

			// Initialize and create a send packet
			sendPacket = new DatagramPacket(message, message.length, InetAddress.getLocalHost(),
					SCHEDULER_RECEIVE_PORT);

		} catch (UnknownHostException e) {

			// Display an error message if the packet cannot be created.
			// Terminate the program.
			System.out.println("Error: Elevator could not create packet.");
			System.exit(1);
		}
	}
    
    /**
     * Entry point for the application.
     *
     * @param args The command-line arguments that are passed when compiling the
     *             application.
     */
    public static void main(String[] args) {
        System.out.println("---- ELEVATOR SUB SYSTEM ----- \n");
        ElevatorSubsystem subsystem = new ElevatorSubsystem();
        Elevator elevator1 = subsystem.new Elevator();
        Elevator elevator2 = subsystem.new Elevator();
        subsystem.addElevator(elevator1);
        subsystem.addElevator(elevator2);
        Thread elevatorThread1 = new Thread(subsystem.new Elevator());
        Thread elevatorThread2 = new Thread(subsystem.new Elevator());
        elevatorThread1.start();
        elevatorThread2.start();
    }
}
