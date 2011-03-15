package org.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.rayson.api.RpcService;
import org.rayson.api.Session;
import org.rayson.api.Transportable;
import org.rayson.exception.ServiceNotFoundException;

public class Invocation implements Transportable {
	private String methodName;
	private Object[] parameters;
	private Class<?>[] paraTypes;
	private static final Class SESSION_CLASS = Session.class;

	public Invocation() {

	}

	public Invocation(Method method, Object[] parameters)
			throws UnportableTypeException {
		// TODO: throws UnportableTypeException
		this.methodName = method.getName();
		this.parameters = parameters;
		this.paraTypes = method.getParameterTypes();
	}

	public String getMethodName() {
		return methodName;
	}

	public Object invoke(Session session, RpcService serviceObject)
			throws InvocationException {
		Method method;
		try {
			Class[] realParaTypes = new Class[this.paraTypes.length + 1];
			realParaTypes[0] = SESSION_CLASS;
			System.arraycopy(paraTypes, 0, realParaTypes, 1,
					this.paraTypes.length);
			method = serviceObject.getClass().getMethod(methodName,
					realParaTypes);
		} catch (Exception e) {
			throw new InvocationException(false, new ServiceNotFoundException(
					"service of " + session.getServiceName() + "." + methodName
							+ " not found"));
		}
		method.setAccessible(true);
		Object result = null;
		try {
			Object[] realParameters = new Object[this.parameters.length + 1];
			realParameters[0] = session;
			System.arraycopy(parameters, 0, realParameters, 1,
					this.parameters.length);

			result = method.invoke(serviceObject, realParameters);
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			Class[] exceptionTypes = method.getExceptionTypes();
			for (Class exceptionType : exceptionTypes) {
				if (exceptionType.isAssignableFrom(targetException.getClass()))
					throw new InvocationException(false, targetException);
			}
			throw new InvocationException(true, targetException);
		} catch (Exception e) {
			throw new InvocationException(true, e);
		}
		return result;
	}

	@Override
	public void read(DataInput in) throws IOException {
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
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("method name: ");
		sb.append(methodName);
		sb.append(", parameters: [");
		for (Object parameter : parameters) {
			sb.append((parameter == null) ? "null" : parameter.toString());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}

	@Override
	public void write(DataOutput out) throws IOException {
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
