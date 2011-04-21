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

	private static Map<Class<?>, Object> EMPTY_RETURN_VALUES = new HashMap<Class<?>, Object>();

	static {
		EMPTY_RETURN_VALUES.put(Void.TYPE, null);
		EMPTY_RETURN_VALUES.put(Boolean.TYPE, Boolean.FALSE);
		EMPTY_RETURN_VALUES.put(Byte.TYPE, Byte.valueOf((byte) 0));
		EMPTY_RETURN_VALUES.put(Short.TYPE, Short.valueOf((short) 0));
		EMPTY_RETURN_VALUES.put(Character.TYPE, Character.valueOf((char) 0));
		EMPTY_RETURN_VALUES.put(Integer.TYPE, Integer.valueOf(0));
		EMPTY_RETURN_VALUES.put(Long.TYPE, Long.valueOf(0));
		EMPTY_RETURN_VALUES.put(Float.TYPE, Float.valueOf(0));
		EMPTY_RETURN_VALUES.put(Double.TYPE, Double.valueOf(0));
	}

	private static Map<Class<?>, Class<?>> NOT_PRIMITIVE_CLASSES = new HashMap<Class<?>, Class<?>>();

	static {
		NOT_PRIMITIVE_CLASSES.put(Void.TYPE, Void.class);
		NOT_PRIMITIVE_CLASSES.put(Boolean.TYPE, Boolean.class);
		NOT_PRIMITIVE_CLASSES.put(Byte.TYPE, Byte.class);
		NOT_PRIMITIVE_CLASSES.put(Short.TYPE, Short.class);
		NOT_PRIMITIVE_CLASSES.put(Character.TYPE, Character.class);
		NOT_PRIMITIVE_CLASSES.put(Integer.TYPE, Integer.class);
		NOT_PRIMITIVE_CLASSES.put(Long.TYPE, Long.class);
		NOT_PRIMITIVE_CLASSES.put(Float.TYPE, Float.class);
		NOT_PRIMITIVE_CLASSES.put(Double.TYPE, Double.class);
	}

	public static Class getNonePrimitiveClass(Class klass) {
		if (!klass.isPrimitive())
			return klass;
		return NOT_PRIMITIVE_CLASSES.get(klass);
	}

	public static Object emptyReturnValueFor(final Class<?> type) {
		return type.isPrimitive() ? EMPTY_RETURN_VALUES.get(type) : null;
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
