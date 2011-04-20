/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.rayson.apt;

public final class Constants {

	public static final String PROXY_ANNOTATION_NAME = "org.rayson.annotation.Proxy";

	public static final String RPCSERVICE_INTERFACE_NAME = "org.rayson.api.RpcService";

	public static final String PROXY_INTERFACE_NAME = "org.rayson.api.RpcProxy";

	public static final String Proxy_ANNOTATIONED_MUST_SERVICE = "Proxy annotationed type must implements "
			+ RPCSERVICE_INTERFACE_NAME;
	public static final String PROXY_MUST_BE_INTERFACE = "Proxy annotationed type must be an interface";

	public static final String SERVICE_MUST_EXTENDS_RPCSERVICE = "Protocol must be an interface that extends  "
			+ RPCSERVICE_INTERFACE_NAME;
	public static final String SESSION_INTERFACE_NAME = "org.rayson.api.Session";

	public static final String SERVICE_METHOD_FIRST_PARAMETER_MUST_BE_SESSION = "Service method first paramter's type must be "
			+ SESSION_INTERFACE_NAME;

	public static final String RPC_EXCEPTION_NAME = "org.rayson.exception.RpcException";

	public static final String PROXY_METHOD_MUST_THROWN_RPCEXCEPTION = "Proxy method must throws "
			+ RPC_EXCEPTION_NAME;

	public static final String SERVICE_METHOD_SHOULD_NOT_THROWN_RPCEXCEPTION = "Service method should not throws "
			+ RPC_EXCEPTION_NAME;

	public static final String CAN_NOT_FOUND_METHOD_IN_PROXY = "Canot found matched method in proxy interface";

	public static final String PROXY_METHOD_PARA_SHOULD_NOT_BE_SESSION = "Proxy method parameter should not be "
			+ SESSION_INTERFACE_NAME;

	public static final String PROXY_METHOD_RETURN_TYPE_NOT_MATCH_SERVICE = "Proxy method return type not match assoiated service method";

	public static final String PROXY_METHOD_EXCEPTION_NOT_MATCH = "Proxy method thrown exception can not find match in assoiated service method";
}
