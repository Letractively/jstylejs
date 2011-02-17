package common;

public interface ConnectionProtocol {
	public static final int HEADER_LENGTH = 512;// protocol+version+reserved.
	// + reserved.
	public static final int PACKET_HEADER_SIZE = 8 + 2;
	public static final int RESPONSE_LENGTH = 512;// connection code
	public static final int MAX_PENDING_PACKETS = 1000;
}