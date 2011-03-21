package org.rayson.transport.server.activity;

import java.util.HashMap;

import org.rayson.api.ActivitySocket;

public class ActivityConnector {
	private HashMap<Short, ActivityInvoker> activityInvokers;
	private CallManager callManager;

	public ActivityConnector() {
		activityInvokers = new HashMap<Short, ActivityInvoker>();
		callManager = new CallManager();
	}

	public void submitCall(short activity, ActivitySocket socket)
			throws ActivityCallException {
		ActivityInvoker invoker = activityInvokers.get(activity);
		if (invoker == null)
			throw new ActivityCallException(
					new IllegalStateException(
							"Can not find activity invoker assiosiated with"
									+ activity));
		ActivityCall call = new ActivityCall(invoker, socket);
		try {
			callManager.put(call);
		} catch (InterruptedException e) {
			throw new ActivityCallException(e);
		}
	}

	public void start() {
		// TODO:
	}
}
