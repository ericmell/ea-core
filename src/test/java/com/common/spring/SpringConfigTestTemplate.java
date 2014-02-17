package com.common.spring;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ByteArrayResource;

public class SpringConfigTestTemplate {
	public static XmlBeanFactory createXmlBeanFactory(String config) {
		String completeConfig = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<beans xmlns=\"http://www.springframework.org/schema/beans\" "
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xmlns:aop=\"http://www.springframework.org/schema/aop\" "
				+ "xmlns:tx=\"http://www.springframework.org/schema/tx\" "
				+ "xsi:schemaLocation=\""
				+ "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd "
				+ "http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.0.xsd "
				+ "http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.0.xsd\">"
				+ config + "</beans>";
		System.out.println(completeConfig);
		XmlBeanFactory factory = new XmlBeanFactory(new ByteArrayResource(
				completeConfig.getBytes()));
		return factory;

	}
}
