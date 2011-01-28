package common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Writable {

	public void readFields(DataInput in) throws IOException;

	public void write(DataOutput out) throws IOException;
}
