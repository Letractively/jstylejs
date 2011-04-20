/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.common;

import java.io.IOException;
import org.creativor.rayson.transport.api.TimeLimitConnection;

/**
 *
 * @author Nick Zhang
 */
public abstract class PacketConnection extends TimeLimitConnection {

	private ProtocolType protocol = ProtocolType.RPC;

	protected PacketConnection() {
	}

	public abstract void addSendPacket(Packet responsePacket)
			throws IOException;

	@Override
	public final ProtocolType getProtocol() {
		return protocol;
	}

	public abstract void init() throws IOException;

	public abstract int pendingPacketCount();

}