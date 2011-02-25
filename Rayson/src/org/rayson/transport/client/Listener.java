package org.rayson.transport.client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

class Listener extends Thread {
	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private AtomicBoolean registering;
	private Selector selector;

	private ConnectionManager connectionManager;

	Listener(ConnectionManager connectionManager) throws IOException {
		setName("Client listener");
		this.connectionManager = connectionManager;
		this.selector = Selector.open();
		registering = new AtomicBoolean(false);
	}

	private void read(SelectionKey key) {
		ClientConnection connection = (ClientConnection) key.attachment();
		int readCount = -1;
		try {
			readCount = connection.read();
		} catch (IOException e) {
		}
		if (readCount == -1)
			try {
				connection.close();
			} catch (IOException e) {

			}
		connectionManager.remove(connection);
		LOGGER.info(connection.toString() + " removed!");

	}

	SelectionKey register(SocketChannel socketChannel, int ops,
			ClientConnection clientConnection) throws IOException {
		SelectionKey key;
		synchronized (registering) {
			registering.set(true);
			selector.wakeup();
			key = socketChannel.register(selector, ops, clientConnection);
			registering.set(false);
			registering.notifyAll();
		}
		return key;
	}

	@Override
	public void run() {
		LOGGER.info(getName() + " starting...");
		SelectionKey key;
		Iterator<SelectionKey> iterator;
		while (true) {

			try {
				selector.select();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			synchronized (registering) {
				while (registering.get()) {
					try {
						registering.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			for (iterator = selector.selectedKeys().iterator(); iterator
					.hasNext();) {

				key = iterator.next();
				iterator.remove();
				if (key.isValid()) {
					if (key.isReadable())
						read(key);
					else if (key.isWritable())
						write(key);
					else
						;
				}
			}

		}
	}

	private void write(SelectionKey key) {
		ClientConnection connection = (ClientConnection) key.attachment();
		try {
			connection.write();
		} catch (IOException e) {
			try {
				connection.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			connectionManager.remove(connection);
			LOGGER.info(connection.toString() + " removed!");

		}
	}
}
