package com.examples.jdklog;

import java.util.logging.Logger;

/**
 * 测试的是jdk的日志
 * */
public class HelloLogWorld {
	private static Logger log = Logger.getLogger(HelloLogWorld.class.getName());

	public static void main(String[] args) {
		HelloLogWorld logWorld = new HelloLogWorld();
		log.info("Hello Logging World");
	}
}