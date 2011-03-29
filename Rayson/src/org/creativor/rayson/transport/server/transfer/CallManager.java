package org.creativor.rayson.transport.server.transfer;

import java.util.concurrent.LinkedBlockingQueue;

class CallManager {

	private LinkedBlockingQueue<TransferCall> callQueue;

	public CallManager() {
		callQueue = new LinkedBlockingQueue<TransferCall>();
	}

	public void put(TransferCall call) throws InterruptedException {
		this.callQueue.put(call);
	}

	public TransferCall take() throws InterruptedException {
		return callQueue.take();
	}
}
