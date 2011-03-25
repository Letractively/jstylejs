package org.rayson.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.rayson.annotation.Proxy;
import org.rayson.api.RpcProxy;
import org.rayson.api.RpcService;
import org.rayson.api.Session;
import org.rayson.common.Invocation;
import org.rayson.common.InvocationException;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.ServiceNotFoundException;
import org.rayson.util.ServiceVerifier;

class ServiceReflection {
	private String description;
	private RpcService instance;
	private String name;
	private Class<? extends RpcProxy>[] proxys;
	private HashMap<Integer, Method> methods;
	private Class<? extends RpcService>[] interfaces;

	private static ServiceProxyPair[] getPair(
			final Class<? extends RpcService> serviceClass)
			throws IllegalServiceException {
		List<ServiceProxyPair> list = new ArrayList<ServiceProxyPair>();
		Class<? extends RpcService> serviceInterface;
		Class<? extends RpcProxy> proxyInterface;
		Class<?>[] interfaces = serviceClass.getInterfaces();
		for (Class interfake : interfaces) {
			if (!RpcService.class.isAssignableFrom(interfake))
				continue;
			serviceInterface = (Class<? extends RpcService>) interfake;
			proxyInterface = getProxy(serviceInterface);
			list.add(new ServiceProxyPair(serviceInterface, proxyInterface));
		}
		return list.toArray(new ServiceProxyPair[0]);
	}

	private static Class<? extends RpcProxy> getProxy(
			Class<? extends RpcService> serviceInterface)
			throws IllegalServiceException {
		Proxy proxyAnnotation = serviceInterface.getAnnotation(Proxy.class);
		if (proxyAnnotation == null)
			throw new IllegalServiceException(
					"No proxy annotation found in interface "
							+ serviceInterface.getName());
		Class<? extends RpcProxy> interfake1 = proxyAnnotation.value();
		return interfake1;
	}

	private static class ServiceProxyPair {

		private Class<? extends RpcService> serviceInterface;
		private Class<? extends RpcProxy> proxyInterface;

		ServiceProxyPair(Class<? extends RpcService> serviceInterface,
				Class<? extends RpcProxy> proxyInterface) {
			this.serviceInterface = serviceInterface;
			this.proxyInterface = proxyInterface;
		}

		public Class<? extends RpcProxy> getProxyInterface() {
			return proxyInterface;
		}

		public Class<? extends RpcService> getServiceInterface() {
			return serviceInterface;
		}
	}

	private static class ServiceMethod {
		private Method method;
		private Method proxyMethod;

		ServiceMethod(Method method, RpcProxy proxy)
				throws IllegalServiceException {

		}

		public Method getMethod() {
			return method;
		}

		public Method getProxyMethod() {
			return proxyMethod;
		}
	}

	ServiceReflection(String name, String description, RpcService instance)
			throws IllegalServiceException {
		// TODO: throw IllegalServiceException
		this.name = name;
		this.description = description;
		this.instance = instance;

		methods = new HashMap<Integer, Method>();
		this.interfaces = ServiceVerifier.getServices(instance.getClass());
		this.proxys = new Class[this.interfaces.length];
		for (int i = 0; i < this.interfaces.length; i++) {
			this.proxys[i] = ServiceVerifier.getProxy(this.interfaces[i]);
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
