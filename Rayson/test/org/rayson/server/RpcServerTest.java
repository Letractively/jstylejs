package org.rayson.server;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.rayson.exception.IllegalServiceException;
import org.rayson.transport.api.ServiceAlreadyExistedException;
import org.rayson.transport.server.TransportServer;

public class RpcServerTest {

	public static void startTestServer() {
		try {
			RpcServer rpcServer = new RpcServer(TransportServer.PORT_NUMBER);
			rpcServer.start();

			rpcServer.registerService("demo", "Demo service",
					new TestServiceImpl());
			rpcServer.registerService(new TestActivityService());
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
