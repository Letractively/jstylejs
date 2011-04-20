/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.rayson.util;

import java.util.logging.Logger;

/**
 *
 * @author Nick Zhang
 */
public class Log {

	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static Logger getLogger() {
		return LOGGER;
	}
}
