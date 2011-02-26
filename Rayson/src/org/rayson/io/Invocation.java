package org.rayson.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.rayson.RpcService;

public class Invocation implements Writable {
	private String methodName;
	private Class<?>[] paraTypes;
	private String serviceName;
	private Object[] parameters;

	public Invocation() {

	}

	public Invocation(String serviceName, Method method, Object[] parameters)
			throws UnsupportedIOObjectException {
		this.serviceName = serviceName;
		this.methodName = method.getName();
		this.parameters = parameters;
		this.paraTypes = method.getParameterTypes();
		for (int i = 0; i < parameters.length; i++) {
			IOObject.valueOf(parameters[i]);
		}
	}

	public String getServiceName() {
		return serviceName;
	}

	public Object invoke(RpcService serviceObject) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Method method = serviceObject.getClass().getMethod(methodName,
				paraTypes);
		method.setAccessible(true);
		return method.invoke(serviceObject, parameters);
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
		short paraObjectType;
		IOObject paraIoObject;
		for (int i = 0; i < paraLength; i++) {
			paraObjectType = in.readShort();
			try {
				paraIoObject = IOObject.valueOf(paraObjectType);
				this.parameters[i] = paraIoObject.read(in);
			} catch (UnsupportedIOObjectException e) {
				throw new IOException(e);
			}
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
			try {
				IOObject ioObject = IOObject.valueOf(parameters[i]);
				// write io object type
				out.writeShort(ioObject.getType());
				ioObject.write(out, this.parameters[i]);
			} catch (UnsupportedIOObjectException e) {
				throw new IOException(e);
			}
		}
	}

}
