package com.examples.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 
 * http://localhost/maven-doc/ueaac-base-common/
 * 
 * logback 另外logback的整体性能比log4j也较佳，hibernate等项目已经采用了slf4j。 能自动加载配置文件；多个
 * JVM写一个日志文件， 或其他 I/O 错误时不影响程序执行； 配置文件 中加入条 件控制； 强大的日 志过滤；
 * 更强的日志切分功能；自动压缩、删除日志文件； 异常栈中更多的数据信息 logback-access 让你方便的访问日志信息，如通过 http 的方式
 * Slf4j 和log4j 作者都是同一个人。Slf4j默认会使用jdk自带的方案实现.
 * 
 * */
public class LogbackDemo {
	private static Logger log = LoggerFactory.getLogger(LogbackDemo.class);

	public static void main(String[] args) {
		log.trace("======trace");
		log.debug("======debug");

		log.info("======info");
		log.warn("======warn");
		log.error("======error");
		log.debug("======yes");
		log.debug("======no");
		String name = "Aub";
		String message = "3Q";
		String[] fruits = { "apple", "banana" };
		// logback提供的可以使用变量的打印方式，结果为"Hello,Aub!"
		log.info("Hello,{}!", name);
		// 可以有多个参数,结果为“Hello,Aub! 3Q!”
		log.info("Hello,{}!   {}!", name, message);
		// 可以传入一个数组，结果为"Fruit:  apple,banana"
		log.info("Fruit:  {},{}", fruits);
	}
}
