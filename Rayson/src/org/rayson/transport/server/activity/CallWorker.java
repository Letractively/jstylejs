package org.rayson.transport.server.activity;

class CallWorker extends Thread {

	private CallManager manager;

	public CallWorker(CallManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		while (true) {
			try {
				ActivityCall call = manager.take();
				try {
					call.process();
				} catch (ActivityCallException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
	}
}
