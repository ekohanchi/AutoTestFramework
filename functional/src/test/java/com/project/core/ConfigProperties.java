package com.project.core;

import java.io.IOException;
import java.util.Properties;

public class ConfigProperties {
	private static String CONFIG_PROPERTIES = "config.properties";
	private static Properties properties = new Properties();
	private static String env = System.getProperty("env");
	
	static {
		try {
			properties.load(ConfigProperties.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES));
		} catch (IOException e) {
			// We should fail immediately, we can't work without config.properties
		      throw new RuntimeException(e);
		}
	}
	
	public String getEnv() {
		return env;
	}
	
	private String getEnvProperty(String name) {
		return properties.getProperty(env + "." + name);
	}
	
	public String getBaseSite() {
		return getEnvProperty("base.site");
	}
	
	public String getApiHost() {
		return getEnvProperty("api.host");
	}
}
