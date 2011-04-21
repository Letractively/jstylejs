/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.server;

import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.lang.model.type.ArrayType;
import org.creativor.rayson.annotation.AsyncProxy;
import org.creativor.rayson.annotation.Proxy;
import org.creativor.rayson.api.AsyncRpcProxy;
import org.creativor.rayson.api.RpcProxy;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;
import org.creativor.rayson.common.Invocation;
import org.creativor.rayson.exception.CallInvokeException;
import org.creativor.rayson.exception.IllegalProxyMethodException;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.RpcCallException;
import org.creativor.rayson.exception.ServiceNotFoundException;
import org.creativor.rayson.util.Reflection;
import org.creativor.rayson.util.ServiceVerifier;

/**
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
			// verify methods of interface.
			for (Method method : interfake.getDeclaredMethods()) {
				try {
					ServiceVerifier.verifyServiceMethod(method);
				} catch (IllegalProxyMethodException e) {
					throw new IllegalServiceException(e.getMessage());
				}
			}
			serviceInterface = (Class<? extends RpcService>) interfake;
			proxyInterface = getProxy(serviceInterface);
			if (proxyInterface != null)
				list.add(new ServiceProxyPair(serviceInterface, proxyInterface));
			proxyInterface = getAsyncProxy(serviceInterface);
			if (proxyInterface != null)
				list.add(new ServiceProxyPair(serviceInterface, proxyInterface));
		}
		return list.toArray(new ServiceProxyPair[0]);
	}

	/**
	 * @param serviceInterface
	 * @return Found rpc proxy class. Or null when no found.
	 * @throws IllegalServiceException
	 */
	private static Class<? extends RpcProxy> getProxy(
			Class<? extends RpcService> serviceInterface)
			throws IllegalServiceException {
		Proxy proxyAnnotation = serviceInterface.getAnnotation(Proxy.class);
		if (proxyAnnotation == null)
			return null;
		Class<? extends RpcProxy> interfake = proxyAnnotation.value();
		if (interfake == null || interfake == RpcProxy.class
				|| !interfake.isInterface())
			throw new IllegalServiceException(
					"Annotation proxy must be an interface that extends "
							+ RpcProxy.class.getName());
		return interfake;
	}

	/**
	 * @param serviceInterface
	 * @return Found asynchronous rpc proxy class. Or null when no found.
	 * @throws IllegalServiceException
	 */
	private static Class<? extends RpcProxy> getAsyncProxy(
			Class<? extends RpcService> serviceInterface)
			throws IllegalServiceException {
		AsyncProxy proxyAnnotation = serviceInterface
				.getAnnotation(AsyncProxy.class);
		if (proxyAnnotation == null)
			return null;
		Class<? extends AsyncRpcProxy> interfake = proxyAnnotation.value();
		if (interfake == null || interfake == AsyncRpcProxy.class
				|| !interfake.isInterface())
			throw new IllegalServiceException(
					"Annotation proxy must be an interface that extends "
							+ AsyncRpcProxy.class.getName());
		return interfake;
	}

	private static class ServiceProxyPair {

		private Class<? extends RpcService> serviceInterface;
		private Class<? extends RpcProxy> proxyInterface;
		private List<ServiceMethod> serviceMethods;
		private boolean async = false;

		ServiceProxyPair(Class<? extends RpcService> serviceInterface,
				Class<? extends RpcProxy> proxyInterface)
				throws IllegalServiceException {
			this.serviceInterface = serviceInterface;
			this.proxyInterface = proxyInterface;
			this.serviceMethods = new ArrayList<ServiceReflection.ServiceMethod>();
			if (AsyncRpcProxy.class.isAssignableFrom(proxyInterface))
				async = true;
			ServiceMethod serviceMethod;
			for (Method method : this.serviceInterface.getDeclaredMethods()) {
				try {
					serviceMethod = new ServiceMethod(method,
							this.proxyInterface, async);
				} catch (IllegalProxyMethodException e) {
					throw new IllegalServiceException(e.getMessage());
				}
				this.serviceMethods.add(serviceMethod);
			}
		}

		/**
		 * @return True if the rpc proxy is a asynchronouse one.
		 */
		public boolean isAsync() {
			return async;
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

		private Class<?>[] serviceExceptions;
		private Class<?>[] proxyExceptions;

		private int hashCode;

		/**
		 * Find proxy method in proxyInterface which has the same method name
		 * and method parameter types with serviceMethod.
		 * 
		 * @param serviceMethod
		 * @param proxyInterface
		 * @return Found proxy method.
		 * @throws IllegalServiceException
		 *             If can not find associated proxy method.
		 */
		private static Method findProxyMethod(Method serviceMethod,
				Class<? extends RpcProxy> proxyInterface)
				throws IllegalProxyMethodException {
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
				throw new IllegalProxyMethodException(serviceMethod,
						"Can not find associated method in proxy interface");
			}

			return proxyMethod;
		}

		/**
		 * 
		 * @param method
		 * @param proxyClass
		 * @param async
		 *            Indicate that the proxy class is an {@link AsyncRpcProxy}.
		 * @throws IllegalProxyMethodException
		 */
		ServiceMethod(Method method, Class<? extends RpcProxy> proxyClass,
				boolean async) throws IllegalProxyMethodException {

			serviceExceptions = method.getExceptionTypes();
			// 1. find associated proxy method.
			Method proxyMethod = findProxyMethod(method, proxyClass);
			proxyExceptions = proxyMethod.getExceptionTypes();
			// 2. verify proxy method.
			if (async) {
				ServiceVerifier.verifyAsyncProxyMethod(proxyMethod);
			} else
				ServiceVerifier.verifyProxyMethod(proxyMethod);

			// 3. verify return type
			if (async) {
				ParameterizedType genericReturnType = (ParameterizedType) proxyMethod
						.getGenericReturnType();
				Type[] typeArguments = genericReturnType
						.getActualTypeArguments();
				if (typeArguments.length != 1)
					throw new IllegalProxyMethodException(proxyMethod,
							"No type argument found in generic return type: "
									+ genericReturnType);
				Class returnType = method.getReturnType();
				Type typeArgument = typeArguments[0];

				if (returnType.isArray()) {
					returnType = returnType.getComponentType();
					GenericArrayType arrayTypeArgument = (GenericArrayType) typeArgument;
					typeArgument = arrayTypeArgument.getGenericComponentType();
				} else {
					returnType = Reflection.getNonePrimitiveClass(returnType);
				}
				if (returnType != typeArgument)
					throw new IllegalProxyMethodException(
							proxyMethod,
							"Return type argument "
									+ typeArgument
									+ " is not match to service method return type");
			} else {
				if (proxyMethod.getReturnType() != method.getReturnType())
					throw new IllegalProxyMethodException(proxyMethod,
							"Return type is not equals to service method");
			}

			// 4.verify other exceptions
			if (!async) {
				for (Class serviceException : serviceExceptions) {
					verifyServiceException(serviceException);
				}
			}

			this.method = method;
			this.proxyMethod = proxyMethod;
		}

		/**
		 * @param serviceException
		 */
		private void verifyServiceException(Class serviceException)
				throws IllegalProxyMethodException {
			boolean match = false;
			for (Class proxyException : this.proxyExceptions) {
				if (proxyException == serviceException) {
					match = true;
					break;
				}
			}
			if (!match)
				throw new IllegalProxyMethodException(proxyMethod,
						"Can not find associated exception: "
								+ serviceException.toString()
								+ " in associated service method");
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
		this.name = name;
		this.description = description;
		this.instance = instance;

		methods = new HashMap<Integer, Method>();
		this.pairs = getPairs(instance.getClass());
		if (this.pairs.length == 0)
			throw new IllegalServiceException(
					"No any rpc proxy class found in this rpc service");
		this.proxys = new Class[this.pairs.length];
		for (int i = 0; i < this.pairs.length; i++) {
			this.proxys[i] = this.pairs[i].getProxyInterface();
		}
		// Initialize all the methods.
		for (ServiceProxyPair pair : this.pairs) {
			for (Iterator<ServiceMethod> iterator = pair.serviceMethods
					.iterator(); iterator.hasNext();) {
				ServiceMethod serviceMethod = iterator.next();
				this.methods.put(
						Invocation.getHashCode(serviceMethod.getProxyMethod()),
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
			throws RpcCallException {
		Method method = methods.get(invocation.getHashCode());
		if (method == null)
			throw new RpcCallException(new ServiceNotFoundException(
					"service of " + session.getServiceName() + " hash code "
							+ invocation.getHashCode() + " method "
							+ " not found"));

		try {
			return invocation.invoke(session, this.instance, method);
		} catch (CallInvokeException e) {
			throw new RpcCallException(e);
		}
	}
}
