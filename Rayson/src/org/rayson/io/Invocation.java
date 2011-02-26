package org.rayson.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Method;

public class Invocation implements Writable {
	private String methodName;
	private String serviceName;
	private IOObject[] parameters;
	private Class<?>[] paraTypes;

	public Invocation(String serviceName, Method method, Object[] parObjects) {

	}

	@Override
	public void read(DataInput in) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub

	}

}
