package com.examples;

import java.util.Enumeration;
import java.util.Properties;

public class SystemProperties {
	/** 得到系统中所有属性的名称 */
	public static void main(String[] args) {
		Properties pro = System.getProperties();

		Enumeration e = pro.propertyNames();

		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			System.out.println(key + "=" + pro.getProperty(key));
		}

	}

}
