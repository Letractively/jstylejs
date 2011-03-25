package org.rayson.util;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.rayson.api.TestProxy;
import org.rayson.exception.IllegalServiceException;

public class ServiceParserTest {

	@Test
	public void testGetProtocols() {
		fail("Not yet implemented");
	}

	@Test
	public void testVerifyService() {

		try {
			ServiceVerifier.verifyService(TestProxy.class);
		} catch (IllegalServiceException e) {
			fail(e.getMessage());
		}
	}

}
