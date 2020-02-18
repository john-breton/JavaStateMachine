/**
 * 
 */
package scheduler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import elevatorSubsystem.Elevator;
import floorSubsystem.Floor;
import floorSubsystem.RequestData;

/**
 * @author osayimwense, John Breton
 * @version Iteration 2 - February 15th, 2020
 */
class SchedulerTest {

	Scheduler scheduler;
	Elevator elevator;
	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	PrintStream originalOut = System.out;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		elevator = new Elevator();
		scheduler = new Scheduler(elevator);
		elevator.setScheduler(scheduler);
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
		elevator = null;
		assertNull(scheduler);
		assertNull(elevator);
	}

	@Test
	void testSetGetData() throws InterruptedException {
		RequestData data = new RequestData();
		scheduler.setRequest(data);
		// Make sure that the scheduler prints information to the console
		// which indicates setting was successful.
		assertNotNull(outContent.toString());
	}

	@Test
	void testNotification() throws InterruptedException {
		// There is no notification so this should be null.
		assertNull(scheduler.getNotifiedRequest());
		RequestData data = new RequestData();
		scheduler.notifyScheduler(data);
		// Ensure that the notification was received and the scheduler printed to the
		// console.
		assertNotNull(outContent.toString());
		// Ensure that the request is still present in the scheduler.
		assertNotNull(scheduler.getNotifiedRequest());
	}
}
