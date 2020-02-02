/**
 * 
 */
package floorSubsystem;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author osayimwense
 *
 */
class RequestDataTest {
	
	RequestData requestData;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		requestData = new RequestData();
		}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		requestData = null;
		assertNull(requestData);
				
	}

	@Test
	void test() {
		requestData.setCurrentFloor(3);
		assertSame(3, requestData.getCurrentFloor());
		requestData.setDestinationFloor(1);
		assertSame(1, requestData.getDestinationFloor());
		requestData.setIsGoingUp(true);
		assertSame(true, requestData.getIsGoingUp());
		requestData.setTime("14:05:15.0");
		assertSame("14:05:15.0", requestData.getTime());

	}

}
