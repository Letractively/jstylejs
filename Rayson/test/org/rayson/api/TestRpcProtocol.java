package org.rayson.api;

import java.io.EOFException;

import org.rayson.exception.RemoteException;

public interface TestRpcProtocol extends RpcProtocol {

	public String echo(String message) throws EOFException, RemoteException;

	public int getInt() throws RemoteException;

	public int[] getIntArray() throws RemoteException;

	public Integer getInteger() throws RemoteException;

	public void voidMethod() throws RemoteException;
}
