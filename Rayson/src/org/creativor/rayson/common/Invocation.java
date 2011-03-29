package org.creativor.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.creativor.rayson.api.Portable;
import org.creativor.rayson.api.RpcService;
import org.creativor.rayson.api.Session;

public class Invocation implements Portable {

	private Object[] parameters;
	private int hashCode;

	private static final Object[] EMPTY_PARAMETERS = new Object[0];

	private static final Class<?> SESSION_CLASS = Session.class;

	public Invocation() {

	}

	public Invocation(Method method, Object[] parameters)
			throws UnportableTypeException {
		// TODO: throws UnportableTypeException
		if (parameters == null)
			this.parameters = EMPTY_PARAMETERS;
		else
			this.parameters = parameters;
		this.hashCode = getHashCode(method);
	}

	public static int getHashCode(Method method) {
		if (method == null)
			throw new IllegalArgumentException("Method should not be null");
		StringBuffer idString = new StringBuffer();
		idString.append(method.getName());
		idString.append("(");
		Class<?>[] parameterTypes = method.getParameterTypes();
		int parameterCount = parameterTypes.length;

		for (int i = 0; i < parameterCount; i++) {
			idString.append(parameterTypes[i].getName());
			idString.append(",");
		}

		if (parameterCount > 0)
			idString.deleteCharAt(idString.length() - 1);
		idString.append(")");
		return idString.toString().hashCode();
	}

	public int getHashCode() {
		return hashCode;
	}

	public Object invoke(Session session, RpcService serviceObject,
			Method method) throws InvocationException {
		Class[] realParaTypes = new Class[this.parameters.length + 1];
		realParaTypes[0] = SESSION_CLASS;
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
		this.hashCode = in.readInt();
		byte paraLength = in.readByte();
		this.parameters = new Object[paraLength];

		// read parameter object
		for (int i = 0; i < paraLength; i++) {
			this.parameters[i] = Stream.readPortable(in);
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("hash code: ");
		sb.append(hashCode);
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
		// write hash code
		out.writeInt(hashCode);
		byte paraLength = (byte) parameters.length;
		out.writeByte(paraLength);
		// write paramter objects.
		for (int i = 0; i < paraLength; i++) {
			Stream.writePortable(out, this.parameters[i]);
		}
	}
}
