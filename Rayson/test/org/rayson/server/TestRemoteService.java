package org.rayson.server;

import org.rayson.annotation.RpcProtocols;
import org.rayson.api.RpcService;
import org.rayson.api.TestRpcProtocol;

@RpcProtocols({ TestRpcProtocol.class })
public interface TestRemoteService extends RpcService {
	public void tdddestd();
}
