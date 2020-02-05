/**
 * 
 */
package floorSubsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scheduler.Scheduler;

/**
 * @author osayimwense
 *
 */
class FloorTest {
	
	Floor validFloor;
	Floor invalidFloor;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		validFloor = new Floor(new Scheduler());
		invalidFloor = new Floor(null);

	}
	
	@BeforeEach
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}

	@AfterEach
	public void restoreStreams() {
	    System.setOut(originalOut);
	}
	
	@Test
	void exceptionTesting() {
		
		assertThrows(NullPointerException.class, () -> invalidFloor.run(),
				"Expected run() to throw since scheduler is null");
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		validFloor = null;
		invalidFloor = null;
		assertNull(invalidFloor);
		assertNull(validFloor);

	}

	@Test
	void test() {
			assertNotSame(validFloor.fetchRequests(),0);
			assertNotSame(invalidFloor.fetchRequests(),0);
			validFloor.displayAllRequests();
		    assertNotNull(outContent.toString());

	}
	

}
