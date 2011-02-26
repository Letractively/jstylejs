package org.rayson;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.rayson.io.Writable;

public class ServiceDescription implements Writable {

	private ServiceDescription() {

	}

	public ServiceDescription(String serviceName,
			Class<? extends RpcService> serviceClass) {
		this.name = serviceName;
	}

	private String name;
	private String[] protocols;

	public String[] getProtocols() {
		return protocols;
	}

	public String getName() {
		return name;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.name = in.readUTF();
		// this.className = in.readUTF();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(name);
		// out.writeUTF(className);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("name: ");
		sb.append(this.name);
		sb.append(", protocols: ");
		sb.append("}");
		return sb.toString();
	}
}