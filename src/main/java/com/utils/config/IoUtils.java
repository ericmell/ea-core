package com.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * IO 文件输入输出工具类
 */
public class IoUtils {

	/**
	 * 
	 * 用UTF-8编码进行读取一个属性文件
	 * 
	 * @param is
	 *            Stream to properties file
	 * @return The Properties object
	 * @throws java.io.IOException
	 */
	public static Properties readUtf8Properties(InputStream is)
			throws IOException {
		Properties properties = new Properties();
		properties.load(is);
		for (Object key : properties.keySet()) {
			String value = properties.getProperty(key.toString());
			String goodValue = new String(value.getBytes("iso8859-1"), "utf-8");
			properties.setProperty(key.toString(), goodValue);
		}
		is.close();
		return properties;
	}

}
