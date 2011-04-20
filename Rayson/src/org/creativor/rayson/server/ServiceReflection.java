/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;
import org.creativor.rayson.common.Invocation;
import org.creativor.rayson.common.InvocationException;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.util.ServiceVerifier;

/**
 *
 * @author Nick Zhang
 */
class ServiceReflection {
	private String description;
	private RpcService instance;
	private String name;
	private Class<? extends RpcProxy>[] proxys;
	/**
	 * Map of <Proxy method hash code ==> service interface method>
	 */
	private HashMap<Integer, Method> methods;
	private ServiceProxyPair[] pairs;

	private static ServiceProxyPair[] getPairs(
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
		Class<? extends RpcProxy> interfake = proxyAnnotation.value();
		if (interfake == null || !interfake.isInterface())
			throw new IllegalServiceException(
					"Annotation proxy must be an interface");
		return interfake;
	}

	private static class ServiceProxyPair {

		private Class<? extends RpcService> serviceInterface;
		private Class<? extends RpcProxy> proxyInterface;
		private List<ServiceMethod> serviceMethods;

		ServiceProxyPair(Class<? extends RpcService> serviceInterface,
				Class<? extends RpcProxy> proxyInterface)
				throws IllegalServiceException {
			this.serviceInterface = serviceInterface;
			this.proxyInterface = proxyInterface;
			this.serviceMethods = new ArrayList<ServiceReflection.ServiceMethod>();
			ServiceMethod serviceMethod;
			for (Method method : this.serviceInterface.getDeclaredMethods()) {
				serviceMethod = new ServiceMethod(method, this.proxyInterface);
				this.serviceMethods.add(serviceMethod);
			}
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

		private int hashCode;

		private static Method findProxyMethod(Method serviceMethod,
				Class<? extends RpcProxy> proxyInterface)
				throws IllegalServiceException {
			Class<?>[] parameterTypes = serviceMethod.getParameterTypes();
			Class[] proxyMethodParaTypes = new Class[parameterTypes.length - 1];
			System.arraycopy(parameterTypes, 1, proxyMethodParaTypes, 0,
					proxyMethodParaTypes.length);
			// remove first parameter type of service method.
			Method proxyMethod;
			try {
				proxyMethod = proxyInterface.getMethod(serviceMethod.getName(),
						proxyMethodParaTypes);
			} catch (Exception e) {
				throw new IllegalServiceException(
						"Can not find associated method "
								+ serviceMethod.toGenericString()
								+ "in proxy interface");
			}

			return proxyMethod;
		}

		ServiceMethod(Method method, Class<? extends RpcProxy> proxyClass)
				throws IllegalServiceException {
			// 1. verify service method .
			ServiceVerifier.verifyServiceMethod(method);
			// 2. find associated proxy method.
			Method proxyMethod = findProxyMethod(method, proxyClass);
			// 3. verify proxy method.
			ServiceVerifier.verifyProxyMethod(proxyMethod);

			this.method = method;
			this.proxyMethod = proxyMethod;
			this.hashCode = Invocation.getHashCode(proxyMethod);
		}

		public Method getMethod() {
			return method;
		}

		public Method getProxyMethod() {
			return proxyMethod;
		}

		public int getHashCode() {
			return hashCode;
		}
	}

	ServiceReflection(String name, String description, RpcService instance)
			throws IllegalServiceException {
		// TODO: throw IllegalServiceException
		this.name = name;
		this.description = description;
		this.instance = instance;

		methods = new HashMap<Integer, Method>();
		this.pairs = getPairs(instance.getClass());
		this.proxys = new Class[this.pairs.length];
		for (int i = 0; i < this.pairs.length; i++) {
			this.proxys[i] = this.pairs[i].getProxyInterface();
		}
		// Initialize all the methods.
		for (ServiceProxyPair pair : this.pairs) {
			for (Iterator<ServiceMethod> iterator = pair.serviceMethods
					.iterator(); iterator.hasNext();) {
				ServiceMethod serviceMethod = iterator.next();
				this.methods.put(serviceMethod.getHashCode(),
						serviceMethod.method);
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
