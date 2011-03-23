package org.rayson.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.rayson.annotation.Config;
import org.rayson.transport.server.TransportServer;

@Config
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerConfig {
	public short portNumber() default TransportServer.PORT_NUMBER;
}
