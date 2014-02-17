package com.common.spring;


import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import com.app.common.spring.ssh.dao.BaseDao;
import com.app.model.Tb1;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class SpringJunit4Test  {
	static Logger log = LoggerFactory.getLogger(SpringJunit4Test.class);
	@Autowired
	static BaseDao eaDaoTarget;
	
	@BeforeClass
	public static void init() throws Exception {
	   //无法调用eaDaoTarget
	}	
	@Test
	@Rollback
	public void testBaseDao() {
	
	//	System.out.println("tb1表记录个数=" + eaDaoTarget.find("from Tb1").size());
	}


	

}
