package org.rayson.transport.server.transfer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.rayson.api.TransferArgument;
import org.rayson.api.TransferSocket;

class TransferCall {

	private TransferSocket transferSocket;
	private TransferInvoker invoker;
	private TransferArgument argument;

	public TransferCall(TransferInvoker invoker, TransferArgument argument,
			TransferSocket transferSocket) {
		this.transferSocket = transferSocket;
		this.invoker = invoker;
		this.argument = argument;
	}

	/**
	 * Process this call, finally close the transfer socket.
	 * 
	 * @throws TransferCallException
	 */
	public void process() throws TransferCallException {
		try {
			this.invoker.invoke(this.argument, this.transferSocket);
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
