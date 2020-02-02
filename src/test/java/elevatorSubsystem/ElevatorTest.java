/**
 * 
 */
/**
 * 
 */
package elevatorSubsystem;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scheduler.Scheduler;

/**
 * Tests for the Elevator class.
 * 
 * @author osayimwense
 * @version Iteration 1 - February 1st, 2020
 */
class ElevatorTest {

    /**
     * @throws java.lang.Exception
     */
    Elevator invalidElevator;
    Elevator validElevator;

    @BeforeEach
    void setUp() throws Exception {
        invalidElevator = new Elevator(null);
        validElevator = new Elevator(new Scheduler());
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
}
