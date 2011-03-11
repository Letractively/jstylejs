package org.rayson.apt;

public final class Constants {
	public static final String PROTOCOLS_ANNOTATION_NAME = "org.rayson.annotation.Protocols";
	public static final String RPCSERVICE_INTERFACE_NAME = "org.rayson.api.RpcService";
	public static final String RPCPROTOCOL_INTERFACE_NAME = "org.rayson.api.RpcProtocol";
	public static final String TYPE_MUST_ANNOTATIONED_SERVICE = "Protocols annotationed type must implements "
			+ RPCSERVICE_INTERFACE_NAME;
	public static final String TYPE_MUST_BE_INTERFACE = "Protocols annotationed type must be an interface";
	public static final String PROTOCOL_MUST_EXTENDS_RPCPROTOCOL = "Protocol must be an interface that extends  "
			+ RPCPROTOCOL_INTERFACE_NAME;
	public static final String SESSION_INTERFACE_NAME = "org.rayson.api.Session";
	public static final String FIRST_PARAMETER_MUST_BE_SESSION = "Service method first paramter's type must be "
			+ SESSION_INTERFACE_NAME;

}
