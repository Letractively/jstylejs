package org.rayson.transport.server.activity;

import java.util.concurrent.LinkedBlockingQueue;

public class CallManager {

	private LinkedBlockingQueue<ActivityCall> callQueue;

	public CallManager() {
		callQueue = new LinkedBlockingQueue<ActivityCall>();
	}

	public void put(ActivityCall call) throws InterruptedException {
		this.callQueue.put(call);
	}

	public ActivityCall take() throws InterruptedException {
		return callQueue.take();
	}
}
