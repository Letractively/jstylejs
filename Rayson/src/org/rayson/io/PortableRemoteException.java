package org.rayson.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.rayson.api.RpcException;
import org.rayson.api.Transportable;
import org.rayson.util.Reflection;

public class PortableRemoteException implements Transportable {
	private Throwable throwException;
	private boolean unDeclaredException;

	private static final Class[] DEFAULT_CONSTRUCTOR_PARAMETER_TYPES = new Class[] { String.class };

	public PortableRemoteException() {

	}

	public PortableRemoteException(boolean unDeclaredException,
			Throwable thrownException) {
		this.unDeclaredException = unDeclaredException;
		if (unDeclaredException) {
			this.throwException = new RpcException(thrownException);
		} else {
			this.throwException = thrownException;
		}
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.unDeclaredException = in.readBoolean();
		String className = in.readUTF();
		String message = (String) PortableObject.readObject(in);

		try {
			Throwable throwable = (Throwable) Reflection.newInstance(className,
					DEFAULT_CONSTRUCTOR_PARAMETER_TYPES,
					new Object[] { message });
			if (unDeclaredException)
				this.throwException = new RpcException(throwable);
			else
				this.throwException = throwable;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// write exception type
		out.writeBoolean(unDeclaredException);
		String className;
		String message;
		if (unDeclaredException) {
			className = this.throwException.getCause().getClass().getName();
			message = this.throwException.getCause().getMessage();
		} else {
			className = this.throwException.getClass().getName();
			message = this.throwException.getMessage();
		}
		// write class name.
		out.writeUTF(className);
		// write error message.
		PortableObject.writeObject(out, message);

	}

	public Throwable getThrowException() {
		return throwException;
	}

}
