package elevatorSubsystem;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import floorSubsystem.RequestData;
import scheduler.Scheduler;

/**
 * Tests for the Elevator class.
 * 
 * @author osayimwense, John Breton
 * @version Iteration 2 - February 15th, 2020
 */
class ElevatorTest {

    /**
     * @throws java.lang.Exception
     */
	ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	PrintStream originalOut = System.out;
    Elevator invalidElevator;
    Elevator validElevator;
    Scheduler scheduler;

    @BeforeEach
    void setUp() throws Exception {
        invalidElevator = new Elevator(null);
        scheduler = new Scheduler();
        validElevator = new Elevator(scheduler);
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
    }

    @Test
    void exceptionTesting() {

        assertThrows(NullPointerException.class, () -> invalidElevator.toString(),
                "Expected toString() to throw since scheduler is null");

        assertThrows(NullPointerException.class, () -> invalidElevator.run(),
                "Expected run() to throw since scheduler is null");

    }

    @Test
    void constructorTest() {
        assertNotNull(invalidElevator);
        assertNotNull(validElevator);
    }
    
    @Test
    void movementTest() {
    	
    	RequestData testData = new RequestData("14:05:43.000", 1, true, 7);
    	try {
			scheduler.setRequest(testData);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	// Ensure that data is being printed to the console now that the Elevator has received a request to move.
    	assertNotNull(outContent.toString());
    }
    
}
