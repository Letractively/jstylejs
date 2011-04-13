package org.creativor.viva.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import org.creativor.viva.Environment;

import com.google.gson.Gson;

public class ConfTool {
	private static ConfTool singleton = new ConfTool();
	private static final String CONF_FILE_POSTFIX = ".json";
	private Gson gson;
	private HashMap<Class<? extends Configuration>, Configuration> caches;

	private ConfTool() {
		try {
			caches = new HashMap<Class<? extends Configuration>, Configuration>();
			gson = new Gson();
		} catch (Throwable e) {
			throw new RuntimeException(
					"Initialize the configuration tool error", e);
		}
	}

	public static ConfTool getSingleton() {
		return singleton;
	}

	public static void main(String[] args) {
		ConfTool tool = ConfTool.getSingleton();
		try {
			Servants seeds = tool.getConfiguration(Servants.class);
			seeds = tool.getConfiguration(Servants.class);
			System.out.println(seeds.toString());
		} catch (LoadConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized <T extends Configuration> T getConfiguration(
			Class<T> confClass) throws LoadConfigException {
		Configuration configuration = caches.get(confClass);
		if (configuration == null) {
			String confFileName = confClass.getSimpleName().toLowerCase()
					+ CONF_FILE_POSTFIX;
			String confFilePath = Environment.getEnvironment().getConfDir()
					+ File.separator + confFileName;
			Reader reader;
			try {
				reader = new InputStreamReader(
						new FileInputStream(confFilePath));
				configuration = gson.fromJson(reader, confClass);
				// add to caches
				caches.put(confClass, configuration);
			} catch (Exception e) {
				throw new LoadConfigException(e);
			}
		}
		return (T) configuration;
	}
}