package org.rayson.server;

import java.io.EOFException;

import org.rayson.api.Session;
import org.rayson.exception.RpcException;

public class DemoRpcService implements TestRemoteService {
	@Override
	public String echo(Session session, String message) throws EOFException,
			RpcException {
		return message;
	}

	@Override
	public int getInt(Session session) throws RpcException {
		// TODO Auto-generated method stub
		return 345;
	}

	@Override
	public int[] getIntArray(Session session) throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getInteger(Session session) throws RpcException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void voidMethod(Session session) throws RpcException {
		// TODO Auto-generated method stub

	}
}
