package org.creativor.rayson.transport.server.transfer;

class CallWorker extends Thread {

	private CallManager manager;

	public CallWorker(CallManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		while (true) {
			try {
				TransferCall call = manager.take();
				try {
					call.process();
				} catch (TransferCallException e) {
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
