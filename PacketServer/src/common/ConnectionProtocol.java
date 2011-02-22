package common;

/**
 * Define the protocol of the connection between client and server. <br>
 * <h4>Connection header format:</h4>
 * <table border="1">
 * <tr>
 * <td>protocol(1)</td>
 * <td>version(2)</td>
 * <td>reserved(125)</td>
 * </tr>
 * </table>
 * <h4>Connection response format:</h4>
 * <table border="1">
 * <tr>
 * <td>response code(1)</td>
 * <td>reserved(127)</td>
 * </tr>
 * </table>
 * <h4>Request packet format:</h4>
 * <table border="1">
 * <tr>
 * <td>request code(1)</td>
 * <td>data length(2)</td>
 * <td>checksum(2)</td>
 * <td>data</td>
 * </tr>
 * </table>
 * <h4>Response packet format:</h4>
 * <table border="1">
 * <tr>
 * <td>response code(1)</td>
 * <td>data length(2)</td>
 * <td>checksum(2)</td>
 * <td>data</td>
 * </tr>
 * </table>
 * <br>
 * Please notes, all size unit is <b>Byte</b>.
 * 
 */
public interface ConnectionProtocol {
	public static final int HEADER_LENGTH = 128;// protocol+version+reserved.
	public static final int PACKET_HEADER_SIZE = 5; // type+data length+checksum
	public static final int RESPONSE_LENGTH = 128;// connection code_reserved
	public static final int MAX_PENDING_PACKETS = 1000;
	public static final int MAX_PACKET_DATA_LENGTH = 100 * 1204;
}