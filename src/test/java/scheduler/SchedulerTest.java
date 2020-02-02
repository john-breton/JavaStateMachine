/**
 * 
 */
package scheduler;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevatorSubsystem.Elevator;
import floorSubsystem.Floor;
import floorSubsystem.RequestData;


/**
 * @author osayimwense
 *
 */
class SchedulerTest {
	
	Scheduler scheduler;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		scheduler = new Scheduler();
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
	void test() throws InterruptedException {
		Thread floor = new Thread(new Floor(scheduler));
		Thread elevator = new Thread(new Elevator(scheduler));
		floor.start();
		elevator.start();
		assertNotNull(scheduler.getRequest());
		RequestData data = new RequestData();
		scheduler.setRequest(data);
		assertSame(data,scheduler.getRequest());
}

}
