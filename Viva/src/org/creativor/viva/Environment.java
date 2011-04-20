/**
 * Copyright © 2011 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */
package org.creativor.viva;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import org.creativor.rayson.util.Log;

/**
 *
 * @author Nick Zhang
 */
public final class Environment {
	private static Environment environment = new Environment();

	public static Environment getEnvironment() {
		return environment;
	}

	/**
	 * Get home directory of current Viva system environment.
	 * 
	 * @return
	 */
	public String getHomeDir() {
		return System.getProperty("user.dir");
	}

	/**
	 * Get log files directory.
	 * 
	 * @return
	 */
	public String getLogDir() {
		return getHomeDir() + File.separatorChar + "log";
	}

	/**
	 * Get configuration files directory.
	 * 
	 * @return
	 */
	public String getConfDir() {
		return getHomeDir() + File.separatorChar + "conf";
	}

	/**
	 * Set log file name. The new log file will using the formatter
	 * {@link SimpleFormatter}.
	 * 
	 * @param logFileName
	 *            Name of the log file, not including the path and the post fix
	 *            name.
	 * @throws SecurityException
	 * @throws IOException
	 */
	public void setupLogFile(String logFileName) throws SecurityException,
			IOException {
		String logFilePath = getLogDir() + File.separatorChar + logFileName
				+ ".log";
		FileHandler fileHandler = new FileHandler(logFilePath, false);
		fileHandler.setFormatter(new SimpleFormatter());
		Log.getLogger().addHandler(fileHandler);
		Log.getLogger().info("Set log file to" + logFilePath);
	}
}
