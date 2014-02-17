package com.examples.jdklog;

import java.util.logging.Logger;

/**
 * 系统提供的缺省配置文件lib/logging.properties，那么也可以使用自己定义的配置文件。使用自定义配置文件的方法有很
 * 多种，比较方便的方法是通过定义系统属性值java.util.logging.config.file来设置自定义配置 文件。 例 如在执行 例2时，使用下
 * 面的命令： java -Djava.util.logging.config.file=mylogging.properties LevelTest
 * 
 * */
public class HelloLevel {

	private static Logger log = Logger.getLogger(HelloLevel.class.getName());

	public static void main(String[] args) {
		log.severe("severe level");// <= (1)
		log.warning("warning level");// <= (2)
		log.info("info level");// <= (3)
		log.config("config level");// <= (4)
		log.fine("fine level");// <= (5)
		log.finer("finer level");// <= (6)
		log.finest("finest level");// <= (7)
	}
}