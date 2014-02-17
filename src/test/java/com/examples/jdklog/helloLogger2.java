package com.examples.jdklog;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class helloLogger2 {
	public static void main(String[] args) throws IOException {
		Logger log = Logger.getLogger("lavasoft");
		log.setLevel(Level.INFO);
		Logger log1 = Logger.getLogger("lavasoft");
		System.out.println(log == log1); // true
		Logger log2 = Logger.getLogger("lavasoft.blog");
		// log2.setLevel(Level.WARNING);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		log.addHandler(consoleHandler);
		FileHandler fileHandler = new FileHandler("C:/testlog%g.log");
		fileHandler.setLevel(Level.INFO);
		log.addHandler(fileHandler);
		log.info("aaa");
		log2.info("bbb");
		log2.fine("fine");
	}
}
