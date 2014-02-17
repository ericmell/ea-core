package com.examples;


import java.util.Enumeration;
import java.util.Properties;

import com.utils.config.Appconfig;
import com.utils.hardinfo.HardInfo;

import junit.framework.TestCase;
public class AppconfigSample {
	/**
	 * 得到系统中所有属性的名称
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Properties prop = Appconfig.configuration;
		Enumeration enumeration = prop.propertyNames();
		while (enumeration.hasMoreElements()) {
			String propertyName = enumeration.nextElement().toString();
			System.out.println(propertyName + ": "
					+ prop.getProperty(propertyName, "unknown "));
		}

	}


}