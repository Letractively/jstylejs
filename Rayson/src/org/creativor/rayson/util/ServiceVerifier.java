package org.creativor.rayson.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.creativor.rayson.api.CallFuture;
import org.creativor.rayson.api.Session;
import org.creativor.rayson.common.Stream;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.exception.NetWorkException;
import org.creativor.rayson.exception.RpcException;

public final class ServiceVerifier {
	public static void verifyServiceMethod(Method method)
			throws IllegalServiceException {

		// 1. return type must be portable.
		Class returnType = method.getReturnType();
		if (!Stream.isPortable(returnType))
			throw new IllegalServiceException("Method " + method.getName()
					+ " return type must be portable");
		boolean firstParaSession = false;
		Class<?>[] parameterTyps = method.getParameterTypes();

		// 2. every parameter type must be portable.
		for (int i = 0; i < parameterTyps.length; i++) {
			if (i == 0 && Session.class.equals(parameterTyps[i]))
				firstParaSession = true;
			if (i != 0 && !Stream.isPortable(parameterTyps[i]))
				throw new IllegalServiceException("Method " + method.getName()
						+ " parameter type must be portable");
		}

		// 3. First parameter must be session type.
		if (!firstParaSession)
			throw new IllegalServiceException("Service method "
					+ method.getName() + " first parameter type must be "
					+ Session.class.getName());
	}

	public static void verifyProxyMethod(Method method)
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
		if (!Stream.isPortable(returnType))
			throw new IllegalServiceException("Method " + method.getName()
					+ " return type must be portable");
		boolean foundSessionPara = false;
		// 3. every parameter type must be portable.
		Class[] parameterTypes = method.getParameterTypes();
		for (Class type : parameterTypes) {
			if (Session.class.isAssignableFrom(type))
				foundSessionPara = true;
			if (!Stream.isPortable(type))
				throw new IllegalServiceException("Method " + method.getName()
						+ " parameter type must be portable");
		}
		// 4. must not session paramter type.
		if (foundSessionPara)
			throw new IllegalServiceException("Proxy method "
					+ method.getName() + " parameter type must not "
					+ Session.class.getName());
	}

	public static void verifyAsyncProxyMethod(Method method)
			throws IllegalServiceException {
		// 1. must throws only network-exceptoion.
		Class<?>[] exceptionTypes = method.getExceptionTypes();
		if (exceptionTypes.length != 1
				|| !NetWorkException.class.isAssignableFrom(exceptionTypes[0])) {
			throw new IllegalServiceException("Method " + method.getName()
					+ " should only throws " + NetWorkException.class.getName());
		}

		// 2. return type must be call future.
		Class returnType = method.getReturnType();
		if (!CallFuture.class.isAssignableFrom(returnType))
			throw new IllegalServiceException("Return type must be "
					+ CallFuture.class.getName());
		// 3. call future parameter type must be portable.
		ParameterizedType genericReturnType = (ParameterizedType) method
				.getGenericReturnType();
		Type[] actualTypeArguments = genericReturnType.getActualTypeArguments();

		Type genericType = actualTypeArguments[0];

		if (GenericArrayType.class.isAssignableFrom(genericType.getClass()))
			genericType = ((GenericArrayType) genericType)
					.getGenericComponentType();

		if (!Stream.isPortable((Class) genericType))
			throw new IllegalServiceException("Method " + method.getName()
					+ " return type must be portable");
		boolean foundSessionPara = false;
		// 4. every parameter type must be portable.
		Class[] parameterTypes = method.getParameterTypes();
		for (Class type : parameterTypes) {
			if (Session.class.isAssignableFrom(type))
				foundSessionPara = true;
			if (!Stream.isPortable(type))
				throw new IllegalServiceException("Method " + method.getName()
						+ " parameter type must be portable");
		}
		// 5. must not session paramter type.
		if (foundSessionPara)
			throw new IllegalServiceException(
					"Proxy method parameter type must not "
							+ Session.class.getName());
	}
}
