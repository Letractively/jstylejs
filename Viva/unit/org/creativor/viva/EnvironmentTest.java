/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.logging.Level;
import org.creativor.rayson.util.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Nick Zhang
 */
public class EnvironmentTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetEnvironment() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetHomeDir() {
		System.out.println(Environment.getEnvironment().getHomeDir());
	}

	@Test
	public void testGetLogDir() {
		System.out.println(Environment.getEnvironment().getLogDir());
	}

	@Test
	public void testSetLogFile() throws SecurityException, IOException {
		Environment.getEnvironment().setupLogFile("testLog");
		Log.getLogger().log(Level.INFO, "fine message1");
		Log.getLogger().log(Level.INFO, "fine message2");
	}

}
