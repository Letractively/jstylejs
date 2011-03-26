package org.rayson.api;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.rayson.exception.RpcException;

public interface CallFuture<V> {

	/**
	 * Attempts to cancel execution of this task. This attempt will fail if the
	 * task has already completed, has already been cancelled, or could not be
	 * cancelled for some other reason. If successful, and this task has not
	 * started when <tt>cancel</tt> is called, this task should never run. If
	 * the task has already started, then the <tt>mayInterruptIfRunning</tt>
	 * parameter determines whether the thread executing this task should be
	 * interrupted in an attempt to stop the task.
	 * 
	 * <p>
	 * After this method returns, subsequent calls to {@link #isDone} will
	 * always return <tt>true</tt>. Subsequent calls to {@link #isCancelled}
	 * will always return <tt>true</tt> if this method returned <tt>true</tt>.
	 * 
	 * @param mayInterruptIfRunning
	 *            <tt>true</tt> if the thread executing this task should be
	 *            interrupted; otherwise, in-progress tasks are allowed to
	 *            complete
	 * @return <tt>false</tt> if the task could not be cancelled, typically
	 *         because it has already completed normally; <tt>true</tt>
	 *         otherwise
	 */
	boolean cancel(boolean mayInterruptIfRunning);

	/**
	 * Returns <tt>true</tt> if this task was cancelled before it completed
	 * normally.
	 * 
	 * @return <tt>true</tt> if this task was cancelled before it completed
	 */
	boolean isCancelled();

	/**
	 * Returns <tt>true</tt> if this task completed.
	 * 
	 * Completion may be due to normal termination, an exception, or
	 * cancellation -- in all of these cases, this method will return
	 * <tt>true</tt>.
	 * 
	 * @return <tt>true</tt> if this task completed
	 */
	boolean isDone();

	/**
	 * Waits if necessary for the computation to complete, and then retrieves
	 * its result.
	 * 
	 * @return the computed result
	 * @throws CancellationException
	 *             if the computation was cancelled
	 * @throws ExecutionException
	 *             if the computation threw an exception
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 */
	V get() throws InterruptedException, RpcException;

	/**
	 * Waits if necessary for at most the given time for the computation to
	 * complete, and then retrieves its result, if available.
	 * 
	 * @param timeout
	 *            the maximum time to wait
	 * @param unit
	 *            the time unit of the timeout argument
	 * @return the computed result
	 * @throws CancellationException
	 *             if the computation was cancelled
	 * @throws RpcException
	 *             if the computation threw an exception
	 * @throws InterruptedException
	 *             if the current thread was interrupted while waiting
	 * @throws TimeoutException
	 *             if the wait timed out
	 */
	V get(long timeout, TimeUnit unit) throws InterruptedException,
			RpcException, TimeoutException;
}
