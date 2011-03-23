package org.rayson.transport.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.rayson.annotation.Config;

@Config
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerConfig {
	public short portNumber() default TransportServer.PORT_NUMBER;
}
