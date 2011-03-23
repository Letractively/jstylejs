package org.rayson.demo.explorer.server;

import org.rayson.api.Session;

class ExplorerServiceImpl implements ExplorerService {
	private String rootPath;

	ExplorerServiceImpl(String rootPath) {
		this.rootPath = rootPath;
	}

	@Override
	public String[] list(Session session, String path) {
		return null;
	}

}
