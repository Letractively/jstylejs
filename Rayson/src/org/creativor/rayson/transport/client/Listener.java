/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.client;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
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
	private Selector selector;

	/**
	 * A task to accept new connection in this listener.
	 * 
	 */
	private class AcceptTask {
		private SocketChannel socketChannel;
		private SelectionKey key;
		private AtomicBoolean done;

		private ClosedChannelException exception;

		public AcceptTask(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
			done = new AtomicBoolean(false);
		}

		public void waitForDone() throws InterruptedException {
			synchronized (done) {
				while (!done.get()) {
					done.wait();
				}
			}
		}

		public void execute() throws ClosedChannelException {
			try {
				key = socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (ClosedChannelException e) {
				exception = e;
				throw e;
			} finally {
				synchronized (done) {
					done.set(true);
					done.notifyAll();
				}
			}
		}

		public SelectionKey getResult() throws ClosedChannelException {
			if (exception != null)
				throw exception;
			return key;
		}

	}

	private boolean running = true;

	private ConcurrentLinkedQueue<AcceptTask> tasks;

	Listener(ConnectionManager connectionManager) throws IOException {
		setName("Client listener");
		this.connectionManager = connectionManager;
		this.selector = Selector.open();
		this.tasks = new ConcurrentLinkedQueue<AcceptTask>();
	}

	private void doRead(SelectionKey key) {
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
	 * Accept a new connection.
	 * 
	 * @param socketChannel
	 * @param clientConnection
	 * @return selection key register into the selector of this lisenter.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	SelectionKey accept(SocketChannel socketChannel,
			TimeLimitConnection clientConnection) throws IOException,
			InterruptedException {
		SelectionKey key;
		AcceptTask task = submitTask(socketChannel, clientConnection);
		selector.wakeup();
		// wait until task is done.
		task.waitForDone();
		key = task.getResult();
		key.attach(clientConnection);
		return key;
	}

	/**
	 * Submit new accept task.
	 * 
	 * @param socketChannel
	 * @param clientConnection
	 * @return
	 */
	private AcceptTask submitTask(SocketChannel socketChannel,
			TimeLimitConnection clientConnection) {
		AcceptTask task = new AcceptTask(socketChannel);
		this.tasks.add(task);
		return task;
	}

	@Override
	public void run() {

		LOGGER.info(getName() + " starting...");
		SelectionKey key;
		Iterator<SelectionKey> iterator;

		while (running) {

			try {

				// Do accept new connections first.
				doAccept();

				selector.select();

				for (iterator = selector.selectedKeys().iterator(); iterator
						.hasNext();) {

					key = iterator.next();
					iterator.remove();
					if (key.isValid()) {
						if (key.isReadable())
							doRead(key);
						else if (key.isWritable())
							doWrite(key);
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

	/**
	 * Execute all accept connection tasks.
	 * 
	 * @throws ClosedChannelException
	 */
	private void doAccept() throws ClosedChannelException {
		if (tasks.isEmpty())
			return;
		for (Iterator iterator = tasks.iterator(); iterator.hasNext();) {
			AcceptTask task = (AcceptTask) iterator.next();
			iterator.remove();
			task.execute();
		}
	}

	private void doWrite(SelectionKey key) {
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
