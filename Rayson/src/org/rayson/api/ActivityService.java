package org.rayson.api;

import java.io.IOException;

public interface ActivityService {
	public void runActivity(ActivitySocket socket) throws IOException;
}
