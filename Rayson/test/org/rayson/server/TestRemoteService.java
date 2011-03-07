package org.rayson.server;

import org.rayson.annotation.RpcService;
import org.rayson.api.RemoteService;
import org.rayson.api.TestRpcService;

@RpcService(protocols = { TestRpcService.class })
public interface TestRemoteService extends RemoteService {
	public void tdddestd();	
	}
