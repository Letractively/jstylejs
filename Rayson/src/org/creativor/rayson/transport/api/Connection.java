/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.api;

import java.io.IOException;
import org.creativor.rayson.transport.common.ProtocolType;

/**
 *
 * @author Nick Zhang
 */
public interface Connection {
	public void close() throws IOException;

	public long getId();

	public ProtocolType getProtocol();

	public byte getVersion();

	public int read() throws IOException;

	public void write() throws IOException;

}
