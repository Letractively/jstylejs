package org.rayson.transport.common;

import org.rayson.transport.stream.TransferResponse;

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
 * <td>connection state(1)</td>
 * <td>reserved(127)</td>
 * </tr>
 * </table>
 * <br/>
 * See {@link ConnectionState} about connection state. <br/>
 * 
 * <h4>Request packet format:</h4>
 * <table border="1">
 * <tr>
 * <td>request type(1)</td>
 * <td>data length(2)</td>
 * <td>checksum(2)</td>
 * <td>data</td>
 * </tr>
 * </table>
 * <br/>
 * See {@link ResponseType} about packet response type. <br/>
 * 
 * <h4>Response packet format:</h4>
 * <table border="1">
 * <tr>
 * <td>response type(1)</td>
 * <td>data length(2)</td>
 * <td>checksum(2)</td>
 * <td>data</td>
 * </tr>
 * </table>
 * <br/>
 * See {@link RequestType} about packet request type. <br/>
 * <h4>Transfer request header format:</h4>
 * <table border="1">
 * <tr>
 * <td>transfer code(2)</td>
 * <td>data length(2)</td>
 * <td>argument data</td>
 * </tr>
 * </table>
 * 
 * <h4>Transfer response header format:</h4>
 * <table border="1">
 * <tr>
 * <td>response code(1)</td>
 * <td>reserved(127)</td>
 * </tr>
 * </table>
 * <br/>
 * 
 * See {@link TransferResponse} about transfer response code. <br/>
 * <br>
 * Please notes, all size unit is <b>Byte</b>.
 * 
 */
public final class ConnectionProtocol {
	public static final int HEADER_LENGTH = 128;// protocol+version+reserved.
	public static final int MAX_PACKET_DATA_LENGTH = 100 * 1204;
	public static final int MAX_PENDING_PACKETS = 1000;
	public static final int PACKET_HEADER_LENGTH = 5; // type+data
	// length+checksum
	public static final int RESPONSE_LENGTH = 128;// connection response
													// state+reserved
	public static final int TRANSFER_RESPONSE_LENGTH = 128;// transfer response
															// code+reserved
	public static final int TIME_OUT_INTERVAL = 600 * 1000;

}