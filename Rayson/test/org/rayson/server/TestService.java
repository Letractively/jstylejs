package org.rayson.server;

import java.io.EOFException;

import org.rayson.annotation.Proxy;
import org.rayson.api.RpcService;
import org.rayson.api.Session;
import org.rayson.api.TestProxy;

@Proxy(TestProxy.class)
public interface TestService extends RpcService {
	public String echo(Session session, String message) throws EOFException;

	public int getInt(Session session);

	public int[] getIntArray(Session session);

	public Integer getInteger(Session session);

	public void voidMethod(Session session);
}
