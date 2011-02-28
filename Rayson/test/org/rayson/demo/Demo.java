package org.rayson.demo;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.rayson.api.IllegalServiceException;
import org.rayson.api.ServiceNotFoundException;
import org.rayson.api.TestRpcService;
import org.rayson.client.Rayson;
import org.rayson.server.RpcServerTest;

public class Demo {

	public static void main(String[] args) throws UnknownHostException,
			IllegalServiceException {
		RpcServerTest.startTestServer();
		SocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);
		TestRpcService testRpcService = Rayson.createProxy(
				TestRpcService.class, "demo", serverAddress);
		try {
			int returnI = Rayson.call(Integer.MIN_VALUE);
			testRpcService.echo("hello world");

			System.out.println(Rayson.call(testRpcService.echo("hello world")));

		} catch (EOFException e) {
			// TODO: handle exception
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(0);

	}
}
