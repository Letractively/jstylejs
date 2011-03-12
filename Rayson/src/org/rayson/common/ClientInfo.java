package org.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.rayson.api.Transportable;

public class ClientInfo implements Transportable {

	private byte protocol;

	private ClientInfo() {

	}

	public ClientInfo(byte protocol) {
		this.protocol = protocol;
	}

	public byte getProtocol() {
		return protocol;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.protocol = in.readByte();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(protocol);
	}

}
