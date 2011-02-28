package org.rayson.server;

import java.io.EOFException;

import org.rayson.api.TestRpcService;

class DemoRpcService implements TestRpcService {

	@Override
	public String echo(String message) throws EOFException {
		return message;
	}

	@Override
	public void voidMethod() {
		System.out.println("voidMethod");
	}

	@Override
	public int getInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getIntArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getInteger() {
		// TODO Auto-generated method stub
		return null;
	}

}
