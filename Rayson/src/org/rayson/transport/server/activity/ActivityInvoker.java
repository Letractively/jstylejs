package org.rayson.transport.server.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.rayson.api.ActivityService;
import org.rayson.api.ActivitySocket;

public class ActivityInvoker {

	private short activity;

	public ActivityInvoker(short activity, ActivityService service,
			Method method) {
		super();
		this.activity = activity;
		this.service = service;
		this.method = method;
	}

	private ActivityService service;
	private Method method;

	public void invoke(ActivitySocket activitySocket)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		this.method.invoke(service, activitySocket);
	}

	public short getActivity() {
		return activity;
	}
}