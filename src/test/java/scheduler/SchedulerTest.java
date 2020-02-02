/**
 * 
 */
package scheduler;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		assertNotNull(scheduler.getRequest());
		RequestData data = new RequestData();
		scheduler.setRequest(data);
		assertSame(data,scheduler.getRequest());
}

}
