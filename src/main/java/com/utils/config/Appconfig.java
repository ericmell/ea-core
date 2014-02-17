package com.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Main framework class
 */
public class Appconfig {
	public static Properties configuration;
	public static final String confFileName = "config.txt";
	/**
	 * 读取配置文件 application.conf
	 */
	static {
		InputStream in = null;
		in = Appconfig.class.getResourceAsStream(confFileName);
		if (in == null) {
			in = Appconfig.class.getResourceAsStream("/" + confFileName);
			if (in == null) {
				ClassLoader loader = ClassLoader.getSystemClassLoader();
				if (loader != null) {
					URL url = loader.getResource(confFileName);
					if (url == null) {
						url = loader.getResource("/" + confFileName);
					}
					if (url != null) {
						try {
							in = url.openStream();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
					}
				}
			}
		}
		configuration = new Properties();
		try {
			configuration = IoUtils.readUtf8Properties(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getValue(String propertyName, String defaultValue)
			throws Exception {
		Properties prop = Appconfig.configuration;
		Enumeration enumeration = prop.propertyNames();
		return prop.getProperty(propertyName, defaultValue).toString();
	}
}
