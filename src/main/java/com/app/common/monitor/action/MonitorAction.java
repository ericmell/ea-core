package com.app.common.monitor.action;


import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.struts2.ServletActionContext;
import org.hibernate.impl.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.app.common.base.action.BaseEaAction;
import com.app.ea.model.User;
import com.opensymphony.xwork2.ActionContext;
import com.utils.path.Path;

@Component("monitorAction")
public class MonitorAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(MonitorAction.class);
	public User user = new User();
	
	public static WebApplicationContext getWebApplicationContext(
			) {
		Map applicationMap = ActionContext.getContext().getApplication();

		if (applicationMap == null) {
			return null;
		}
		Object webApplicationContext = applicationMap
				.get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		if (webApplicationContext == null) {
			return null;
		}
		if (webApplicationContext instanceof RuntimeException) {
			throw (RuntimeException) webApplicationContext;
		}
		if (webApplicationContext instanceof Error) {
			throw (Error) webApplicationContext;
		}
		if (!(webApplicationContext instanceof WebApplicationContext)) {
			throw new IllegalStateException(
					"Root context attribute is not of type WebApplicationContext: "
							+ webApplicationContext);
		}

		return (WebApplicationContext) webApplicationContext;
	}
	public String push_ajax_jvm()  {
		rhs.put("runtime", Runtime.getRuntime());
		return "success";
	}	
	public String push_ajax_database()  {
		BasicDataSource basicDataSource=(BasicDataSource)getWebApplicationContext().getBean("eaDataSource");
		rhs.put("database",basicDataSource);
		return "success";
	}		
	
	public String home() throws Exception {
		rhs.put("runtime", Runtime.getRuntime());
		WebApplicationContext webApplicationContext=getWebApplicationContext();
		System.out.println("getApplicationName="+webApplicationContext.getApplicationName());
		System.out.println("getBeanDefinitionCount="+webApplicationContext.getBeanDefinitionCount());
		System.out.println("getBeanDefinitionNames="+webApplicationContext.getBeanDefinitionNames());
		System.out.println("getClassLoader="+webApplicationContext.getClassLoader());
		System.out.println("getDisplayName="+webApplicationContext.getDisplayName());
		System.out.println("getEnvironment="+webApplicationContext.getEnvironment());
		System.out.println("getId="+webApplicationContext.getId());
		System.out.println("getStartupDate="+webApplicationContext.getStartupDate());
		System.out.println("getServletContext="+webApplicationContext.getServletContext());
		
		rhs.put("spring_beans",webApplicationContext.getBeanDefinitionNames());
		rhs.put("servlet_context",webApplicationContext.getServletContext());
		
		BasicDataSource basicDataSource=(BasicDataSource)webApplicationContext.getBean("eaDataSource");
		
		
		SessionFactoryImpl  eaSessionFactory=(SessionFactoryImpl)webApplicationContext.getBean("eaSessionFactory");
		rhs.put("eaSessionFactory",eaSessionFactory);
		
		/*
		
		for (Iterator iterator = eaSessionFactory.getConfiguration().getCollectionMappings(); iterator.hasNext();) {
		//	type type = (type) iterator.next();
			System.out.println(iterator.next());
		}
		*/
		
		rhs.put("database",basicDataSource);
		rhs.put("webApplicationContext",webApplicationContext);
		rhs.put("startTime",new Date(webApplicationContext.getStartupDate()));
		Enumeration<String> e= webApplicationContext.getServletContext().getInitParameterNames();
		ArrayList  webxmlList=new ArrayList();
		while(e.hasMoreElements()){
 			String paraString=e.nextElement();
 			webxmlList.add(paraString+"="+webApplicationContext.getServletContext().getInitParameter(paraString));
		//	System.out.println(paraString+"="+webApplicationContext.getServletContext().getInitParameter(paraString));
		}
		rhs.put("web_xml_para",webxmlList);
		
		
		
		/*
		Enumeration<Servlet> eservlet= webApplicationContext.getServletContext().getServlets();
	
		while(eservlet.hasMoreElements()){
			Servlet servlet=eservlet.nextElement();
 			//webxmlList.add(paraString+"="+webApplicationContext.getServletContext().getInitParameter(paraString));
			System.out.println("---"+servlet.getServletInfo());
		}
		
		Enumeration<String> es= webApplicationContext.getServletContext().getServletNames();
		ArrayList  webservletList=new ArrayList();
		while(es.hasMoreElements()){
 			String paraString=es.nextElement();
 			System.out.println(paraString+"="+paraString);
		}
		*/

		
		Map  hashmap_system=new HashMap();
		for (Iterator iterator = System.getenv().keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			hashmap_system.put(key, System.getenv().get(key));
		}
		hashmap_system.put("classpath", System.getProperty("java.class.path"));
		hashmap_system.put("spring-version",Path.getLocation( Class.forName("org.springframework.orm.hibernate3.support.OpenSessionInViewFilter")));
		hashmap_system.put("hibernate-version",Path.getLocation( Class.forName("org.hibernate.dialect.H2Dialect")));
		hashmap_system.put("struts-version",Path.getLocation( Class.forName("org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter")));
		hashmap_system.put("freemarker-version",Path.getLocation( Class.forName("freemarker.ext.servlet.FreemarkerServlet")));
		hashmap_system.put("h2-version",Path.getLocation( Class.forName("org.h2.Driver")));
		hashmap_system.put("servlet-version",getServletVersion());
		hashmap_system.put("xml-sax-parser-version", getXmSAXlParserName());
		try{
		hashmap_system.put("jetty-version", Path.getLocation( Class.forName("org.eclipse.jetty.server.Server")));
			
		} catch (Exception excetion) {
			hashmap_system.put("jetty-version", "");
		}
		hashmap_system.put("database-filename", System.getProperty("online", "db-${online}.properties"));
		
		
		
		rhs.put("hashmap_system",hashmap_system);
    	return "success";
	}
       //servlet版本
	  public String getServletVersion() {
		    ServletContext context=ServletActionContext.getServletContext();
		    
	        int major = context.getMajorVersion();
	        int minor = context.getMinorVersion();
	        return "server info:"+ context.getServerInfo()+" servlet version:"+Integer.toString(major) + '.' + Integer.toString(minor);
	    }
	  /**
	     * what parser are we using.
	     * @return the classname of the parser
	     */
	    private String getXmSAXlParserName() {
	        SAXParser saxParser = getSAXParser();
	        if (saxParser == null) {
	            return "Could not create an XML Parser";
	        }

	        // check to what is in the classname
	        String saxParserName = saxParser.getClass().getName();
	        return saxParserName;
	    }
	    /**
	     * Create a JAXP SAXParser
	     * @return parser or null for trouble
	     */
	    private SAXParser getSAXParser() {
	        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	        if (saxParserFactory == null) {
	            return null;
	        }
	        SAXParser saxParser = null;
	        try {
	            saxParser = saxParserFactory.newSAXParser();
	        } catch (Exception e) {
	        }
	        return saxParser;
	    }

}
