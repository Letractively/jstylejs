package org.rayson.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.rayson.api.RpcProxy;
import org.rayson.api.RpcService;
import org.rayson.api.Session;
import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.util.ServiceParser;

class Service {
	private String description;
	private RpcService instance;
	private String name;
	private Class<? extends RpcProxy>[] proxys;
	private HashMap<Integer, Method> methods;
	private Class<? extends RpcService>[] interfaces;

	Service(String name, String description, RpcService instance)
			throws IllegalServiceException {
		// TODO: throw IllegalServiceException
		this.name = name;
		this.description = description;
		this.instance = instance;

		methods = new HashMap<Integer, Method>();
		this.interfaces = ServiceParser.getServices(instance.getClass());
		this.proxys = new Class[this.interfaces.length];
		for (int i = 0; i < this.interfaces.length; i++) {
			this.proxys[i] = ServiceParser.getProxy(this.interfaces[i]);
		}
		// Initialize all the methods.
		for (Class<? extends RpcService> interfake : this.interfaces) {
			for (Method method : interfake.getMethods()) {
				methods.put(Invocation.getHashCode(method), method);
			}
		}
	}

	public String getDescription() {
		return this.description;
	}

	public RpcService getInstance() {
		return instance;
	}

	public String getName() {
		return name;
	}

	Class<? extends RpcProxy>[] getProxys() {
		return proxys;
	}

	public Object invoke(Session session, Invocation invocation)
			throws InvocationException {
		Method method = methods.get(invocation.getHashCode());
		if (method == null)
			throw new InvocationException(false, new ServiceNotFoundException(
					"service of " + session.getServiceName() + " hash code "
							+ invocation.getHashCode() + " method "
							+ " not found"));

		return invocation.invoke(session, this.instance, method);
	}
}
