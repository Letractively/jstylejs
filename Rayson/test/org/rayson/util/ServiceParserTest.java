package org.rayson.util;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.rayson.api.TestRpcProtocol;
import org.rayson.exception.IllegalServiceException;

public class ServiceParserTest {

	@Test
	public void testGetProtocols() {
		fail("Not yet implemented");
	}

	@Test
	public void testVerifyService() {

		try {
			ServiceParser.verifyService(TestRpcProtocol.class);
		} catch (IllegalServiceException e) {
			fail(e.getMessage());
		}
	}

}
