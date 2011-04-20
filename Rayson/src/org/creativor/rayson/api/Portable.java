/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.api;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Portable {
	
	public void read(DataInput in) throws IOException;

	public void write(DataOutput out) throws IOException;
}
