package org.rayson.api;

import java.io.IOException;

public interface ActivityService {
	public void process(ActivitySocket socket) throws IOException;
}
