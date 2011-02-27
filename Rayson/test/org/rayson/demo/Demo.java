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
import org.rayson.client.RpcClient;
import org.rayson.server.RpcServerTest;

public class Demo {

	public static void main(String[] args) throws UnknownHostException,
			IllegalServiceException {
		RpcServerTest.startTestServer();
		SocketAddress serverAddress = new InetSocketAddress(
				InetAddress.getLocalHost(), 4465);
		TestRpcService testRpcService = RpcClient.getInstance().createProxy(
				TestRpcService.class, "demo", serverAddress);

		try {
			// RpcClient.getInstance().call(null);
			try {
				testRpcService.echo("hello world");
			} catch (Throwable e) {
				e.printStackTrace();
			}
			System.out.println(RpcClient.getInstance().call(
					testRpcService.echo("hello world")));
		} catch (UndeclaredThrowableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EOFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
