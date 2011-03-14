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
	private long lastAccessedTime;

	public ClientSession() {

	}

	public ClientSession(byte protocol, long id, long creationTime,
			long lastAccessedTime) {
		this.protocol = protocol;
		this.id = id;
		this.creationTime = creationTime;
		this.lastAccessedTime = lastAccessedTime;
	}

	public byte getProtocol() {
		return protocol;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.protocol = in.readByte();
		this.id = in.readLong();
		this.creationTime = in.readLong();
		this.lastAccessedTime = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(protocol);
		out.writeLong(id);
		out.writeLong(creationTime);
		out.writeLong(lastAccessedTime);
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
	public long getLastAccessedTime() {
		return lastAccessedTime;
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
		sb.append("creatioTime: ");
		sb.append(creationTime);
		sb.append(", ");
		sb.append("lastAccessedTime: ");
		sb.append(lastAccessedTime);
		sb.append("}");
		return sb.toString();
	}
}
