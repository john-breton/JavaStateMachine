package scheduler;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import floorSubsystem.RequestData;

/**
 * @author osayimwense, John Breton, Shoaib Khan
 * @version Iteration 3 - March 7th, 2020
 */
class SchedulerTest {

	Scheduler scheduler;
	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	PrintStream originalOut = System.out;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		scheduler = new Scheduler();
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
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		scheduler = null;
		assertNull(scheduler);
	}

	@Test
	void testSchedulerReceive() {
		Thread floorToScheduler = new Thread(scheduler);
		floorToScheduler.setName("F2S");
		floorToScheduler.start();
		
		Thread elvatorToScheduler = new Thread(scheduler);
		elvatorToScheduler.setName("E2S");
		elvatorToScheduler.start();
		
		RequestData data = new RequestData("14:05:55.0 1 Up 4");
		byte[] bytes = data.toBytes();
		
		try {
			DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), 23);
			DatagramSocket socket = new DatagramSocket();
			socket.send(sendPacket);
			socket.close();
			System.out.println(scheduler.toString());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertNotNull(outContent.toString());
	}
}
