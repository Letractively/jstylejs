package org.rayson.demo;

import java.io.EOFException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.rayson.api.IllegalServiceException;
import org.rayson.api.NetWorkException;
import org.rayson.api.RemoteException;
import org.rayson.api.ServerService;
import org.rayson.api.ServiceNotFoundException;
import org.rayson.api.ServiceRegistration;
import org.rayson.api.TestRpcService;
import org.rayson.client.Rayson;
import org.rayson.server.RpcServerTest;

public class Demo {

	public static void main(String[] args) throws UnknownHostException,
			IllegalServiceException {
		RpcServerTest.startTestServer();
		SocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);

		ServerService serverService = Rayson.createProxy(ServerService.class,
				"server", serverAddress);
		TestRpcService testRpcService = Rayson.createProxy(
				TestRpcService.class, "demo", serverAddress);

		try { // list services.

			for (ServiceRegistration registration : serverService.list()) {
				System.out.println(registration.toString());
			}
			// testRpcService.voidMethod();

			// System.out.println(testRpcService.getIntArray());

			// int returnI = Rayson.call(2);
			// testRpcService.voidMethod();
			System.out.println(testRpcService.echo("hello world"));

		} catch (EOFException e) {
			// TODO: handle exception
			e.printStackTrace();

		} catch (RemoteException e) {
			try {
				e.throwCause();
			} catch (UndeclaredThrowableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NetWorkException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ServiceNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		System.exit(0);

	}
}
