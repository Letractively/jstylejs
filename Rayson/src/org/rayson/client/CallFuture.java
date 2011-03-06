package org.rayson.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.common.InvocationException;

public class CallFuture<V> implements Future<V> {

	private AtomicBoolean cancelled;
	private AtomicBoolean done;
	private InvocationException invocationException;
	private V result;

	CallFuture() {
		done = new AtomicBoolean(false);
		cancelled = new AtomicBoolean(false);
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

	@Override
	public V get() throws InterruptedException, ExecutionException {
		synchronized (done) {
			while (!isDone()) {
				done.wait();
			}
		}
		if (invocationException != null)
			throw new ExecutionException(invocationException);
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
		if (invocationException != null)
			throw new ExecutionException(invocationException);
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

	private void notifyDone() {
		synchronized (done) {
			done.notifyAll();
		}
	}

	void set(V v) {
		this.result = v;
		this.done.set(true);
		notifyDone();
	}

	protected void setException(InvocationException t) {
		this.invocationException = t;
		this.done.set(true);
		notifyDone();
	}

}
