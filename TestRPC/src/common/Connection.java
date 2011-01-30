package common;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface Connection {
	public void close() throws IOException;

	public void addRpcCall(RpcCall call);

	public boolean isTimeOut();

	public void read() throws IOException;

	public void touch();

	public void build() throws IOException;

	public void write() throws IOException;

	public void readData(ByteBuffer longByteBuffer);
}
