package org.creativor.rayson.transport.server.transfer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.creativor.rayson.api.TransferArgument;
import org.creativor.rayson.api.TransferService;
import org.creativor.rayson.api.TransferSocket;

class TransferInvoker {

	private short code;

	public TransferInvoker(short code, TransferService service, Method method) {
		super();
		this.code = code;
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

	public short getCode() {
		return code;
	}

	public boolean isSupportedVersion(short clientVersion) {
		return service.isSupportedVersion(clientVersion);
	}
}