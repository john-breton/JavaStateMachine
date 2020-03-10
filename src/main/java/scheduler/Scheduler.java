package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * The Scheduler class in one the three subsystems of the elevator program. The
 * scheduler is responsible for scheduling requests to ensure maximum throughput
 * and minimize waiting time.
 * 
 * For Iteration 3: The scheduler receives request from the FloorSubsystem and
 * requests the states of all elevators from the ElevatorSubsystem. Based on the
 * information returned, the scheduler will send information to the
 * ElevatorSubsystem that can be used to insert the .
 * 
 * @author John Breton, Shoaib Khan
 * @version Iteration 3 - March 6th, 2020
 */
public class Scheduler implements Runnable {

    /**
     * An enumeration representing the possible states of the Scheduler.
     * 
     * @author John Breton
     * @version Iteration 3 - March 6th, 2020
     */
    private enum State {
        IDLE, SCHEDULING
    }

    /**
     * Constants used for UDP communication between the 3 programs.
     */
    private static final int FLOOR_SEND_PORT = 23;
    private static final int ELEVATOR_RECEIVE_PORT = 60;
    private static final int ELEVATOR_SEND_PORT = 61;
    private static final int ELEVATOR_REPLY_PORT = 62;
    private static final int DATA_SIZE = 26;

    /**
     * Deque to store all the requests.
     */
    private Deque<DatagramPacket> workQueue;

    /**
     * Used to capture the state of the Scheduler.
     */
    private static State state;

    /**
     * Packets used for sending and receiving data via UDP communication.
     */
    private DatagramPacket sendPacket;
    private DatagramPacket receiveFloorPacket;
    private DatagramPacket receiveElevatorPacket;

    /**
     * Sockets used for sending and receiving data via UDP communication.
     */
    private DatagramSocket floorSocketReceiver, elevatorSocketReceiver, elevatorSocketReplier, sendSocket;

    /**
     * Default constructor to initialize the class variables.
     */
    public Scheduler() {
        workQueue = new ArrayDeque<>();
        state = State.IDLE;
        try {
            floorSocketReceiver = new DatagramSocket(FLOOR_SEND_PORT);
            elevatorSocketReceiver = new DatagramSocket(ELEVATOR_SEND_PORT);
            elevatorSocketReplier = new DatagramSocket(ELEVATOR_REPLY_PORT);
            sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error: SchedulerSubSystem cannot be initialized.");
            System.exit(1);
        }
    }

    /**
     * Advances the scheduler to the next state.
     */
    private void goToNextState() {
        if (getState().ordinal() == 1)
            state = State.IDLE;
        else
            state = State.SCHEDULING;
        System.out.println("State has been updated to: " + getState());
    }

    /**
     * Get the current state of the scheduler.
     * 
     * @return The current state of the scheduler.
     */
    private State getState() {
        return state;
    }

    /**
     * Receive a DatagramPacket from either the ElevatorSubsystem or the
     * FloorSubsystem.
     * 
     * @param fromFloor True if the DatagramPacket is originating from the
     *                  FloorSubsystem, false otherwise.
     */
    private void receivePacket(boolean fromFloor) {
        byte[] request = new byte[DATA_SIZE];
        try {
            // Receive a packet
            if (fromFloor) {
                receiveFloorPacket = new DatagramPacket(request, request.length);
                floorSocketReceiver.receive(receiveFloorPacket);
            }

            else {
                receiveElevatorPacket = new DatagramPacket(request, request.length);
                elevatorSocketReceiver.receive(receiveElevatorPacket);
            }

        } catch (IOException e) {

            // Display an error if the packet cannot be received
            // Terminate the program
            System.out.println("Error: Scheduler cannot receive packet.");
            System.exit(1);
        }
    }

