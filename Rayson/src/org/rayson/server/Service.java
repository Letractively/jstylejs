package org.rayson.server;

import org.rayson.api.RpcService;
import org.rayson.util.ServiceParser;

class Service {
	private String name;
	private RpcService instance;
	private Class<? extends RpcService>[] protocols;

	Service(String name, RpcService instance) {
		this.name = name;
		this.instance = instance;
		this.protocols = ServiceParser.getProtocols(instance);
	}

	public String getName() {
		return name;
	}

	public RpcService getInstance() {
		return instance;
	}

	public Class<? extends RpcService>[] getProtocols() {
		return protocols;
	}
}
