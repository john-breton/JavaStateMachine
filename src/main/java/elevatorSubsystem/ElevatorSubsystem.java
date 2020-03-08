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

//import elevatorSubsystem.ElevatorTemp.State;


public class ElevatorSubsystem {

    /**
     * The queue used to keep track of the work for the Elevator.
     */
    private Deque<RequestData> workQueue;

    private static final int SCHEDULER_SEND_PORT = 60;
    
    private static final int SCHEDULER_STATUS_REPLY_PORT = 62;


    private static final int SCHEDULER_RECEIVE_PORT = 61;

    private static final int DATA_SIZE = 26;
    
    private ArrayList<Elevator> elevators;

    // Packets for sending and receiving
    private DatagramPacket sendPacket, receivePacket,statusPacket;

    private DatagramSocket receiveSocket, sendSocket;
	
	private enum State {
        IDLE, MOVINGUP,MOVINGDOWN,ARRIVED;

    }

    private static State state;

    public ElevatorSubsystem() {
    	elevators = new ArrayList<Elevator>();
    	Elevator ev1 =this.new Elevator();
    	Elevator ev2 =this.new Elevator();
    	Thread elevator1 = new Thread(ev1);
        Thread elevator2 = new Thread(ev2);
        elevators.add(ev1);
    	elevators.add(ev2);
        elevator1.start();
        elevator2.start();
    	
    	
    	try {
            receiveSocket = new DatagramSocket(SCHEDULER_SEND_PORT);
            sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error: Scheduler sub system cannot be initialized.");
            System.exit(1);
        }
    	
    	while(true) {
            receivePacketFromScheduler();
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

	/**
	 * Method to send the packet to the scheduler
	 */
	private void sendStatusPacket() {
		try {

			// Send the packet
			sendSocket.send(statusPacket);
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
			if((receivePacket.getData()[0] == (byte) 0)) {
				 statusPacket = getStatusPacket();
//				 System.out.println(statusPacket);
				 String string = new String(statusPacket.getData());
					System.out.print("\n Data (String): " + string + "\n\n");
			        sendStatusPacket();
					printStatusPacketInfo(true);

				
			}else {
				int elevatorNum = receivePacket.getData()[0];
				elevators.get(elevatorNum-1).setAlert(false);
				int workQueueIndex = receivePacket.getData()[2];
				byte[] data = Arrays.copyOfRange(receivePacket.getData(), 4, receivePacket.getData().length);
				elevators.get(elevatorNum-1).setWorkQueue(data, workQueueIndex);
				
				while(!elevators.get(elevatorNum-1).getAlert()) {}
				
				sendPacketToScheduler();
				this.printPacketInfo(true);
				
			}
			
//			this.addRequestToQueue();
		} catch (IOException e) {

			// Display an error if the packet cannot be received
			// Terminate the program
			System.out.println("Error: Elevator cannot receive packet.");
			System.exit(1);
		}
	}

	/**
	 * Method to make status packet
	 */
	private DatagramPacket getStatusPacket() {
				
		try {
			ArrayList<Byte> d = new ArrayList<Byte>();
			//Add all the states
			for(Elevator ev: elevators) {
				for (byte stateByte : ev.getState().name().getBytes()) {
					d.add(stateByte);
				}
				d.add((byte) '|');
				if(ev.getCurrentRequest()!=null){
					d.add((byte) ev.getCurrentFloor());
					d.add((byte) '|');
					d.add((byte) ev.getDestinationFloor());	
				}
				d.add((byte) '-');
			}
			
			byte[] s = new byte[d.size()];

			for (int i = 0; i < d.size(); i++) {
				s[i] = (byte) d.get(i);
			}
            // Initialize and create a send packet
             DatagramPacket ans = new DatagramPacket(s, s.length, InetAddress.getLocalHost(),
            		 SCHEDULER_STATUS_REPLY_PORT);
            return ans;

        } catch (UnknownHostException e) {

            // Display an error message if the packet cannot be created.
            // Terminate the program.
            System.out.println("Error: Elevator could not create packet.");
            System.exit(1);
        }
		return null ;
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
	
	
	private void printStatusPacketInfo(boolean sending) {
		String symbol = sending ? "->" : "<-";
		String title = sending ? "sending" : "receiving";
		DatagramPacket packetInfo = statusPacket;
		System.out.println(symbol + " Scheduler: " + title + " Packet");
		System.out.println(symbol +  " Address: " + packetInfo.getAddress());
		System.out.println(symbol + " Port: " + packetInfo.getPort());
		System.out.print(symbol + " Data (byte): ");
		for (byte b : packetInfo.getData()) {
			System.out.print(b);
		}

		String string = new String(packetInfo.getData());
		System.out.print("\n"+symbol + "Data (String): " + string + "\n\n");
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
	 * The Elevator class represents nodes within the elevatorSubsystem package.
	 * Each Elevator serves as one node, continually polling the scheduler for
	 * pending work. For Iteration 1, once an Elevator receives work from the
	 * scheduler, it takes the information and immediately tries to sends it back to
	 * the scheduler.
	 *
	 * @author John Breton, Shoaib Khan
	 * @version Iteration 3 - March 6th, 2020
	 */
	public class Elevator implements Runnable {
		
		
		

	    /**
	     * The queue used to keep track of the work for the Elevator.
	     */
	    private Deque<RequestData> workQueue;

	    /**
	     * The current request the Elevator is handling.
	     */
	    private RequestData currentRequest;

	    /**
	     * Experimental value for iteration 2. Value taken from iteration 0 calculations
	     * Time it takes to move 1 floor at full acceleration
	     * 
	     */

	    private static final double TIME_PER_FLOOR = 1;

	    private static final int SCHEDULER_SEND_PORT = 60;
	    
	    private static final int SCHEDULER_RECEIVE_PORT = 61;

	    private static final int DATA_SIZE = 26;

	    // Packets for sending and receiving
	    private DatagramPacket sendPacket, receivePacket;

	    private DatagramSocket receiveSocket, sendSocket;
	    
	    private boolean alert;
		
	    
	    

	    

		/**
	     * Construct a new Elevator.
	     */
	    public Elevator() {
	        state = State.IDLE;
	        workQueue = new ArrayDeque<RequestData>();

//	        try {
//	            receiveSocket = new DatagramSocket(SCHEDULER_SEND_PORT);
//	            sendSocket = new DatagramSocket();
//	        } catch (SocketException e) {
//	            System.out.println("Error: Scheduler sub system cannot be initialized.");
//	            System.exit(1);
//	        }
	    }

	    /**
	     * Method to add a request to the elevator queue.
	     * 
	     * @param requestData
	     */
	    public void addToQueue(RequestData requestData) {
	        workQueue.add(requestData);
	    }
	    
	    /**
	     * Advances the elevator to the next state.
	     */
	    private void goToNextState() {
	    	
	        if (getState()== State.ARRIVED) 
	            state = State.IDLE;
	        else if (getState() == State.IDLE) {
	        	state = State.MOVINGUP;
	        }
	        else if (getState() == State.MOVINGUP) {
	        	state = State.MOVINGDOWN;
	        }
	        else if (getState() == State.MOVINGDOWN) {
	        	state = State.ARRIVED;
	        }
	        
	            
	    }
	    
	    /**
	     * Get the current state of the elevator.
	     * 
	     * @return The current state of the elevator.
	     */
	    private State getState() {
	        return state;
	    }

	    /**
	     * Method to move the elevator to a particular floor
	     * 
	     * @param floor
	     */
	    private void move(int floor) {
	        try {
	            System.out.println("Elevator: Floor " + floor);

	            // Wait while the elevator is moving in between floors
	            Thread.sleep((long) (TIME_PER_FLOOR * 1000));
	        } catch (InterruptedException e) {
	            System.out.println("Error: Elevator cannot not move");
	        }
	    }

	    /**
	     * Method to move floors
	     * 
	     * @return
	     */
	    private boolean moveFloors() {

	        // Get the current and the destination floors
	        int currentFloor = currentRequest.getCurrentFloor();
	        int destinationFloor = currentRequest.getDestinationFloor();

	        // If the elevator is moving up
	        if (currentFloor < destinationFloor) {
	        	state = State.MOVINGUP;
	            int count = currentFloor;
	            while (count <= destinationFloor) {
	                this.move(count);
	                count++;
	            }
	            state = State.ARRIVED;
	            return true;
	        }

	        // If the elevator is moving down
	        else if (currentFloor > destinationFloor) {
	        	state = State.MOVINGDOWN;
	            int count = currentFloor;
	            while (count >= destinationFloor) {
	                this.move(count);
	                count--;
	            }
	            state = State.ARRIVED;
	            return true;
	        }

	        // If the destination floor is the same as the current floor
	        else {
	            System.out.println("Elevator is already at the floor");
	            state = State.IDLE;
	            return false;
	        }
	    }
	    
	    /**
	     * Method to get current floor
	     * 
	     * @return
	     */
	    private int getCurrentFloor() {
	    
	    	return currentRequest.getCurrentFloor();
	    }
	    
	    /**
	     * Method to get current request
	     * 
	     * @return
	     */
	    private RequestData getCurrentRequest() {
	    
	    	return currentRequest;
	    }

	    /**
	     * Method to get destination floor
	     * 
	     * @return
	     */
	    private int getDestinationFloor() {
	    
	    	return currentRequest.getDestinationFloor();
	    }
	    
	    /**
	     * Method to set work queue
	     * 
	     * @return
	     */
	    private void setWorkQueue(byte[] request, int index) {
	    	RequestData data = new RequestData(request);
	        this.workQueue.add(data);
	    
	    }
	    
	    
	    public boolean getAlert() {
			return alert;
		}

		public void setAlert(boolean alert) {
			this.alert = alert;
		}

	    
//	    private void receivePacketFromScheduler() {
//	        byte[] request = new byte[DATA_SIZE];
//	        receivePacket = new DatagramPacket(request, request.length);
//	        try {
//	            // Receive a packet
//	            receiveSocket.receive(receivePacket);
//	            this.printPacketInfo(false);
//	            this.addRequestToQueue();
//	        } catch (IOException e) {
//
//	            // Display an error if the packet cannot be received
//	            // Terminate the program
//	            System.out.println("Error: Elevator cannot receive packet.");
//	            System.exit(1);
//	        }
//	    }

//	    private void printPacketInfo(boolean sending) {
//	        String symbol = sending ? "->" : "<-";
//	        String title = sending ? "sending" : "receiving";
//	        DatagramPacket packetInfo = sending ? sendPacket : receivePacket;
//	        System.out.println(symbol + " Scheduler: " + title + " Packet");
//	        System.out.println(symbol + " Address: " + packetInfo.getAddress());
//	        System.out.println(symbol + " Port: " + packetInfo.getPort());
//	        System.out.print(symbol + " Data (byte): ");
//	        for (byte b : packetInfo.getData()) {
//	            System.out.print(b);
//	        }
//
//	        String string = new String(packetInfo.getData());
//	        System.out.print("\n" + symbol + " Data (String): " + string + "\n\n");
//	    }

	    private void addRequestToQueue() {
	        RequestData data = new RequestData(receivePacket.getData());
	        this.workQueue.add(data);
	    }

	    /**
	     * Method to create a send packet
	     * 
	     * @param message
	     */
//	    private void createPacket(byte[] message) {
//	        try {
//
//	            // Initialize and create a send packet
//	            sendPacket = new DatagramPacket(message, message.length, InetAddress.getLocalHost(),
//	                    SCHEDULER_RECEIVE_PORT);
//
//	        } catch (UnknownHostException e) {
//
//	            // Display an error message if the packet cannot be created.
//	            // Terminate the program.
//	            System.out.println("Error: Elevator could not create packet.");
//	            System.exit(1);
//	        }
//	    }

	    /**
	     * Method to send the packet to the scheduler
	     */
//	    private void sendPacket() {
//	        try {
//
//	            // Send the packet
//	            sendSocket.send(sendPacket);
//	        } catch (IOException e) {
//
//	            // Display an error message if the packet cannot be sent.
//	            // Terminate the program.
//	            System.out.println("Error: Elevator could not send the packet.");
//	            System.exit(1);
//	        }
//	    }

//	    private void sendPacketToScheduler() {
//	        this.createPacket(receivePacket.getData());
//	        this.printPacketInfo(true);
//	        this.sendPacket();
//	    }

	    /**
	     * Return the current movement of the Elevator, as a String.
	     * 
	     * @return The string representation of the movement the Elevator is currently
	     *         completing.
	     */
	    @Override
	    public String toString() {
	        return "At time " + currentRequest.getTime() + ", the elevator is moving "
	                + (currentRequest.getIsGoingUp() ? "up" : "down") + " from floor " + currentRequest.getCurrentFloor()
	                + " to floor " + currentRequest.getDestinationFloor();
	    }

	    private void doWork() {
	        if (!workQueue.isEmpty()) {
	            currentRequest = workQueue.pop();
	            System.out.println("Elevator received information from Scheduler: " + currentRequest.toString());
	            System.out.println(toString());
	            if (this.moveFloors()) {
	                System.out.println();
	                alert = true;
	            }
	        }
	    }

	    /**
	     * Thread execution routine.
	     */
	    @Override
	    public void run() {
	        while (true) {
	            this.doWork();
//	            System.out.println("---------------------------------------------------------------------");
	        }
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
        ElevatorSubsystem test = new ElevatorSubsystem();
        test.receivePacketFromScheduler();
//        Thread elevator1 = new Thread(test.new Elevator());
//        Thread elevator2 = new Thread(test.new Elevator());
//        elevator1.start();
//        elevator2.start();
    }
}
