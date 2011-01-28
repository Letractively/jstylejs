package common;

import java.io.IOException;

public interface Connection {
	public void close() throws IOException;

	public void addRpcCall(RpcCall call);

	public boolean isTimeOut();
}
