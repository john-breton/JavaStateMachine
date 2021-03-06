package floorSubsystem;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author osayimwense. Shoaib Khan
 * @version Iteration 3 - March 7th, 2020
 */
class FloorTest {
	
	private Floor floor;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	// --Commented out by Inspection (2020-03-11 10:42 a.m.):private final PrintStream originalOut = System.out;
	private static final int expectedRequests = 6;
	
	/**
	 */
	@BeforeEach
	void setUp() {
		floor = new Floor();
	}
	
	/**
	 */
	@AfterEach
	void tearDown() {
		floor = null;
		assertNull(floor);
	}

	@Test
	void testFetchingData() {
		int test = floor.fetchRequests();
		System.out.println(test);
		assertEquals(floor.fetchRequests() , expectedRequests);
	}
	
	@Test
	void sendData() {
		Thread elevatorThread = new Thread(floor);
		elevatorThread.start();
		// Ensure that data is being printed to the console now that the floor has
		// sent a request.
		assertNotNull(outContent.toString());
	}
}
