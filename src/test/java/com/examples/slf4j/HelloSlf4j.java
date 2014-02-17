package com.examples.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * slf4j由log4j作者Ceki开发，逐步取代apahce commons logging。
 * logback由log4j作者Ceki开发，逐步取代log4j。 slf4j支持参数化的logger.error("帐号ID：{}不存在",
 * userId); 告别了if(logger.isDebugEnable()) 时代。
 * 另外logback的整体性能比log4j也较佳，hibernate等项目已经采用了slf4j。 slf4j和logback的使用
 * 
 * 1.如果日志的参数超过3个，需要写成
 * 
 * 
 * 
 * 
 * 
 * Java代码 Object[] params = {newVal, below, above};
 * logger.debug("Value {} was inserted between {} and {}.", params);
 * 
 * Object[] params = {newVal, below, above};
 * logger.debug("Value {} was inserted between {} and {}.", params);
 * 
 * 2.因为内部已优化，作者认为slf4j的logger不需要定义为static。
 * 
 * 3.可设置缓存后批量写日志文件(但服务器如果重启，可能会丢失未写到磁盘的记录)
 * 
 * 4.MDC，用Filter，将当前用户名等业务信息放入MDC中，在日志format定义中即可使用该变量。
 * 
 * 5.JMS Appender用于告警, DB Appender用于业务日志等可以使用插件，如生成Log代码的Eclipse插件Log4E。
 * 
 * 6.tomcat和glassfish中，设定日志路径为../logs/xxxx.log 都能将日志放入应用服务器本身的logs目录。
 * 
 * 
 * 
 * 
 * Slf4j : 全称为Simple Logging Facade for JAVA：
 * -------------------------------------------- 日志门面。
 * 是对不同日志框架提供的一个门面封装。可以在部署的时候不修改任何配置 即可接入一种日志实现方案。和commons-loging 应该有一样的初衷。个人
 * 感觉设从计上更好一些，没有commons 那么多潜规则。同时有两个额外特点： Slf4j 和log4j 作者都是同一个人。
 * 
 * Logback 号称在性能各方面有很多优势，也很诱人 如使用SLF4j 的时候为了避免冲突，一定要保障只有一种实现类jar 包在里面。
 * 
 * */
public class HelloSlf4j {
	static Logger logger = LoggerFactory
			.getLogger("hcom.sunrise.ueaac.base.common.log.slf4j.HelloSlf4j");

	public void testdebug() {
		logger.debug("开始 ");

	}

	public static void main(String[] args) {
		HelloSlf4j hello = new HelloSlf4j();
		// Logger logger = LoggerFactory.getLogger(HelloSlf4j.class);
		logger.debug("Hello ?");
		hello.testdebug();
	}
}
