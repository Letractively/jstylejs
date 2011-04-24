/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.demo;

import org.creativor.rayson.server.RpcServerTest;
import org.creativor.rayson.transport.server.ServerConfig;

/**
 * 
 * @author Nick Zhang
 */
public class DemoServer {
	public static void main(String[] args) {

		int portNumber;
		try {
			portNumber = Integer.parseInt(args[0]);
		} catch (Exception e) {
			portNumber = ServerConfig.DEFAULT_PORT_NUMBER;
		}
		System.out.println("Start demo server in port " + portNumber);
		RpcServerTest.startTestServer(portNumber);

	}
}
