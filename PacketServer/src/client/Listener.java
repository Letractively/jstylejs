package client;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

class Listener extends Thread {
	private Selector selector;

	Listener(Selector selector) {
		this.setName("Client listener");
		this.selector = selector;
	}

	@Override
	public void run() {

		SelectionKey key;
		Iterator<SelectionKey> iterator;
		while (true) {
			try {

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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void write(SelectionKey key) {
		ClientConnection connection = (ClientConnection) key.attachment();
		System.out.println("got write key");
		try {
			connection.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void read(SelectionKey key) throws IOException {
		ClientConnection connection = (ClientConnection) key.attachment();
		System.out.println("got read key");
		int readCount = connection.read();
		if (readCount == -1)
			connection.close();

	}
}
