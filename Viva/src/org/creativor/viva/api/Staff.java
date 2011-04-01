package org.creativor.viva.api;

import org.creativor.rayson.exception.RpcException;

public interface Staff {
	public int getId();

	public String getIp();

	public short getPort();

	public Card getCard() throws RpcException;
}