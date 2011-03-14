package org.rayson.server;

import java.io.EOFException;

import org.rayson.exception.RpcException;

public class DemoRpcService implements TestRemoteService {
	@Override
	public String echo(RpcSession session, String message) throws EOFException,
			RpcException {
		return message;
	}

	@Override
	public int getInt(RpcSession session) throws RpcException {
		// TODO Auto-generated method stub
		return 345;
	}

	@Override
	public int[] getIntArray(RpcSession session) throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getInteger(RpcSession session) throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void voidMethod(RpcSession session) throws RpcException {
		// TODO Auto-generated method stub

	}
}
