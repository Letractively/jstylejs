package org.rayson.api;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.rayson.annotation.TransferCode;

@TransferCode(10)
public class TestTransferArgument implements TransferArgument {

	@Override
	public void read(DataInput in) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub

	}

}
