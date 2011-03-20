package org.rayson.transport.api;

public abstract class TimeLimitConnection implements Connection {
	private long lastContactTime;
	private long creationTime;

	protected TimeLimitConnection() {
		this.lastContactTime = System.currentTimeMillis();
		this.creationTime = this.lastContactTime;
	}

	public final long getCreationTime() {
		return this.creationTime;
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
