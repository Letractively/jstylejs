package org.rayson.api;

public interface TestRpcService extends RpcService {

	public String echo(String message) throws RpcException;

	public void voidMethod() throws RpcException;

	public int getInt() throws RpcException;

	public int[] getIntArray() throws RpcException;

	public Integer getInteger() throws RpcException;
}
