package org.rayson.transport.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Logger;

import org.rayson.util.Log;

class Listener extends Thread {
	private static Logger LOGGER = Log.getLogger();
	private Selector selector;
	private TransportServer server;

	public Listener(TransportServer server) throws IOException {
		setName("Server " + server.toString() + " lisenter");
		this.server = server;
		this.selector = Selector.open();
		server.getSocketChannel().register(selector, SelectionKey.OP_ACCEPT);
	}

	private void accept(SelectionKey key) {

		ServerSocketChannel channel = (ServerSocketChannel) key.channel();
		ServerConnection connection = null;

		try {

			SocketChannel clientChannel = channel.accept();
			clientChannel.configureBlocking(false);
			// register read events for the new connection.
			SelectionKey clientKey = clientChannel.register(selector,
					SelectionKey.OP_READ);
			connection = new ServerConnection(clientChannel,
					this.server.getPacketManager(), clientKey);
			clientKey.attach(connection);
			try {
				this.server.getConnectionManager().accept(connection);
			} catch (DenyServiceException e) {
				connection.denyToAccept();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOGGER.info("new connection: " + connection + " accepted.");
	}

	private void read(SelectionKey key) {
		ServerConnection connection = (ServerConnection) key.attachment();
		int readCount = -1;
		try {
			readCount = connection.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (readCount == -1) {
			try {
				connection.close();
			} catch (IOException e) {
			}
			this.server.getConnectionManager().remove(connection);
			LOGGER.info(connection.toString() + " removed!");

		}

	}

	@Override
	public void run() {
		LOGGER.info(getName() + " starting...");

		SelectionKey key;

		Iterator<SelectionKey> iterator;

		while (true) {

			try {
				selector.select();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}

			for (iterator = selector.selectedKeys().iterator(); iterator
					.hasNext();) {
				key = iterator.next();
				iterator.remove();
				if (key.isValid()) {
					if (key.isAcceptable())
						accept(key);
					else if (key.isReadable())
						read(key);
					else if (key.isWritable())
						write(key);
					else
						;
				}
			}

		}
		LOGGER.info(getName() + " stopped");

	}

	private void write(SelectionKey key) {
		ServerConnection connection = (ServerConnection) key.attachment();
		try {
			connection.write();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				connection.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.server.getConnectionManager().remove(connection);
			LOGGER.info(connection.toString() + " removed!");
		}
	}
}
