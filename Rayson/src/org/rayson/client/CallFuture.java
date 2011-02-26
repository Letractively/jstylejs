package org.rayson.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class CallFuture<V> implements Future<V> {

	private AtomicBoolean done;
	private AtomicBoolean cancelled;
	private V result;
	private Throwable exception;

	CallFuture() {
		done = new AtomicBoolean(false);
		cancelled = new AtomicBoolean(false);
	}

	void set(V v) {
		this.done.set(true);
		notifyDone();
		this.result = v;
	}

	protected void setException(Throwable t) {
		this.done.set(true);
		notifyDone();
		this.exception = t;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if (isDone() || isCancelled())
			return false;
		cancelled.set(true);
		if (mayInterruptIfRunning) {
			notifyDone();
		}
		return true;
	}

	private void notifyDone() {
		synchronized (done) {
			done.notifyAll();
		}
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		synchronized (done) {
			while (!isDone()) {
				done.wait();
			}
		}
		if (exception != null)
			throw new ExecutionException(exception);
		return result;
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		synchronized (done) {
			while (!isDone()) {
				done.wait(unit.toMillis(timeout));
			}
		}
		if (exception != null)
			throw new ExecutionException(exception);
		return result;
	}

	@Override
	public boolean isCancelled() {
		return cancelled.get();
	}

	@Override
	public boolean isDone() {
		if (isCancelled())
			return true;
		return done.get();
	}

}
