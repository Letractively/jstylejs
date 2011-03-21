package org.rayson.transport.server.transfer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.rayson.api.TransferSocket;

class TransferCall {

	private TransferSocket transferSocket;
	private TransferInvoker invoker;

	public TransferCall(TransferInvoker invoker, TransferSocket transferSocket) {
		this.transferSocket = transferSocket;
		this.invoker = invoker;
	}

	/**
	 * Process this call, finally close the transfer socket.
	 * 
	 * @throws TransferCallException
	 */
	public void process() throws TransferCallException {
		try {
			this.invoker.invoke(this.transferSocket);
		} catch (Exception e) {
			Throwable throwable = e;
			if (e instanceof InvocationTargetException) {
				throwable = ((InvocationTargetException) e)
						.getTargetException();
			}
			throw new TransferCallException(throwable);
		} finally {
			try {
				this.transferSocket.close();
			} catch (IOException e) {
				throw new TransferCallException(e);
			}
		}
	}

}
