package com.common.spring.ok;

import org.hibernate.FlushMode;

import org.hibernate.Session;

import org.hibernate.SessionFactory;

import org.junit.After;

import org.junit.Before;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import org.springframework.orm.hibernate3.SessionFactoryUtils;

import org.springframework.orm.hibernate3.SessionHolder;

import org.springframework.transaction.support.TransactionSynchronizationManager;

/***
 * 
 * An abstract base class for TestCases.
 * All test cases should extend this class.
 */
/*
如果在Hibernate层采用lazy=true的话，有的时候会抛出LazyInitializationException，这时一种解决办法是用OpenSessionInViewFilter,但是如果通过main方法来运行一些测试程序，那么上述方法就没有用武之地了。这里提供了一种方法，来达到实现和OpenSessionInViewFilter相同作用的目的。这里的应用场景是采用JUnit4来编写测试用例。
JUnit4的好处是：采用annotation来代替反射机制，不必写死方法名.
首先添加一个abstract class(AbstractBaseTestCase.class), 做一些准备性的工作：
(可以看到@Before和@After两个annotation的作用相当于setUp()和tearDown()方法，但是，显然更灵活)

 * */
public class AbstractBaseTestCase {

	private SessionFactory sessionFactory;

	private Session session;

	protected ClassPathXmlApplicationContext applicationContext;



	@Before
	public void openSession() throws Exception {
		String configFile = "spring.xml";
		applicationContext = new ClassPathXmlApplicationContext(
				configFile);
		

		sessionFactory = (SessionFactory) applicationContext.getBean("eaSessionFactory");

		session = SessionFactoryUtils.getSession(sessionFactory, true);

		session.setFlushMode(FlushMode.COMMIT);

		TransactionSynchronizationManager.bindResource(sessionFactory,
				new SessionHolder(session));
		
	}

	@After
	public void closeSession() throws Exception {

		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.releaseSession(session, sessionFactory);

	}

}
