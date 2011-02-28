package org.rayson.util;

import java.util.logging.Logger;

public class Log {

	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static Logger getLogger() {
		return LOGGER;
	}
}
