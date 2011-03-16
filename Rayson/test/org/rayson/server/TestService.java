package org.rayson.server;

import java.io.EOFException;

import org.rayson.annotation.Proxy;
import org.rayson.api.RpcService;
import org.rayson.api.Session;
import org.rayson.api.TestProxy;
import org.rayson.exception.RpcException;

@Proxy(TestProxy.class)
public interface TestService extends RpcService {
	public String echo(Session session, String message) throws EOFException,
			RpcException;

	public int getInt(Session session) throws RpcException;

	public int[] getIntArray(Session session) throws RpcException;

	public Integer getInteger(Session session) throws RpcException;

	public void voidMethod(Session session) throws RpcException;
}
