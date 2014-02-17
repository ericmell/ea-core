package com.examples.jdklog;

import java.util.logging.Logger;

public class helloLogger1 {
	private static Logger log = Logger.getLogger(helloLogger1.class.getName());

	public static void main(String[] args) {
		helloLogger1 logWorld = new helloLogger1();
		log.info("Hello Logging World");
	}
}