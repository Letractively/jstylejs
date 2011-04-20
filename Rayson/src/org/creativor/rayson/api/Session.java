/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.api;

import java.net.InetSocketAddress;

/**
 * One {@link RpcProxy} associated with one {@link Session}.
 * 
 * @author hp
 */
public interface Session {

	public long getId();

	public long getCreationTime();

	public long getInvocationTime();

	/**
	 * Get version of Rayson client.
	 */
	public byte getVersion();

	/**
	 * The version of the {@link RpcProxy} .
	 */
	public short getClientVersion();

	public String getServiceName();

	public InetSocketAddress getPeerAddress();
}
