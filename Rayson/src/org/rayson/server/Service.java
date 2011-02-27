package org.rayson.server;

import org.rayson.api.IllegalServiceException;
import org.rayson.api.RpcService;
import org.rayson.util.ServiceParser;

class Service {
	private String name;
	private RpcService instance;
	private Class<? extends RpcService>[] protocols;
	private String description;

	Service(String name, String description, RpcService instance)
			throws IllegalServiceException {
		this.name = name;
		this.description = description;
		this.instance = instance;
		this.protocols = ServiceParser.getProtocols(instance);
	}

	public String getName() {
		return name;
	}

	public RpcService getInstance() {
		return instance;
	}

	public String getDescription() {
		return this.description;
	}

	public Class<? extends RpcService>[] getProtocols() {
		return protocols;
	}
}
