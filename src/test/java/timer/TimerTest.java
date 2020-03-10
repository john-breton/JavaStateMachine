package timer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author John Breton
 * @version Iteration 3 - March 7th, 2020
 */

public class TimerTest {

	Timer timer;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;

	/**
     */
	@BeforeEach
	void setUp() {
		timer = new Timer();
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
		timer = null;
		assertNull(timer);
	}

	@Test
	void testTimePassing() {
		// Ensure that the timer does not think a second has passed yet.
		assertFalse(timer.itsTime(0, 0, 1, 0));

		// Wait for 1 second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check that the timer has registered that a second has passed.
		assertTrue(timer.itsTime(0, 0, 1, 0));

		// Repeat the above procedure for 3 seconds (1 + 1 = 2 seconds total).
		assertFalse(timer.itsTime(0, 0, 2, 0));
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(timer.itsTime(0, 0, 2, 0));
	}

}
