package com.mcredit.alfresco.utils;

import java.io.IOException;
import java.util.Properties;

public class Config {

	private static Properties config;
	
	static{
		config = new Properties();
	}

	public static Properties getConfig() {
		
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			config.load(classloader.getResourceAsStream("config.properties"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return config;
	}

}
