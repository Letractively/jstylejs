package org.rayson.transport.server.activity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.rayson.api.ActivitySocket;

class ActivityCall {

	private ActivitySocket activitySocket;
	private ActivityInvoker invoker;

	public ActivityCall(ActivityInvoker invoker, ActivitySocket activitySocket) {
		this.activitySocket = activitySocket;
		this.invoker = invoker;
	}

	/**
	 * Process this call, finally close the activity socket.
	 * 
	 * @throws ActivityCallException
	 */
	public void process() throws ActivityCallException {
		try {
			this.invoker.invoke(this.activitySocket);
		} catch (Exception e) {
			Throwable throwable = e;
			if (e instanceof InvocationTargetException) {
				throwable = ((InvocationTargetException) e)
						.getTargetException();
			}
			throw new ActivityCallException(throwable);
		} finally {
			try {
				this.activitySocket.close();
			} catch (IOException e) {
				throw new ActivityCallException(e);
			}
		}
	}

}
