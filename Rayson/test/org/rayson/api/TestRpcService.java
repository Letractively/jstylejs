package org.rayson.api;

import java.io.EOFException;

public interface TestRpcService extends RpcService {

	public String echo(String message) throws EOFException;

	public void voidMethod();

	public int getInt();

	public int[] getIntArray();

	public Integer getInteger();
}
