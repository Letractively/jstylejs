package org.rayson.transport.server.transfer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.rayson.api.TransferArgument;
import org.rayson.api.TransferService;
import org.rayson.api.TransferSocket;

class TransferInvoker {

	private short transfer;

	public TransferInvoker(short transfer, TransferService service,
			Method method) {
		super();
		this.transfer = transfer;
		this.service = service;
		this.method = method;
	}

	private TransferService service;
	private Method method;

	public void invoke(TransferArgument argument, TransferSocket transferSocket)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		this.method.invoke(service, argument, transferSocket);
	}

	public short getTransfer() {
		return transfer;
	}
}