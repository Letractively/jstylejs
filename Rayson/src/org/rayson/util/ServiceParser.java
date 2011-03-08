package org.rayson.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.rayson.api.RpcProtocol;
import org.rayson.common.Stream;
import org.rayson.exception.IllegalServiceException;
import org.rayson.exception.RemoteException;

public final class ServiceParser {

	public static Class<? extends RpcProtocol>[] getProtocols(
			Class<? extends RpcProtocol> serviceClass)
			throws IllegalServiceException {
		List<Class<? extends RpcProtocol>> list = new ArrayList<Class<? extends RpcProtocol>>();
		for (Class interfake : serviceClass.getInterfaces()) {
			if (RpcProtocol.class.isAssignableFrom(interfake)) {
				verifyService(interfake);
				list.add(interfake);
			}
		}
		return list.toArray(new Class[0]);
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

	public static void verifyService(Class<? extends RpcProtocol> protocol)
			throws IllegalServiceException {
		if (!protocol.isInterface())
			throw new IllegalServiceException(
					"Rpc protocol must be an interface");
		Method[] methods = protocol.getMethods();
		for (Method method : methods) {
			verifyMethod(method);
		}
	}

}
