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
class ParserTest {
	Parser parser;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		parser = new Parser();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		parser = null;
		assertNull(parser);
	}

	@Test
	void test() {
		assertNotNull(parser.getRequestFromFile());
		
	}

}
