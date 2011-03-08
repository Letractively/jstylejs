package org.rayson.demo;

import java.io.EOFException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.rayson.annotation.RpcProtocols;
import org.rayson.api.ServerProtocol;
import org.rayson.api.ServiceRegistration;
import org.rayson.api.TestRpcProtocol;
import org.rayson.client.Rayson;
import org.rayson.exception.CallException;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.NetWorkException;
import org.rayson.exception.RemoteException;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.server.DemoRpcService;

public class Demo {

	public static void main(String[] args) throws UnknownHostException,
			IllegalServiceException, NetWorkException, RemoteException {
		DemoRpcService demoRpcService = new DemoRpcService();
		register(demoRpcService);
		SocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);

		Rayson.ping(serverAddress);

		System.out.println("Ping sucessfully");

		ServerProtocol serverService = Rayson.getServerService(serverAddress);
		TestRpcProtocol testRpcService = Rayson.createServiceProxy("demo",
				TestRpcProtocol.class, serverAddress);

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
			} catch (CallException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		System.exit(0);

	}

	public static void register(RpcProtocols t) {

	}
}
