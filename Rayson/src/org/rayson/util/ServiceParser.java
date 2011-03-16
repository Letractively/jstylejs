package org.rayson.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.rayson.annotation.Proxy;
import org.rayson.api.RpcProxy;
import org.rayson.api.RpcService;
import org.rayson.common.Stream;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.RpcException;

public final class ServiceParser {

	public static Class<? extends RpcProxy>[] getProtocols(
			Class<? extends RpcService> serviceClass)
			throws IllegalServiceException {
		List<Class<? extends RpcProxy>> list = new ArrayList<Class<? extends RpcProxy>>();
		Class[] interfaces = serviceClass.getInterfaces();
		for (Class interfake : interfaces) {
			if (!RpcService.class.isAssignableFrom(interfake))
				continue;
			Proxy proxyAnnotation = ((Class<? extends RpcService>) interfake)
					.getAnnotation(Proxy.class);
			if (proxyAnnotation == null)
				continue;
			// throw new IllegalServiceException("Interfae "
			// + interfake.getName() + " must has annotaion "
			// + Protocols.class.getSimpleName());
			Class<? extends RpcProxy> interfake1 = proxyAnnotation.value();
			verifyService(interfake1);
			list.add(interfake1);

		}

		return list.toArray(new Class[0]);
	}

	private static void verifyMethod(Method method)
			throws IllegalServiceException {
		// 1. must throws rpc-exceptoion.
		boolean foundRemoteException = false;
		for (Class exceptionType : method.getExceptionTypes()) {
			if (exceptionType == RpcException.class) {
				foundRemoteException = true;
				break;
			}
		}
		if (!foundRemoteException)
			throw new IllegalServiceException("Method " + method.getName()
					+ " must throws " + RpcException.class.getName());
		// 2. return type must be portable.
		Class returnType = method.getReturnType();
		if (!Stream.isPortableType(returnType))
			throw new IllegalServiceException("Method " + method.getName()
					+ " return type must be portable");
		// 3. every parameter type must be portable.
		Class[] parameterTypes = method.getParameterTypes();
		for (Class type : parameterTypes) {
			if (!Stream.isPortableType(type))
				throw new IllegalServiceException("Method " + method.getName()
						+ " parameter type must be portable");
		}
	}

	public static void verifyService(Class<? extends RpcProxy> protocol)
			throws IllegalServiceException {
		if (!protocol.isInterface())
			throw new IllegalServiceException(
					"Rpc protocol must be an interface");
		Method[] methods = protocol.getDeclaredMethods();
		for (Method method : methods) {
			verifyMethod(method);
		}
	}

}
