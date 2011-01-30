package common;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface Connection {
	public void close() throws IOException;

	public boolean isTimeOut();

	public void read() throws IOException;

	public void touch();

	public void write() throws IOException;

}
