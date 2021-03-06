package floorSubsystem;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author osayimwense
 * @version Iteration 3 - March 7th, 2020
 */
class RequestDataTest {
	
	RequestData requestData;

	/**
	 */
	@BeforeEach
	void setUp() {
		requestData = new RequestData("14:05:15.0",3,true,1);
		}

	/**
	 */
	@AfterEach
	void tearDown() {
		requestData = null;
		assertNull(requestData);
				
	}
	@Test
    void constructorTest() {
        assertNotNull(requestData);
    }
	
	@Test
	void test() {
		assertSame(3, requestData.getCurrentFloor());
		assertSame(1, requestData.getDestinationFloor());
		assertSame(true, requestData.getIsGoingUp());
		assertSame("14:05:15.0", requestData.getTime());

	}

}
