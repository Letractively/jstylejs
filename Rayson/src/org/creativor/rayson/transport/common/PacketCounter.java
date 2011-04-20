/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.common;

public class PacketCounter {
	private long readCount = 0;
	private long writeCount = 0;

	public PacketCounter() {
	}

	public long readCount() {
		return readCount;
	}

	public void readOne() {
		readCount++;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("read: ");
		sb.append(readCount);
		sb.append(", write: ");
		sb.append(writeCount);
		sb.append("}");
		return sb.toString();
	}

	public long writeCount() {
		return writeCount;
	}

	public void writeOne() {
		writeCount++;
	}
}