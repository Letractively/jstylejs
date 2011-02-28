package org.rayson.api;

import java.io.EOFException;

public interface TestRpcService extends RpcService {

	public String echo(String message) throws EOFException, RemoteException;

	public void voidMethod() throws RemoteException;

	public int getInt() throws RemoteException;

	public int[] getIntArray() throws RemoteException;

	public Integer getInteger() throws RemoteException;
}