    /**
     * Routine to create a DatagramPacket that will be sent.
     * 
     * @param message The byte[] data the DatagramPacket will contain.
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
     * Routine to send a DatagramPacket to the ElevatorSubsystem. This
     * DatagramPacket will contain information that the ElevatorSubsystem will use
     * to decide which Elevator should receive the packet.
     */
    private void sendPacketToElevator() {
        System.out.println("-> Sending elevator number");
        printPacketInfo(true, 3);
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

    /**
     * Print the information contained within a particular DatagramPacket.
     * 
     * @param sending   True if we are sending the DatagramPacket, false if we
     *                  received the DatagramPacket.
     * @param fromWhere 1 if the DatagramPacket originated from the
     *                  ElevatorSubsystem, 2 if the DatagramPacket originated from
     *                  the FloorSubsystem, 3 if the DatagramPacket is being sent
     *                  (sending must also be true).
     */
    private void printPacketInfo(boolean sending, int fromWhere) {
        String symbol = sending ? "->" : "<-";
        String title = sending ? "sending" : "receiving";
        DatagramPacket packetInfo = null;
        switch (fromWhere) {
        case 1:
            packetInfo = receiveElevatorPacket;
            break;
        case 2:
            packetInfo = receiveFloorPacket;
            break;
        case 3:
            packetInfo = sendPacket;
        }
        System.out.println(symbol + " Scheduler: " + title + " Packet");
        System.out.println(symbol + " Address: " + packetInfo.getAddress());
        System.out.println(symbol + " Port: " + packetInfo.getPort());
        System.out.print(symbol + " Data (byte): ");
        for (byte b : packetInfo.getData())
            System.out.print(b);

        System.out.print("\n" + symbol + " Data (String): " + new String(packetInfo.getData()) + "\n\n");
    }

    /**
     * Add a request to the work queue for the scheduler.
     * 
     * @param work The request to be added.
     */
    private synchronized void addToWorkQueue(DatagramPacket work) {
        workQueue.add(work);
        notifyAll();
    }

    /**
     * Check to see if there are any requests that must be scheduled.
     * 
     * @return The next request to be scheduled, as a DatagramPacket.
     */
    private synchronized DatagramPacket checkWork() {
        if (workQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return workQueue.pop();
    }

    /**
     * Sends a request for status to the ElevatorSubsystem.
     * 
     * @return A DatagramPacket containing the information for all Elevators.
     */
    private DatagramPacket sendStatusRequest() {
        byte[] request = "Status".getBytes();
        System.out.println("-> Sending a request for Status to the ElevatorSubsystem\n");
        createPacket(request);
        try {

            this.printPacketInfo(true, 3);
            // Send the packet
            sendSocket.send(sendPacket);
            // Wait for a reply
            byte[] longMessage = new byte[1000];
            DatagramPacket receiveElevatorInfo = new DatagramPacket(longMessage, longMessage.length, InetAddress.getLocalHost(),
                    ELEVATOR_REPLY_PORT);
            elevatorSocketReplier.receive(receiveElevatorInfo);
            return receiveElevatorInfo;
        } catch (IOException e) {
            // Display an error message if the packet cannot be sent.
            // Terminate the program.
            System.out.println("Error: Scheduler could not send the packet.");
            System.exit(1);
        }
        return null;

    }

    /**
     * Schedule the request by determining the best elevator to send the request to.
     * 
     * @param work         The current request that is being scheduled.
     * @param elevatorInfo The statuses of all the elevators.
     * @return A DatagramPacket that contains the request, along with the
     *         information as to which Elevator the request will be added to, and if
     *         it should do at the front of the back of the workQueue.
     */
    private void schedule(DatagramPacket work, DatagramPacket elevatorInfo) {
        // Progress the state of the Scheduler to indicate that we are currently
        // scheduling a request.
        goToNextState();

        ArrayList<Integer> elevatorScores = new ArrayList<>();

        String nextReq = new String(work.getData());
        String[] requestInfo = new String(work.getData()).split(" ");
        String[] elevatorStatuses = new String(elevatorInfo.getData()).split("-");
        int numElevators = elevatorStatuses.length - 1;

        boolean requestDirection = requestInfo[2].equals("Up");
        int startFloor = Integer.parseInt(requestInfo[1]);
        int destinationFloor = Integer.parseInt(requestInfo[3]);

        int numIdle = 0;

        for (String s : elevatorStatuses) {
            String[] temp = s.split("\\|");
            if (temp[0].trim().equals("IDLE"))
                numIdle++;
        }

        if (numIdle == numElevators) {
            int i = 0;
            for (String s : elevatorStatuses) {
                if (i < numElevators) {
                    String[] temp = s.split("\\|");
                    elevatorScores.add(i, Math.abs(startFloor - Integer.parseInt(temp[1])));
                    i++;
                }
            }
            int min = elevatorScores.indexOf(Collections.min(elevatorScores));
            String newData = min + "|0|" + nextReq;

            createPacket(newData.getBytes());
        } else {
            int i = 0;
            int location = 1;
            for (String s : elevatorStatuses) {
                if (i < numElevators) {
                    String[] temp = s.split("\\|");
                    if (requestDirection && temp[0].equals("MOVINGUP")
                            && (destinationFloor < Integer.parseInt(temp[2]))) {
                        elevatorScores.add(i, 1);
                        location = 0;
                    } else if (requestDirection && temp[0].equals("MOVINGDOWN")
                            && (destinationFloor > Integer.parseInt(temp[2]))) {
                        elevatorScores.add(i, 1);
                        location = 0;
                    } else {
                        elevatorScores.add(i, Math.abs(startFloor - Integer.parseInt(temp[2])));
                        location = 0;
                    }
                    i++;
                }
            }
            int min = elevatorScores.indexOf(Collections.min(elevatorScores));
            String newData = min + "|" + location + "|" + nextReq;
            createPacket(newData.getBytes());
        }

        // We are done scheduling, so the Scheduler state should indicate that it is no
        // longer scheduling.
        goToNextState();
    }

    /**
     * Thread execution routine.
     */
    @Override
    public void run() {
        if (Thread.currentThread().getName().equals("F2E"))
            // Main routine to receive request information from the FloorSubsystem.
            while (true) {
                receivePacket(true);
                printPacketInfo(false, 2);
                addToWorkQueue(receiveFloorPacket);
            }
        else if (Thread.currentThread().getName().equals("E2S"))
            while (true) {
                // Main routine to receive confirmation from
                receivePacket(false);
                printPacketInfo(false, 1);
                System.out.println("---------------------------------------------------------------------");
            }
        else
            while (true) {
                DatagramPacket work = checkWork();
                // If we get here, we have work we can do!
                DatagramPacket elevatorInfo = this.sendStatusRequest();
                schedule(work, elevatorInfo);
                sendPacketToElevator();
            }
    }

    /**
     * Entry point for the application.
     *
     * @param args The command-line arguments that are passed when compiling the
     *             application.
     */
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        System.out.println("---- SCHEDULER SUB SYSTEM ----- \n");
        Thread elevatorToScheduler = new Thread(scheduler);
        Thread floorToElevator = new Thread(scheduler);
        Thread workThread = new Thread(scheduler);
        floorToElevator.setName("F2E");
        elevatorToScheduler.setName("E2S");
        workThread.setName("Worker");
        floorToElevator.start();
        elevatorToScheduler.start();
        workThread.start();
    }
}