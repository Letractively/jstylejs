package org.rayson.api;

import java.io.EOFException;

import org.rayson.common.RpcException;

public interface TestRpcService extends RpcService {

	public String echo(String message) throws EOFException;

	public void voidMethod() throws RpcException;

	public int getInt() throws RpcException;

	public int[] getIntArray() throws RpcException;

	public Integer getInteger() throws RpcException;
}
