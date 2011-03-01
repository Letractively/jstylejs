package org.rayson.server;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.rayson.exception.IllegalServiceException;

public class RpcServerTest {

	@Test
	public void testStart() {
		fail("Not yet implemented");
	}

	public static void startTestServer() {
		try {
			RpcServer rpcServer = new RpcServer(RpcServer.PORT_NUMBER);
			rpcServer.start();

			rpcServer.registerService("demo", "Demo service",
					new DemoRpcService());
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
	public void testMain() {
		fail("Not yet implemented");
	}

	@Test
	public void testRpcServer() {
		fail("Not yet implemented");
	}

	@Test
	public void testRegisterService() {
		fail("Not yet implemented");
	}

	@Test
	public void testList() {
		fail("Not yet implemented");
	}

	@Test
	public void testInvokeCall() {
		fail("Not yet implemented");
	}

	@Test
	public void testFind() {
		fail("Not yet implemented");
	}

}
