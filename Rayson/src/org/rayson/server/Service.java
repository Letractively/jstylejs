package org.rayson.server;

import org.rayson.api.RpcService;
import org.rayson.exception.IllegalServiceException;
import org.rayson.util.ServiceParser;

class Service {
	private String description;
	private RpcService instance;
	private String name;
	private Class<? extends RpcService>[] protocols;

	Service(String name, String description, RpcService instance)
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

	public RpcService getInstance() {
		return instance;
	}

	public String getName() {
		return name;
	}

	public Class<? extends RpcService>[] getProtocols() {
		return protocols;
	}
}
