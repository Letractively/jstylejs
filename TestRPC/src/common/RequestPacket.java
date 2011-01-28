package common;

import java.io.IOException;

public class RequestPacket extends RpcPacket {

	RequestPacket(long callId, byte[] data) {
		super(callId, data);
	}

	@Override
	public Invocation readParameter() throws IOException {
		// TODO Auto-generated constructor stub
		return null;
	}

	public RequestPacket(long callId, long checkSum, short dataLength) {
		super(callId, checkSum, dataLength);
	}

}
