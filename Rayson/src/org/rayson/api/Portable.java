package org.rayson.api;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Portable {

	public void read(DataInput in) throws IOException;

	public void write(DataOutput out) throws IOException;
}
