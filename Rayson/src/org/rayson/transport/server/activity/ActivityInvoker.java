package org.rayson.transport.server.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.rayson.api.ActivityService;
import org.rayson.api.ActivitySocket;

public class ActivityInvoker {

	public ActivityInvoker(ActivityService service, Method method) {
		super();
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

}
