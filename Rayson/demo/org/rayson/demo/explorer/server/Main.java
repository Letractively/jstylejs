package org.rayson.demo.explorer.server;

import java.io.IOException;

class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		ExplorerServer server = new ExplorerServer();
		server.start();
	}

}