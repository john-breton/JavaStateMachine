package elevatorSubsystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Elevator class.
 * 
 * @author osayimwense, John Breton, Shoaib Khan
 * @version Iteration 3 - March 7th, 2020
 */
class ElevatorTest {

	/**
	 * @throws java.lang.Exception
	 */
	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	PrintStream originalOut = System.out;
	ElevatorSubsystem elevatorSubSystem;

	@BeforeEach
	void setUp() {
		elevatorSubSystem = new ElevatorSubsystem();
		Thread elevatorSubSystemThread = new Thread(elevatorSubSystem);
		elevatorSubSystemThread.start();
	}

	@BeforeEach
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}

	@AfterEach
	public void restoreStreams() {
		System.setOut(originalOut);
	}

	/**
	 */
	@AfterEach
	void tearDown() {
	}


	@Test
	void movementTest() {
		byte[] bytes = "Status".getBytes();
		byte[] data = new byte[100];
		
		try {
			DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), 60);
			DatagramPacket receivePacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 61);
			DatagramSocket sendSocket = new DatagramSocket();
			sendSocket.send(sendPacket);
			sendSocket.receive(receivePacket);
			sendSocket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		// Ensure that data is being printed to the console now that the Elevator has
		// received a request to move.
		assertNotNull(outContent.toString());
	}

}
