package client;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

class Listener extends Thread {
	private Selector selector;

	private AtomicBoolean registering;

	Listener(Selector selector) {
		this.setName("Client listener");
		this.selector = selector;
		registering = new AtomicBoolean(false);
	}

	@Override
	public void run() {

		SelectionKey key;
		Iterator<SelectionKey> iterator;
		while (true) {
			try {

				selector.select();
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void write(SelectionKey key) {
		ClientConnection connection = (ClientConnection) key.attachment();
		try {
			connection.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void read(SelectionKey key) throws IOException {
		ClientConnection connection = (ClientConnection) key.attachment();
		int readCount = connection.read();
		if (readCount == -1)
			connection.close();

	}

	SelectionKey register(SocketChannel socketChannel, int ops,
			ClientConnection clientConnection) throws ClosedChannelException {
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
}
