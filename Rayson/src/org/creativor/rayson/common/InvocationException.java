/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.creativor.rayson.api.Portable;
import org.creativor.rayson.util.Reflection;

public class InvocationException extends Exception implements Portable {

	private static final Class[] DEFAULT_CONSTRUCTOR_PARAMETER_TYPES = new Class[] { String.class };
	private static final long serialVersionUID = 1L;
	private Throwable throwException;

	private boolean unDeclared;

	public InvocationException() {

	}

	public InvocationException(boolean unDeclaredException,
			Throwable thrownException) {
		this.unDeclared = unDeclaredException;
		this.throwException = thrownException;
	}

	public Throwable getRemoteException() {
		return throwException;
	}

	public boolean isUnDeclaredException() {
		return unDeclared;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.unDeclared = in.readBoolean();
		String className = in.readUTF();
		String message = (String) Stream.readPortable(in);
		try {
			Throwable throwable = (Throwable) Reflection.newInstance(className,
					DEFAULT_CONSTRUCTOR_PARAMETER_TYPES,
					new String[] { message });
			this.throwException = throwable;
		} catch (Throwable e) {
			this.unDeclared = true;
			this.throwException = new RpcExcptionInstantiationException(e);
		}

	}

	@Override
	public void write(DataOutput out) throws IOException {
		// write exception type
		out.writeBoolean(unDeclared);
		String className;
		String message;
		className = this.throwException.getClass().getName();
		message = this.throwException.getMessage();
		// write class name.
		out.writeUTF(className);
		// write error message.
		Stream.writePortable(out, message);
	}

}
