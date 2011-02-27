package org.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import org.rayson.api.RpcService;
import org.rayson.api.Transportable;

public class Invocation implements Transportable {
	private String methodName;
	private Class<?>[] paraTypes;
	private String serviceName;
	private Object[] parameters;

	public Invocation() {

	}

	public Invocation(String serviceName, Method method, Object[] parameters)
			throws UnportableTypeException {
		this.serviceName = serviceName;
		this.methodName = method.getName();
		this.parameters = parameters;
		this.paraTypes = method.getParameterTypes();
	}

	public String getServiceName() {
		return serviceName;
	}

	public Object invoke(RpcService serviceObject)
			throws UndeclaredThrowableException, Throwable {
		Method method = serviceObject.getClass().getMethod(methodName,
				paraTypes);
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(serviceObject, parameters);
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			Class[] exceptionTypes = method.getExceptionTypes();
			for (Class exceptionType : exceptionTypes) {
				if (exceptionType.isAssignableFrom(targetException.getClass()))
					throw targetException;
			}
			throw new UndeclaredThrowableException(targetException);
		}
		return result;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.serviceName = in.readUTF();
		this.methodName = in.readUTF();
		byte paraLength = in.readByte();
		this.paraTypes = new Class[paraLength];
		this.parameters = new Object[paraLength];
		// read parameter type.
		String parameterClassName;
		Class parameterType;
		for (int i = 0; i < paraLength; i++) {
			parameterClassName = in.readUTF();
			try {
				parameterType = Class.forName(parameterClassName);
				this.paraTypes[i] = parameterType;
			} catch (ClassNotFoundException e) {
				throw new IOException(e);
			}
		}
		// read parameter object
		for (int i = 0; i < paraLength; i++) {
			this.parameters[i] = Stream.readPortable(in);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(serviceName);
		out.writeUTF(methodName);
		byte paraLenth = (byte) paraTypes.length;
		out.writeByte(paraLenth);
		// write parameter types.
		for (int i = 0; i < paraLenth; i++) {
			out.writeUTF(this.paraTypes[i].getName());
		}
		// write paramter objects.
		for (int i = 0; i < paraLenth; i++) {
			Stream.writePortable(out, this.parameters[i]);
		}
	}

}
