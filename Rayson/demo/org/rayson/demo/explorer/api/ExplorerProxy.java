package org.rayson.demo.explorer.api;

import org.rayson.api.RpcProxy;
import org.rayson.exception.RpcException;

public interface ExplorerProxy extends RpcProxy {
	public String[] list(String path) throws RpcException;

}
