/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.exception;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.creativor.rayson.api.Portable;
import org.creativor.rayson.common.Stream;
import org.creativor.rayson.util.Reflection;

/**
 * 
 * @author Nick Zhang
 */
public class RpcCallException extends Exception implements Portable {

	private static final Class[] DEFAULT_CONSTRUCTOR_PARAMETER_TYPES = new Class[] { String.class };
	private static final long serialVersionUID = 1L;
	private Throwable throwException;

	private boolean invokeException = false;

	public RpcCallException() {

	}

	public RpcCallException(CallInvokeException invokeException) {
		this.invokeException = true;
		this.throwException = invokeException.getCause();
	}

	public RpcCallException(Throwable cause) {
		this.invokeException = false;
		this.throwException = cause;
	}

	/**
	 * Get exception that cause this RPC call exception.
	 */
	@Override
	public Throwable getCause() {
		return throwException;
	}

	/**
	 * @return True if this exception is throws when invoke the rpc call in the
	 *         remote server.
	 */
	public boolean isInvokeException() {
		return invokeException;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.invokeException = in.readBoolean();
		String className = in.readUTF();
		String message = (String) Stream.readPortable(in);
		try {
			Throwable throwable = (Throwable) Reflection.newInstance(className,
					DEFAULT_CONSTRUCTOR_PARAMETER_TYPES,
					new String[] { message });
			this.throwException = throwable;
		} catch (Throwable e) {
			this.invokeException = true;
			this.throwException = new RpcExcptionInstantiationException(e);
		}

	}

	@Override
	public void write(DataOutput out) throws IOException {
		// write exception type
		out.writeBoolean(invokeException);
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