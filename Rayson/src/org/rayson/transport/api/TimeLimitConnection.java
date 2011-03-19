package org.rayson.transport.api;

public abstract class TimeLimitConnection implements Connection {
	private long lastContactTime;

	protected TimeLimitConnection() {
		this.lastContactTime = System.currentTimeMillis();
	}

	public final boolean isTimeOut() {
		return (System.currentTimeMillis() - this.lastContactTime > getTimeoutInterval()) ? true
				: false;
	}

	public long getLastContactTime() {
		return lastContactTime;
	}

	protected abstract long getTimeoutInterval();

	public final void touch() {
		this.lastContactTime = System.currentTimeMillis();
	}

}
