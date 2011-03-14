package org.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.rayson.api.Session;
import org.rayson.api.Transportable;

public class ClientSession implements Session, Transportable {

	private byte protocol;
	private long id;
	private long creationTime;
	private long lastInvocationTime;
	private String serviceName;

	public ClientSession() {

	}

	public ClientSession(Session session) {
		this.protocol = session.getProtocol();
		this.id = session.getId();
		this.serviceName = session.getServiceName();
		this.creationTime = session.getCreationTime();
		this.lastInvocationTime = session.getLastInvocationTime();
	}

	public byte getProtocol() {
		return protocol;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.protocol = in.readByte();
		this.id = in.readLong();
		this.serviceName = in.readUTF();
		this.creationTime = in.readLong();
		this.lastInvocationTime = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(protocol);
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
	public long getLastInvocationTime() {
		return lastInvocationTime;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id: ");
		sb.append(id);
		sb.append(", ");
		sb.append("protocol: ");
		sb.append(protocol);
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
}
