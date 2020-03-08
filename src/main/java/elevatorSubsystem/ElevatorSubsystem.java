package elevatorSubsystem;

import floorSubsystem.RequestData;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class ElevatorSubsystem implements Runnable {

    private static final int SCHEDULER_SEND_PORT = 60;

    private static final int SCHEDULER_RECEIVE_PORT = 61;
    private static final int SCHEDULER_RECEIVE_REPLY_PORT = 62;

    private static final int DATA_SIZE = 26;
    
    private ArrayList<Elevator> elevators;

    // Packets for sending and receiving
    private DatagramPacket sendPacket, receivePacket;

    private DatagramSocket receiveSocket, sendSocket;
	
	private enum State {
        IDLE, MOVINGUP,MOVINGDOWN,ARRIVED;
    }

    private static State state;

    public ElevatorSubsystem() {
    	elevators = new ArrayList<Elevator>();
    	Elevator elevator1 = new Elevator();
    	Elevator elevator2 = new Elevator();
    	Thread elevatorThread1 = new Thread(elevator1);
        Thread elevatorThread2 = new Thread(elevator2);
        elevatorThread1.setName("1");
        elevatorThread2.setName("2");
        elevators.add(elevator1);
    	elevators.add(elevator2);
    	elevatorThread1.start();
    	elevatorThread2.start();
    	try {
            receiveSocket = new DatagramSocket(SCHEDULER_SEND_PORT);
            sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error: Elevator sub system cannot be initialized.");
            System.exit(1);
        }
    }
    
    /**
     * Method to create a send packet
     * 
     * @param message
     */
    private void createPacket(byte[] message, boolean status) {
        try {

            // Initialize and create a send packet
            sendPacket = new DatagramPacket(message, message.length, InetAddress.getLocalHost(),
                    status ? SCHEDULER_RECEIVE_REPLY_PORT: SCHEDULER_RECEIVE_PORT);

        } catch (UnknownHostException e) {

            // Display an error message if the packet cannot be created.
            // Terminate the program.
            System.out.println("Error: Elevator could not create packet.");
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
			System.out.println("Error: Elevator could not send the packet.");
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
    
    private boolean isStatusRequest(byte[] data) {
    	return new String(data).contains("Status");
    }
    
    private void parseInfoFromScheduler(byte[] data) {
    	String string = new String(data);
    	String[] temp = string.split("\\|");
    	int elvatorNum = Integer.parseInt(temp[0]);
    	int index = Integer.parseInt(temp[1]);
    	RequestData request = new RequestData(temp[2]);
    	
    	System.out.println("-> Sending request to the Elevator " + (elvatorNum + 1) + "\n");
    	elevators.get(elvatorNum).addToQueue(request, index);
    }

	private void receivePacketFromScheduler() {
		byte[] request = new byte[DATA_SIZE];
		receivePacket = new DatagramPacket(request, request.length);
		try {
			// Receive a packet
			receiveSocket.receive(receivePacket);
			this.printPacketInfo(false);
			
			if (this.isStatusRequest(receivePacket.getData())) {
				
				String elevatorStatues = ""; 
				for(Elevator e: elevators) {
					elevatorStatues += e.getStatus() + "-";
				}
				
				byte[] statuesByte = elevatorStatues.getBytes();
				this.createPacket(statuesByte, true);
				this.printPacketInfo(true);
				this.sendPacket();
			}
			
			else {	
				this.parseInfoFromScheduler(receivePacket.getData());
			}
		} catch (IOException e) {

			// Display an error if the packet cannot be received
			// Terminate the program
			System.out.println("Error: Elevator cannot receive packet.");
			System.exit(1);
		}
	}

	@Override
	public void run() {
		while(true) {
			this.receivePacketFromScheduler();
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
        ElevatorSubsystem elevatorSubSystem = new ElevatorSubsystem();
        Thread elevatorSubSystemThread = new Thread(elevatorSubSystem);
        elevatorSubSystemThread.start();
        
    }
}
