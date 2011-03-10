package org.rayson.server;

import java.io.EOFException;

import org.rayson.annotation.Protocols;
import org.rayson.api.RpcService;
import org.rayson.api.Session;
import org.rayson.api.TestRpcProtocol;
import org.rayson.exception.RemoteException;

@Protocols({ TestRpcProtocol.class })
public interface TestRemoteService extends RpcService {
	public String echo(Session session, String message) throws EOFException,
			RemoteException;

	public int getInt(Session session) throws RemoteException;

	public int[] getIntArray(Session session) throws RemoteException;

	public Integer getInteger(Session session) throws RemoteException;

	public void voidMethod(Session session) throws RemoteException;
}
