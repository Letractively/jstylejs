package org.creativor.viva.api;

import org.creativor.rayson.api.Portable;
import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.exception.RpcException;

public interface VivaProxy extends RpcProxy {

	public void join(int hashCode) throws RpcException;

	public Card getCard(int hashCode) throws RpcException;

	public void putCard(int hashCode, Portable value) throws RpcException;

}
