package org.rayson.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.rayson.api.RpcService;
import org.rayson.common.Stream;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.RemoteException;

public final class ServiceParser {

	public static Class<? extends RpcService>[] getProtocols(
			Class<? extends RpcService> serviceClass)
			throws IllegalServiceException {
		List<Class<? extends RpcService>> list = new ArrayList<Class<? extends RpcService>>();
		for (Class interfake : serviceClass.getInterfaces()) {
			if (RpcService.class.isAssignableFrom(interfake)) {
				verifyService(interfake);
				list.add(interfake);
			}
		}
		return list.toArray(new Class[0]);
	}

	public static void verifyService(Class<? extends RpcService> protocol)
			throws IllegalServiceException {
		if (!protocol.isInterface())
			throw new IllegalServiceException(
					"Rpc protocol must be an interface");
		Method[] methods = protocol.getMethods();
		for (Method method : methods) {
			verifyMethod(method);
		}
	}

	private static void verifyMethod(Method method)
			throws IllegalServiceException {
		// 1. must throws rpcexceptoion.
		boolean foundRemoteException = false;
		for (Class exceptionType : method.getExceptionTypes()) {
			if (exceptionType == RemoteException.class) {
				foundRemoteException = true;
				break;
			}
		}
		if (!foundRemoteException)
			throw new IllegalServiceException("Method " + method.getName()
					+ " must throws " + RemoteException.class.getName());
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

}
