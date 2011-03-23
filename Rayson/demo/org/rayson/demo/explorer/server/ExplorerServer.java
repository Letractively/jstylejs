package org.rayson.demo.explorer.server;

import java.io.IOException;

import org.rayson.exception.IllegalServiceException;
import org.rayson.server.RpcServer;
import org.rayson.transport.api.ServiceAlreadyExistedException;

class ExplorerServer extends RpcServer {
	private static final String SERVICE_NAME = "explorer";
	private static final String SERVICE_DESC = "explorer file";
	private static final String ROOT_PATH = "/";

	ExplorerServer() {
		super(RpcServer.PORT_NUMBER);
	}

	@Override
	public void start() throws IOException {
		super.start();
		try {
			this.registerService(SERVICE_NAME, SERVICE_DESC,
					new ExplorerServiceImpl(ROOT_PATH));
			this.registerService(new DownloadTransfer());
		} catch (ServiceAlreadyExistedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}