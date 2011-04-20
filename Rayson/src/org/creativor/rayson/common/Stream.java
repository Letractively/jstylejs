/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.creativor.rayson.api.Portable;
import org.creativor.rayson.exception.UnportableTypeException;

/**
 *
 * @author Nick Zhang
 */
public final class Stream {
	public static boolean isPortable(Class type) {
		try {
			PortableObject.verifyPortable(type);
		} catch (UnportableTypeException e) {
			return false;
		}
		return true;
	}

	public static Object readPortable(DataInput in) throws IOException {
		try {
			return PortableObject.readObject(in);
		} catch (UnportableTypeException e) {
			throw new IOException(e);
		}
	}

	public static void writePortable(DataOutput out, Object value)
			throws IOException {
		try {
			PortableObject.writeObject(out, value);
		} catch (UnportableTypeException e) {
			throw new IOException(e);
		}
	}

	public static Portable read(DataInput in) throws IOException {
		return PortableObject.TRANSPORTABLE.read(in);
	}

	public static void write(DataOutput out, Portable value) throws IOException {
		PortableObject.TRANSPORTABLE.write(out, value);
	}
}
