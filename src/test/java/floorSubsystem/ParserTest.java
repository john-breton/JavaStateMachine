package floorSubsystem;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author osayimwense
 * @version Iteration 3 - March 7th, 2020
 */
class ParserTest {
	Parser parser;
	/**
	 */
	@BeforeEach
	void setUp() {
		parser = new Parser();
	}

	/**
	 */
	@AfterEach
	void tearDown() {
		parser = null;
		assertNull(parser);
	}

	@Test
	void test() {
		assertNotNull(parser.getRequestFromFile());
		
	}

}
