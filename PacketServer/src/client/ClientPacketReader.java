package client;

import java.nio.channels.SocketChannel;
import java.util.Arrays;

import common.CRC16;
import common.ChecksumMatchException;
import common.ConnectionProtocol;
import common.PacketException;
import common.PacketReader;
import common.ResponseCode;

final class ClientPacketReader extends PacketReader {

	public ClientPacketReader(SocketChannel socketChannel) {
		super(socketChannel);
	}

	@Override
	protected void verifyPacket(byte code, short dataLength, short checksum,
			byte[] data) throws ChecksumMatchException, PacketException {
		ResponseCode responseCode = ResponseCode.valueOf(code);
		if (responseCode != ResponseCode.OK)
			throw new PacketException("Got error response code: "
					+ responseCode.name());
		short computedChecksum = CRC16.compute(code, dataLength, data);
		if (computedChecksum != checksum)
			throw new ChecksumMatchException();
	}

}
