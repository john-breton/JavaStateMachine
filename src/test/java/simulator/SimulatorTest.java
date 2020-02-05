/**
 * 
 */
package simulator;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import elevatorSubsystem.Elevator;
import floorSubsystem.Floor;
import scheduler.Scheduler;

/**
 * @author osayimwense
 *
 */
class SimulatorTest {

	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	PrintStream originalOut = System.out;
	PrintStream originalErr = System.err;
	Scheduler scheduler;
	Thread floorThread;
	Thread schedulerThread;
	Thread elevatorThread;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		scheduler = new Scheduler();
		floorThread = new Thread(new Floor(scheduler), "Floor");
		schedulerThread = new Thread(scheduler, "Scheduler");
		elevatorThread = new Thread(new Elevator(scheduler), "Elevator");
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
		floorThread = null;
		schedulerThread = null;
		elevatorThread = null;
		assertNull(scheduler);
		assertNull(floorThread);
		assertNull(schedulerThread);
		assertNull(elevatorThread);

	}

	@Test
	void test() {
		
		assertNotNull(outContent.toString());

	}

}
