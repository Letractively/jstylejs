package org.rayson.transport.server.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.rayson.annotation.Activity;
import org.rayson.api.ActivityService;
import org.rayson.api.ActivitySocket;
import org.rayson.exception.IllegalServiceException;
import org.rayson.transport.api.ServiceAlreadyExistedException;

public class ActivityConnector {
	private HashMap<Short, ActivityInvoker> activityInvokers;
	private CallManager callManager;
	private static final int INIT_WORKER_COUNT = 4;
	private List<CallWorker> callWorkers;
	private static final String PROCESS_METHOD_NAME = "process";

	private static final Class<?> PROCESS_METHOD_PARATYPE = ActivitySocket.class;

	public ActivityConnector() {
		activityInvokers = new HashMap<Short, ActivityInvoker>();
		callManager = new CallManager();
		callWorkers = new ArrayList<CallWorker>();
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
		CallWorker worker;
		for (int i = 0; i < INIT_WORKER_COUNT; i++) {
			worker = new CallWorker(callManager);
			worker.start();
			this.callWorkers.add(worker);
		}

	}

	public void registerService(ActivityService service)
			throws ServiceAlreadyExistedException, IllegalServiceException {
		if (service == null)
			throw new IllegalServiceException("Service is null");
		Class<? extends ActivityService> serviceClass = service.getClass();
		try {
			Method processMethod = serviceClass.getMethod(PROCESS_METHOD_NAME,
					PROCESS_METHOD_PARATYPE);
			Activity activityAnnotation = processMethod
					.getAnnotation(Activity.class);
			if (activityAnnotation == null)
				throw new IllegalServiceException(
						"No activity annotation found in service method "
								+ PROCESS_METHOD_NAME + "()");
			short activity = activityAnnotation.value();
			ActivityInvoker invoker = activityInvokers.get(activity);
			if (invoker != null)
				throw new ServiceAlreadyExistedException("Activity service "
						+ activity);
			invoker = new ActivityInvoker(activity, service, processMethod);
			this.activityInvokers.put(activity, invoker);

		} catch (Exception e) {
			throw new IllegalServiceException(e.getMessage());
		}

	}

	public boolean serviceExists(short activity) {
		return this.activityInvokers.containsKey(activity);
	}
}
