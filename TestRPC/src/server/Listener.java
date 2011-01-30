package server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Listener extends Thread {
	private Selector selector;

	public Listener(ServerSocketChannel socketChannel) throws IOException {
		this.selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_ACCEPT);
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

						if (key.isAcceptable())
							accept(key);
						else if (key.isReadable())
							read(key);
						else
							;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void read(SelectionKey key) {
		ServerConnection connection = (ServerConnection) key.attachment();
		try {
			connection.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void accept(SelectionKey key) {
		ServerSocketChannel channel = (ServerSocketChannel) key.channel();
		ServerConnection connection = null;

		try {

			SocketChannel clientChannel = channel.accept();
			clientChannel.configureBlocking(false);
			connection = new ServerConnection(clientChannel);
			// register read events for the new connection.
			clientChannel.register(selector, SelectionKey.OP_READ, connection);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("new connection: " + connection + " accepted.");

	}

}
