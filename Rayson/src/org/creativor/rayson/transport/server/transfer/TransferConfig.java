/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.transport.server.transfer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.creativor.rayson.annotation.Config;

/**
 *
 * @author Nick Zhang
 */
@Config
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface TransferConfig {
	public int workerCount() default 4;
}
