package common;

public class PacketCounter {
	private long readCount = 0;
	private long writeCount = 0;

	PacketCounter() {
	}

	void readOne() {
		readCount++;
	}

	void writeOne() {
		writeCount++;
	}

	public long readCount() {
		return readCount;
	}

	public long writeCount() {
		return writeCount;
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
}