package common;

import java.io.IOException;

public class ResponsePacket extends RpcPacket {

	ResponsePacket(long callId, byte[] data) {
		super(callId, data);
	}

	@Override
	public Responser readParameter() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public ResponsePacket(long callId, long checkSum, short dataLength) {
		super(callId, checkSum, dataLength);
	}

}
