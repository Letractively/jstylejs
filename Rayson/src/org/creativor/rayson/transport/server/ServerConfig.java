/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.creativor.rayson.annotation.Config;

/**
 * 
 * @author Nick Zhang
 */
@Config
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerConfig {

	public int portNumber() default DEFAULT_PORT_NUMBER;

	public static final int DEFAULT_PORT_NUMBER = 6660;

	public int workerCount() default 4;
}
