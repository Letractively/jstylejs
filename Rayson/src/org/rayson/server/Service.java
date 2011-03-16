package org.rayson.server;

import org.rayson.api.RpcProxy;
import org.rayson.api.RpcProtocol;
import org.rayson.exception.IllegalServiceException;
import org.rayson.util.ServiceParser;

class Service {
	private String description;
	private RpcProtocol instance;
	private String name;
	private Class<? extends RpcProxy>[] protocols;

	Service(String name, String description, RpcProtocol instance)
			throws IllegalServiceException {
		// TODO: throw IllegalServiceException
		this.name = name;
		this.description = description;
		this.instance = instance;
		this.protocols = ServiceParser.getProtocols(instance.getClass());
	}

	public String getDescription() {
		return this.description;
	}

	public RpcProtocol getInstance() {
		return instance;
	}

	public String getName() {
		return name;
	}

	public Class<? extends RpcProxy>[] getProtocols() {
		return protocols;
	}
}
