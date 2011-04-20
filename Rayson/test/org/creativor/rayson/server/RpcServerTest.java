/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.server;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.server.RpcServer;
import org.creativor.rayson.transport.api.ServiceAlreadyExistedException;
import org.junit.Test;

public class RpcServerTest {

	public static void startTestServer() {
		try {
			RpcServer rpcServer = new RpcServer();
			rpcServer.start();

			rpcServer.registerService("demo", "Demo service",
					new TestServiceImpl());
			rpcServer.registerService(new TestTransferService());
		} catch (ServiceAlreadyExistedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testFind() {
		fail("Not yet implemented");
	}

	@Test
	public void testInvokeCall() {
		fail("Not yet implemented");
	}

	@Test
	public void testList() {
		fail("Not yet implemented");
	}

	@Test
	public void testMain() {
		fail("Not yet implemented");
	}

	@Test
	public void testRegisterService() {
		fail("Not yet implemented");
	}

	@Test
	public void testRpcServer() {
		fail("Not yet implemented");
	}

	@Test
	public void testStart() {
		fail("Not yet implemented");
	}

}
