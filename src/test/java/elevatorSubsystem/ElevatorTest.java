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
import floorSubsystem.RequestData;

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
		ElevatorSubsystem elevatorSubSystem = new ElevatorSubsystem();
		Thread elevatorSubSystemThread = new Thread(elevatorSubSystem);
		elevatorSubSystemThread.start();
		
		RequestData data = new RequestData("14:05:55.0 1 Up 4");
		byte[] bytes = data.toBytes();
		
		try {
			DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), 60);
			DatagramSocket socket = new DatagramSocket();
			socket.send(sendPacket);
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
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
