package org.rayson.api;

import org.rayson.exception.NetWorkException;

public interface TestAsyncProxy extends AsyncProxy {

	public CallFuture<String> echo(String message) throws NetWorkException;

	public CallFuture<Integer> getInt() throws NetWorkException;

	public CallFuture<int[]> getIntArray() throws NetWorkException;

	public CallFuture<Integer> getInteger() throws NetWorkException;

	public CallFuture<Void> voidMethod() throws NetWorkException;
}
