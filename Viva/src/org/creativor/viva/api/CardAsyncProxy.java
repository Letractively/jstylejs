package org.creativor.viva.api;

import org.creativor.rayson.api.AsyncProxy;
import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.api.Portable;
import org.creativor.rayson.exception.NetWorkException;

public interface CardAsyncProxy extends AsyncProxy {

	public CallFuture<Portable> get(int hashCode) throws NetWorkException;

	public CallFuture<Void> put(int hashCode, Portable value)
			throws NetWorkException;

}
