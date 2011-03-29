package org.creativor.rayson.api;

import org.creativor.rayson.api.AsyncProxy;
import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.exception.NetWorkException;

public interface TestAsyncProxy extends AsyncProxy {

	public CallFuture<String> echo(String message) throws NetWorkException;

	public CallFuture<Integer> getInt() throws NetWorkException;

	public CallFuture<int[]> getIntArray() throws NetWorkException;

	public CallFuture<Integer> getInteger() throws NetWorkException;

	public CallFuture<Void> voidMethod() throws NetWorkException;
}
