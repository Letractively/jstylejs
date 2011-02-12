package server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import common.PacketManager;

public class Listener extends Thread {
	private Selector selector;
	private PacketManager packetManager;

	public Listener(ServerSocketChannel socketChannel,
			PacketManager packetManager) throws IOException {
		this.packetManager = packetManager;
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

	private void write(SelectionKey key) throws IOException {
		ServerConnection connection = (ServerConnection) key.attachment();
		System.out.println("got write key");
		try {
			connection.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void read(SelectionKey key) throws IOException {
		ServerConnection connection = (ServerConnection) key.attachment();
		System.out.println("got read key");
		int readCount = connection.read();
		if (readCount == -1)
			connection.close();

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
			connection = new ServerConnection(clientChannel, packetManager,
					clientKey);
			clientKey.attach(connection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("new connection: " + connection + " accepted.");

	}

}
