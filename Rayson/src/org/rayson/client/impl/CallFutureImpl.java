package org.rayson.client.impl;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.rayson.api.CallFuture;
import org.rayson.client.ConnectionClosedException;
import org.rayson.common.InvocationException;
import org.rayson.exception.CallException;
import org.rayson.exception.RpcException;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.impl.RemoteExceptionImpl;

public class CallFutureImpl<V> implements CallFuture<V> {

	private AtomicBoolean cancelled;
	private AtomicBoolean done;
	private InvocationException invocationException;
	private V result;

	public CallFutureImpl() {
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
	public V get() throws InterruptedException, RpcException {
		synchronized (done) {
			while (!isDone()) {
				done.wait();
			}
		}
		if (invocationException != null) {
			throwRpcException(invocationException);
		}
		return result;
	}

	private void throwRpcException(InvocationException invocationException)
			throws RpcException {
		//
		Throwable remoteException = invocationException.getRemoteException();
		StackTraceElement[] stackTraceElements = Thread.currentThread()
				.getStackTrace();
		remoteException.setStackTrace(Arrays.copyOfRange(stackTraceElements,
				stackTraceElements.length - 1, stackTraceElements.length));

		if (invocationException.isUnDeclaredException())
			throw RemoteExceptionImpl
					.createUndecleardException(remoteException);

		if (remoteException instanceof CallException)
			throw RemoteExceptionImpl
					.createParameterException((CallException) remoteException);

		if (remoteException instanceof ConnectionClosedException)

			throw RemoteExceptionImpl
					.createNetWorkException((ConnectionClosedException) remoteException);

		if (remoteException instanceof ServiceNotFoundException)

			throw RemoteExceptionImpl
					.createServiceNotFoundException((ServiceNotFoundException) remoteException);

		throw RemoteExceptionImpl.createUndecleardException(remoteException);

	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException,
			RpcException, TimeoutException {
		long startTime = System.currentTimeMillis();
		long timeOutMillis = unit.toMillis(timeout);
		synchronized (done) {
			while (!isDone()) {
				done.wait(timeOutMillis);
			}
		}
		if (System.currentTimeMillis() - startTime > timeOutMillis)
			throw new TimeoutException();

		if (invocationException != null) {
			throwRpcException(invocationException);
		}
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

	public void set(V v) {
		this.result = v;
		this.done.set(true);
		notifyDone();
	}

	public void setException(InvocationException t) {
		this.invocationException = t;
		this.done.set(true);
		notifyDone();
	}

}
