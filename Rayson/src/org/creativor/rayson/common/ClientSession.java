package org.creativor.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.creativor.rayson.api.Portable;
import org.creativor.rayson.api.Session;

public class ClientSession implements Portable, Session {

	private byte version;
	private long id;
	private long creationTime;
	private long lastInvocationTime;
	private String serviceName;
	private InetSocketAddress serverAddress;

	public ClientSession() {

	}

	public ClientSession(byte version, long sessionId, String serviceName,
			long creationTime, InetSocketAddress serverAddress) {
		this.version = version;
		this.id = sessionId;
		this.serviceName = serviceName;
		this.creationTime = creationTime;
		this.serverAddress = serverAddress;
	}

	@Override
	public byte getVersion() {
		return version;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.version = in.readByte();
		this.id = in.readLong();
		this.serviceName = in.readUTF();
		this.creationTime = in.readLong();
		this.lastInvocationTime = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(version);
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
		sb.append("server address: ");
		sb.append(this.serverAddress.toString());
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
		return serverAddress;
	}
}
