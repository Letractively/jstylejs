/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.api;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.creativor.rayson.annotation.ClientVersion;
import org.creativor.rayson.annotation.TransferCode;
import org.creativor.rayson.api.TransferArgument;

/**
 *
 * @author Nick Zhang
 */
@TransferCode(10)
@ClientVersion(2)
public class TestTransferArgument implements TransferArgument {
	private String path;

	private TestTransferArgument() {
	}

	public TestTransferArgument(String path) {
		this.path = path;
	}

	@Override
	public void read(DataInput in) throws IOException {
		this.path = in.readUTF();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(path);
	}

}
