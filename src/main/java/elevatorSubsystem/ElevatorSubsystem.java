package elevatorSubsystem;

import floorSubsystem.RequestData;
import scheduler.Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;


public class ElevatorSubsystem {
    
    public ElevatorSubsystem() {
        
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
         */
        private static final double TIME_PER_FLOOR = 1;
    
        private static final int SCHEDULER_SEND_PORT = 60;
    
        private static final int SCHEDULER_RECEIVE_PORT = 61;
    
        private static final int DATA_SIZE = 26;
    
        // Packets for sending and receiving
        private DatagramPacket sendPacket, receivePacket;
    
        private DatagramSocket receiveSocket, sendSocket;
    
        /**
         * Construct a new Elevator.
         */
        public Elevator() {
            workQueue = new ArrayDeque<RequestData>();
    
            try {
                receiveSocket = new DatagramSocket(SCHEDULER_SEND_PORT);
                sendSocket = new DatagramSocket();
            } catch (SocketException e) {
                System.out.println("Error: Scheduler sub system cannot be initialized.");
                System.exit(1);
            }
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
                int count = currentFloor;
                while (count <= destinationFloor) {
                    this.move(count);
                    count++;
                }
                return true;
            }
    
            // If the elevator is moving down
            else if (currentFloor > destinationFloor) {
                int count = currentFloor;
                while (count >= destinationFloor) {
                    this.move(count);
                    count--;
                }
                return true;
            }
    
            // If the destination floor is the same as the current floor
            else {
                System.out.println("Elevator is already at the floor");
                return false;
            }
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
                    this.sendPacketToScheduler();
                }
            }
        }
    
        /**
         * Thread execution routine.
         */
        @Override
        public void run() {
            while (true) {
                this.receivePacketFromScheduler();
                this.doWork();
                System.out.println("---------------------------------------------------------------------");
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
        Thread elevator1 = new Thread(test.new Elevator());
        Thread elevator2 = new Thread(test.new Elevator());
        elevator1.start();
        elevator2.start();
    }
}
