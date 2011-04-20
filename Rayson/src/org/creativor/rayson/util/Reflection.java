/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nick Zhang
 */
public class Reflection {
	private static Map<Class<?>, Object> emptyReturnValues = new HashMap<Class<?>, Object>();

	static {
		emptyReturnValues.put(Void.TYPE, null);
		emptyReturnValues.put(Boolean.TYPE, Boolean.FALSE);
		emptyReturnValues.put(Byte.TYPE, Byte.valueOf((byte) 0));
		emptyReturnValues.put(Short.TYPE, Short.valueOf((short) 0));
		emptyReturnValues.put(Character.TYPE, Character.valueOf((char) 0));
		emptyReturnValues.put(Integer.TYPE, Integer.valueOf(0));
		emptyReturnValues.put(Long.TYPE, Long.valueOf(0));
		emptyReturnValues.put(Float.TYPE, Float.valueOf(0));
		emptyReturnValues.put(Double.TYPE, Double.valueOf(0));
	}

	public static Object emptyReturnValueFor(final Class<?> type) {
		return type.isPrimitive() ? emptyReturnValues.get(type) : null;
	}

	public static Object newInstance(String className)
			throws ClassNotFoundException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Class klass = Class.forName(className);
		Constructor constructor = klass.getDeclaredConstructor(null);
		constructor.setAccessible(true);
		return constructor.newInstance(null);
	}

	public static Object newInstance(String className,
			Class<?>[] parameterTypes, Object[] initargs)
			throws ClassNotFoundException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Class klass = Class.forName(className);
		Constructor constructor = klass.getDeclaredConstructor(parameterTypes);
		constructor.setAccessible(true);
		return constructor.newInstance(initargs);
	}
}
