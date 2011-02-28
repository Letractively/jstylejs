package org.rayson.server;

import java.io.EOFException;

import org.rayson.api.TestRpcService;
import org.rayson.common.RpcException;

class DemoRpcService implements TestRpcService {

	@Override
	public String echo(String message) throws EOFException {
		return "asdfsdf";
		// throw new NullPointerException();
	}

	@Override
	public void voidMethod() throws RpcException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getInt() throws RpcException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getIntArray() throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getInteger() throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

}
