/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.client.impl;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.client.ConnectionClosedException;
import org.creativor.rayson.exception.ReadInvocationException;
import org.creativor.rayson.exception.CallExecutionException;
import org.creativor.rayson.exception.RpcCallException;
import org.creativor.rayson.exception.RpcException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.exception.UnsupportedVersionException;
import org.creativor.rayson.impl.RpcExceptionImpl;

/**
 * 
 * @author Nick Zhang
 */
public class CallFutureImpl<V> implements CallFuture<V> {

	private AtomicBoolean cancelled;
	private AtomicBoolean done;
	private RpcCallException invocationException;
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

	private void throwException(RpcCallException invocationException)
			throws RpcException, CallExecutionException {
		//
		Throwable remoteException = invocationException.getCause();
		StackTraceElement[] stackTraceElements = Thread.currentThread()
				.getStackTrace();
		remoteException.setStackTrace(Arrays.copyOfRange(stackTraceElements,
				stackTraceElements.length - 1, stackTraceElements.length));

		if (invocationException.isInvokeException()) {
			boolean declaredInvokeException = false;
			if (this.exceptionTypes != null) {
				for (Class exception : this.exceptionTypes) {
					if (exception.isAssignableFrom(remoteException.getClass())) {
						declaredInvokeException = true;
						break;
					}
				}
			}

			if (declaredInvokeException) {
				throw new CallExecutionException(remoteException);
			} else
				throw RpcExceptionImpl
						.createUndecleardException(remoteException);
		}

		if (remoteException instanceof ReadInvocationException)
			throw RpcExceptionImpl
					.createParameterException((ReadInvocationException) remoteException);

		if (remoteException instanceof UnsupportedVersionException)
			throw RpcExceptionImpl
					.createUnsupportedProxyVersion((UnsupportedVersionException) remoteException);

		if (remoteException instanceof ConnectionClosedException)

			throw RpcExceptionImpl
					.createNetWorkException((ConnectionClosedException) remoteException);

		if (remoteException instanceof ServiceNotFoundException)

			throw RpcExceptionImpl
					.createServiceNotFoundException((ServiceNotFoundException) remoteException);

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

	public void setException(RpcCallException t) {
		this.invocationException = t;
		this.done.set(true);
		notifyDone();
	}

}
