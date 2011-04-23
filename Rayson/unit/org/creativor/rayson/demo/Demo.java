/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.demo;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.api.ServerProxy;
import org.creativor.rayson.api.ServiceRegistration;
import org.creativor.rayson.api.TestAsyncProxy;
import org.creativor.rayson.api.TestProxy;
import org.creativor.rayson.api.TestTransferArgument;
import org.creativor.rayson.api.TransferSocket;
import org.creativor.rayson.client.Rayson;
import org.creativor.rayson.exception.CallExecutionException;
import org.creativor.rayson.exception.ReadInvocationException;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.NetWorkException;
import org.creativor.rayson.exception.RpcException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.exception.UnsupportedVersionException;
import org.creativor.rayson.transport.server.ServerConfig;

/**
 * 
 * @author Nick Zhang
 */
public class Demo {

	public static void main(String[] args) throws IllegalServiceException,
			NetWorkException, RpcException, IOException,
			ServiceNotFoundException, InterruptedException,
			UnsupportedVersionException, CallExecutionException {

		InetSocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), ServerConfig.DEFAULT_PORT_NUMBER);

		Rayson.ping(serverAddress);

		System.out.println("Ping sucessfully");

		ServerProxy serverService = Rayson.getServerProxy(serverAddress);
		TestProxy testRpcService = Rayson.createProxy("demo", TestProxy.class,
				serverAddress);

		System.out.println("Service porxy session information:"
				+ testRpcService.getSession().toString());

		try { // list services.

			for (ServiceRegistration registration : serverService.list()) {
				System.out.println(registration.toString());
			}
			// testRpcService.voidMethod();

			// System.out.println(testRpcService.getIntArray());

			// int returnI = Rayson.call(2);
			// testRpcService.voidMethod();
			System.out.println(testRpcService.echo("hello world"));
			System.out.println("Service porxy session information:"
					+ testRpcService.getSession().toString());

		} catch (EOFException e) {
			// TODO: handle exception
			e.printStackTrace();

		} catch (RpcException e) {
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
			} catch (ReadInvocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedVersionException e1) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Test transfer socekt

		TransferSocket transferSocket = Rayson.openTransferSocket(
				serverAddress, new TestTransferArgument("testPath"));

		transferSocket.getDataOutput().writeInt(1456);

		// close transfer socket.
		transferSocket.close();

		// test asynchronous call.
		TestAsyncProxy asyncProxy = Rayson.createAsyncProxy("demo",
				TestAsyncProxy.class, serverAddress);
		CallFuture<int[]> ddd = asyncProxy.getIntArray();
		int[] intarry = ddd.get();
		CallFuture<String> echoFuture;
		for (int i = 0; i < 3; i++) {
			try {
				echoFuture = asyncProxy.echo("Async call echo message");
				System.out.println("Async call echo:" + echoFuture.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.exit(0);

	}

	public static void register(Proxy t) {

	}
}
