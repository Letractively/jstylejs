package org.rayson.server;

import java.io.EOFException;

import org.rayson.annotation.Protocols;
import org.rayson.api.RpcService;
import org.rayson.api.TestRpcProtocol;
import org.rayson.exception.RpcException;
import org.rayson.server.api.RpcSession;

@Protocols({ TestRpcProtocol.class })
public interface TestRemoteService extends RpcService{
	public String echo(RpcSession session, String message) throws EOFException,
			RpcException;

	public int getInt(RpcSession session) throws RpcException;

	public int[] getIntArray(RpcSession session) throws RpcException;

	public Integer getInteger(RpcSession session) throws RpcException;

	public void voidMethod(RpcSession session) throws RpcException;
}
