package com.examples.classpath;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClassPath {

	static Logger logger = LoggerFactory.getLogger(TestClassPath.class);

	public static void main(String[] args) throws InterruptedException {
		logger.debug(TestClassPath.class.getResource("").toString()
				.substring(6));
		logger.debug(ClassLoader.getSystemResource("").toString());

		logger.debug(new File("/").getAbsolutePath());
	}
}
