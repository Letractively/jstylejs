package org.rayson.util;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.rayson.api.IllegalServiceException;
import org.rayson.api.TestRpcService;

public class ServiceParserTest {

	@Test
	public void testGetProtocols() {
		fail("Not yet implemented");
	}

	@Test
	public void testVerifyService() {

		try {
			ServiceParser.verifyService(TestRpcService.class);
		} catch (IllegalServiceException e) {
			fail(e.getMessage());
		}
	}

}
