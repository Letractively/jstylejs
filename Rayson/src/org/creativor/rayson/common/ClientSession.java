/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.creativor.rayson.api.Portable;
import org.creativor.rayson.api.Session;

/**
 *
 * @author Nick Zhang
 */
public class ClientSession implements Portable, Session {

	private byte version;
	private long id;
	private long creationTime;
	private long lastInvocationTime;
	private String serviceName;

	private short proxyVersion;
	private InetSocketAddress peerAddress;

	/**
	 * Get next global unique session id.
	 * 
	 * @return
	 */
	public static long getNextUID() {
		return System.currentTimeMillis();
	}

	public ClientSession() {

	}

	public ClientSession(byte version, short proxyVersion, long sessionId,
			String serviceName, long creationTime, InetSocketAddress peerAddress) {
		this.version = version;
		this.proxyVersion = proxyVersion;
		this.id = sessionId;
		this.serviceName = serviceName;
		this.creationTime = creationTime;
		this.peerAddress = peerAddress;
	}

	public static ClientSession fromPortableSession(ClientSession portable,
			InetSocketAddress peerAddress) {
		return new ClientSession(portable.version, portable.proxyVersion,
				portable.id, portable.serviceName, portable.creationTime,
				peerAddress);
	}

	@Override
	public byte getVersion() {
		return version;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.version = in.readByte();
		this.proxyVersion = in.readShort();
		this.id = in.readLong();
		this.serviceName = in.readUTF();
		this.creationTime = in.readLong();
		this.lastInvocationTime = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(version);
		out.writeShort(proxyVersion);
		out.writeLong(id);
		out.writeUTF(serviceName);
		out.writeLong(creationTime);
		out.writeLong(lastInvocationTime);
	}

	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public long getCreationTime() {
		return creationTime;
	}

	@Override
	public long getInvocationTime() {
		return lastInvocationTime;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id: ");
		sb.append(id);
		sb.append(", ");
		sb.append("version: ");
		sb.append(version);
		sb.append(", ");
		sb.append("proxy version: ");
		sb.append(proxyVersion);
		sb.append(", ");
		sb.append("server address: ");
		sb.append(this.peerAddress.toString());
		sb.append(", ");
		sb.append("service name: ");
		sb.append(this.serviceName);
		sb.append(", ");
		sb.append("creationTime: ");
		sb.append(creationTime);
		sb.append(", ");
		sb.append("lastInvocationTime: ");
		sb.append(lastInvocationTime);
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	public void touch() {
		this.lastInvocationTime = System.currentTimeMillis();
	}

	@Override
	public InetSocketAddress getPeerAddress() {
		return peerAddress;
	}

	@Override
	public short getClientVersion() {
		return proxyVersion;
	}
}
