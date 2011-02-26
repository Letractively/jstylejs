package org.rayson.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class Streamer {
	public static void writePortable(DataOutput out, Object value)
			throws IOException {
		PortableObject.writeObject(out, value);
	}

	public static Object readPortable(DataInput in) throws IOException {
		return PortableObject.readObject(in);
	}

}
