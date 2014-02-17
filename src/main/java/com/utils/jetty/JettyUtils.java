package com.utils.jetty;

import java.net.ServerSocket;
import java.util.Properties;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyUtils {

	static Logger log = LoggerFactory.getLogger(JettyUtils.class);
	static public String url;

	public static void runWebapps(WebAppContext[] webAppContextArray, int port)
			throws Exception {
		Server server = new Server();
		Connector connector = new SelectChannelConnector();
		findFreePort(port);
		connector.setPort(port);
		server.setConnectors(new Connector[] { connector });
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		for (int i = 0; i < webAppContextArray.length; i++) {
			webAppContextArray[i]
					.setDefaultsDescriptor("src/main/resources/webdefault.xml");
		}
		contexts.setHandlers(webAppContextArray);
		server.setHandler(contexts);
		server.start();
		Runtime.getRuntime().exec(
				"cmd   /c   start  iexplore.exe http://localhost:" + port
						+ "/ea");

	}

	public static void runManyWeb(String[] contextArray, String[] webrootArray,
			int port) throws Exception {

		Server server = new Server();
		Connector connector = new SelectChannelConnector();
		findFreePort(port);
		connector.setPort(port);
		server.setConnectors(new Connector[] { connector });
		ContextHandlerCollection contexts = new ContextHandlerCollection();

		for (int i = 0; i < contextArray.length; i++) {
			WebAppContext webapp = new WebAppContext();
			webapp.setDefaultsDescriptor("src/main/resources/webdefault.xml");
			webapp.setContextPath(contextArray[i]);
			webapp.setWar(webrootArray[i]);
			contexts.addHandler(webapp);
		}

		server.setHandler(contexts);
		server.start();
		Runtime.getRuntime().exec(
				"cmd   /c   start  iexplore.exe http://localhost:" + port
						+ "/ea");

	}

	public static void runjetty8(String context, int port) throws Exception {
		Server server = new Server();
		Connector connector = new SelectChannelConnector();
		int lastPort = findFreePort(port);
		connector
				.setPort(Integer.getInteger("jetty.port", lastPort).intValue());
		server.setConnectors(new Connector[] { connector });

		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/" + context);
		webapp.setWar("./src/main/webapp");
		webapp.setDefaultsDescriptor("src/test/resources/webdefault.xml");
		server.setHandler(webapp);
		System.out.println("Web Server starting please waiting...");
		server.start();
		// server.join();
		url = "http://localhost:" + lastPort + "/" + context;
		Runtime.getRuntime().exec("cmd   /c   start  iexplore.exe " + url);
		Properties p = System.getProperties();
		System.out.println("db file:"
				+ p.getProperty("online", "db-${online}.properties"));

	}

	public static int findFreePort(int initPort) {
		ServerSocket tmpSocket = null;
		while (tmpSocket == null) {
			try {
				tmpSocket = new ServerSocket(initPort);
			} catch (Exception e) {

				initPort++;
				log.info(initPort + "is used, then try new port：" + initPort);
			}
		}
		return initPort;

	}

}
