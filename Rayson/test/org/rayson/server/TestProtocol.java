package org.rayson.server;

import java.io.EOFException;

import org.rayson.annotation.Proxy;
import org.rayson.api.RpcProtocol;
import org.rayson.api.Session;
import org.rayson.api.TestProxy;
import org.rayson.exception.RpcException;

@Proxy(TestProxy.class)
public interface TestProtocol extends RpcProtocol {
	public String echo(Session session, String message) throws EOFException,
			RpcException;

	public int getInt(Session session) throws RpcException;

	public int[] getIntArray(Session session) throws RpcException;

	public Integer getInteger(Session session) throws RpcException;

	public void voidMethod(Session session) throws RpcException;
}
