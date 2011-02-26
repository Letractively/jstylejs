package org.rayson.tool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionTool {

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
}
