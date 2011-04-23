/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.creativor.rayson.transport.api.TimeLimitConnection;
import org.creativor.rayson.util.Log;

/**
 * 
 * @author Nick Zhang
 */
class Listener extends Thread {
	private static Logger LOGGER = Log.getLogger();

	private ConnectionManager connectionManager;
	private volatile boolean registering = false;
	private Lock selectorLock;
	private Selector selector;
	private Condition registerCondition;

	private boolean running = true;

	Listener(ConnectionManager connectionManager) throws IOException {
		setName("Client listener");
		this.connectionManager = connectionManager;
		this.selector = Selector.open();
		selectorLock = new ReentrantLock();
		registerCondition = selectorLock.newCondition();
	}

	private void read(SelectionKey key) {
		RpcConnection connection = (RpcConnection) key.attachment();
		int readCount = -1;
		try {
			readCount = connection.read();
		} catch (IOException e) {
		}
		if (readCount == -1) {
			try {
				connection.close();
			} catch (IOException e) {

			}
			connectionManager.remove(connection);
			LOGGER.info(connection.toString() + " removed!");
		}

	}

	/**
	 * Register a new selection key when initialize a client connection.
	 * 
	 * @param socketChannel
	 * @param ops
	 * @param clientConnection
	 * @return
	 * @throws IOException
	 */
	SelectionKey register(SocketChannel socketChannel, int ops,
			TimeLimitConnection clientConnection) throws IOException {
		SelectionKey key;
		selectorLock.lock();
		try {
			registering = true;
			selector.wakeup();
			key = socketChannel.register(selector, ops, clientConnection);
			registering = false;
			registerCondition.signalAll();
			return key;
		} finally {
			selectorLock.unlock();
		}
	}

	@Override
	public void run() {

		LOGGER.info(getName() + " starting...");
		SelectionKey key;
		Iterator<SelectionKey> iterator;

		while (running) {

			try {
				selectorLock.lock();
				try {
					while (registering)
						registerCondition.await();
				} finally {
					selectorLock.unlock();
				}

				selector.select();

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
			} catch (Throwable e) {
				// protect this thread not to quit.
				LOGGER.log(Level.SEVERE, "Server listener got error", e);
			}

		}
		LOGGER.info(getName() + " stopped");

	}

	private void write(SelectionKey key) {
		RpcConnection connection = (RpcConnection) key.attachment();
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
