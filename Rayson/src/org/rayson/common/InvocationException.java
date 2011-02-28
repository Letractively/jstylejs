package org.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.rayson.api.Transportable;
import org.rayson.util.Reflection;

public class InvocationException extends Exception implements Transportable {

	private static final long serialVersionUID = 1L;
	private Throwable throwException;
	private boolean unDeclaredException;

	private static final Class[] DEFAULT_CONSTRUCTOR_PARAMETER_TYPES = new Class[] { String.class };

	public InvocationException() {

	}

	public boolean isUnDeclaredException() {
		return unDeclaredException;
	}

	public InvocationException(boolean unDeclaredException,
			Throwable thrownException) {
		this.unDeclaredException = unDeclaredException;
		this.throwException = thrownException;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.unDeclaredException = in.readBoolean();
		String className = in.readUTF();
		String message = (String) Stream.readPortable(in);
		try {
			Throwable throwable = (Throwable) Reflection.newInstance(className,
					DEFAULT_CONSTRUCTOR_PARAMETER_TYPES,
					new String[] { message });
			this.throwException = throwable;
		} catch (Throwable e) {
			this.unDeclaredException = true;
			this.throwException = new RpcExcptionInstantiationException(e);
		}

	}

	@Override
	public void write(DataOutput out) throws IOException {
		// write exception type
		out.writeBoolean(unDeclaredException);
		String className;
		String message;
		className = this.throwException.getClass().getName();
		message = this.throwException.getMessage();
		// write class name.
		out.writeUTF(className);
		// write error message.
		Stream.writePortable(out, message);
	}

	public Throwable getThrowException() {
		return throwException;
	}

}
