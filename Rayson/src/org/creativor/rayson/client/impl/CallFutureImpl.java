package org.creativor.rayson.client.impl;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.client.ConnectionClosedException;
import org.creativor.rayson.common.InvocationException;
import org.creativor.rayson.exception.CallException;
import org.creativor.rayson.exception.CallExecutionException;
import org.creativor.rayson.exception.RpcException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.exception.UnsupportedVersionException;
import org.creativor.rayson.impl.RpcExceptionImpl;

public class CallFutureImpl<V> implements CallFuture<V> {

	private AtomicBoolean cancelled;
	private AtomicBoolean done;
	private InvocationException invocationException;
	private V result;
	private Class<?>[] exceptionTypes;

	public CallFutureImpl(Class<?>[] exceptionTypes) {
		done = new AtomicBoolean(false);
		cancelled = new AtomicBoolean(false);
		this.exceptionTypes = exceptionTypes;
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
	public V get() throws InterruptedException, RpcException,
			CallExecutionException {
		synchronized (done) {
			while (!isDone()) {
				done.wait();
			}
		}
		if (invocationException != null) {
			throwException(invocationException);
		}
		return result;
	}

	private void throwException(InvocationException invocationException)
			throws RpcException, CallExecutionException {
		//
		Throwable remoteException = invocationException.getRemoteException();
		StackTraceElement[] stackTraceElements = Thread.currentThread()
				.getStackTrace();
		remoteException.setStackTrace(Arrays.copyOfRange(stackTraceElements,
				stackTraceElements.length - 1, stackTraceElements.length));

		if (invocationException.isUnDeclaredException())
			throw RpcExceptionImpl.createUndecleardException(remoteException);

		if (remoteException instanceof CallException)
			throw RpcExceptionImpl
					.createParameterException((CallException) remoteException);

		if (remoteException instanceof UnsupportedVersionException)
			throw RpcExceptionImpl
					.createUnsupportedProxyVersion((UnsupportedVersionException) remoteException);

		if (remoteException instanceof ConnectionClosedException)

			throw RpcExceptionImpl
					.createNetWorkException((ConnectionClosedException) remoteException);

		if (remoteException instanceof ServiceNotFoundException)

			throw RpcExceptionImpl
					.createServiceNotFoundException((ServiceNotFoundException) remoteException);

		boolean declaredExcutionException = false;
		if (this.exceptionTypes != null) {
			for (Class exception : this.exceptionTypes) {
				if (remoteException instanceof Exception)
					declaredExcutionException = true;
				break;
			}
		}

		if (declaredExcutionException) {
			throw new CallExecutionException(remoteException);
		} else
			throw RpcExceptionImpl.createUndecleardException(remoteException);

	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException,
			RpcException, TimeoutException, CallExecutionException {
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
			throwException(invocationException);
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
