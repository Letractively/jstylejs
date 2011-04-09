package org.creativor.viva.api;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.creativor.rayson.api.Portable;

public class PortableStaff implements Portable {

	public PortableStaff(int id, String ip, short port) {
		super();
		this.id = id;
		this.ip = ip;
		this.port = port;
	}

	private PortableStaff() {
		// Only for transport.
	}

	private int id;
	private String ip;
	private short port;

	@Override
	public void read(DataInput in) throws IOException {
		this.id = in.readInt();
		this.ip = in.readUTF();
		this.port = in.readShort();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(id);
		out.writeUTF(ip);
		out.writeShort(port);
	}

	public int getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public short getPort() {
		return port;
	}

	@Override
	public String toString() {
		return "{id: " + id + ", ip: " + ip + ", port: " + port + "}";
	}
}