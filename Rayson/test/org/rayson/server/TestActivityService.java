package org.rayson.server;

import java.io.IOException;

import org.rayson.annotation.Activity;
import org.rayson.api.ActivityService;
import org.rayson.api.ActivitySocket;

public class TestActivityService implements ActivityService {

	@Override
	@Activity(10)
	public void process(ActivitySocket socket) throws IOException {
		System.out.println("ActivitySocket read int: "
				+ socket.getDataInput().readInt());
	}

}
