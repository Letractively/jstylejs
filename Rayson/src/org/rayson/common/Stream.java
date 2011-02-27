package org.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class Stream {
	public static void writePortable(DataOutput out, Object value)
			throws IOException {
		try {
			PortableObject.writeObject(out, value);
		} catch (UnportableTypeException e) {
			throw new IOException(e);
		}
	}

	public static Object readPortable(DataInput in) throws IOException {
		try {
			return PortableObject.readObject(in);
		} catch (UnportableTypeException e) {
			throw new IOException(e);
		}
	}

	public static boolean isPortableType(Class type) {
		try {
			PortableObject.verifyPortable(type);
		} catch (UnportableTypeException e) {
			return false;
		}
		return true;
	}
}
