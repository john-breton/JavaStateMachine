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

/**
 * @author osayimwense. Shoaib Khan
 * @version Iteration 3 - March 7th, 2020
 */
class FloorTest {
	
	private Floor floor;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private static final int expectedRequests = 5;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		floor = new Floor();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
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
