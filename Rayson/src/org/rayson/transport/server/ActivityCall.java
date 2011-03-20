package org.rayson.transport.server;

import java.io.DataInput;
import java.io.IOException;

import org.rayson.api.ActivitySocket;

class ActivityCall {

	private ActivitySocket activitySocket;

	public ActivityCall(ActivitySocket activitySocket) {
		this.activitySocket = activitySocket;
	}

	public void testProcess() throws IOException {
		DataInput input = this.activitySocket.getDataInput();
		int readInt = input.readInt();
		System.out.println(readInt);
	}

}
