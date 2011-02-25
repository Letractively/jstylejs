package call.transport.server;

import java.nio.channels.SocketChannel;

import call.transport.common.CRC16;
import call.transport.common.ChecksumMatchException;
import call.transport.common.PacketException;
import call.transport.common.PacketReader;
import call.transport.common.RequestCode;



class ServerPacketReader extends PacketReader {

	public ServerPacketReader(SocketChannel socketChannel) {
		super(socketChannel);
	}

	@Override
	protected void verifyPacket(byte code, short dataLength, short checksum,
			byte[] data) throws ChecksumMatchException, PacketException {
		RequestCode requestCode = RequestCode.valueOf(code);
		if (requestCode == RequestCode.UNKNOWN)
			throw new PacketException("Got error request code: "
					+ requestCode.name());
		short computedChecksum = CRC16.compute(code, dataLength, data);
		if (computedChecksum != checksum)
			throw new ChecksumMatchException(
					"Request packet checksum not match");
	}

}
