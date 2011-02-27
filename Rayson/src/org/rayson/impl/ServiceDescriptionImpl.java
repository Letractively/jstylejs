package org.rayson.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.rayson.api.RpcService;
import org.rayson.api.ServiceRegistration;
import org.rayson.api.Transportable;
import org.rayson.common.Stream;

public class ServiceDescriptionImpl implements ServiceRegistration,
		Transportable {

	private ServiceDescriptionImpl() {
		// Forbidden construct.
	}

	public ServiceDescriptionImpl(String serviceName, String description,
			Class<? extends RpcService>[] protocols) {
		this.name = serviceName;
		this.description = description;
		this.protocols = new String[protocols.length];
		for (int i = 0; i < protocols.length; i++) {
			this.protocols[i] = protocols[i].getName();
		}
	}

	private String name;
	private String description;
	private String[] protocols;

	@Override
	public String[] getProtocols() {
		return protocols;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.name = in.readUTF();
		this.description = in.readUTF();
		this.protocols = (String[]) Stream.readPortable(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(name);
		out.writeUTF(description);
		Stream.writePortable(out, protocols);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("name: ");
		sb.append(this.name);
		sb.append(", ");
		sb.append("description: ");
		sb.append(this.description);
		sb.append(", ");
		sb.append("protocols: [");
		for (String protocol : protocols) {
			sb.append(protocol);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]}");
		return sb.toString();
	}

	@Override
	public String getDescription() {
		return this.description;
	}
}