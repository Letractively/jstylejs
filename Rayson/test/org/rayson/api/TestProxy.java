package org.rayson.api;

import java.io.EOFException;

import org.rayson.exception.RpcException;

public interface TestProxy extends RpcProxy {

	public String echo(String message) throws EOFException, RpcException;

	public int getInt() throws RpcException;

	public int[] getIntArray() throws RpcException;

	public Integer getInteger() throws RpcException;

	public void voidMethod() throws RpcException;
}
