package com.app.old;
import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.page.Pagination;
import com.app.ea.api.InfEa;
import com.app.ea.model.User;
import com.app.model.Tb1;
public class SpringDaoTest {
	static Logger log = LoggerFactory.getLogger(SpringDaoTest.class);	
	protected static ApplicationContext applicationContext;
	public static BaseDao baseDao;
	public static InfEa infEa;

    static void create_tb1(String name,String password){
    	Tb1 tb1=new Tb1();
	    tb1.setName(name);
	    tb1.setPassword(password);
	    baseDao.create(tb1);
    }
	@BeforeClass
	public static void init() throws Exception {
		if (applicationContext == null) {
			try {
				String configFile = "spring.xml";
				applicationContext = new ClassPathXmlApplicationContext(
						configFile);
				baseDao = (BaseDao) applicationContext
						.getBean("eaDaoTarget");
				infEa = (InfEa) applicationContext.getBean("impEa");
				//System.err.println("红色 ");
                // 开始初始化数据
				create_tb1("tom","123");
				infEa.initData();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	@Test
	public void create() {
	    Tb1 tb1=new Tb1();
	    baseDao.create(tb1);
		assertTrue( baseDao.find("from Tb1").size()>=2);  //加上初始化的1条，应该是2条
		assertTrue( baseDao.loadAll(Tb1.class).size()>=2);

	}
	@Test
	public void load() {
		 Tb1 tb1=(Tb1)baseDao.loadById("Tb1", Long.parseLong("1"));
		 assertEquals("记录1的name是tom","tom" ,tb1.getName());
		 tb1=(Tb1)baseDao.loadById(Tb1.class, Long.parseLong("1"));
		 assertEquals("记录1的name是tom","tom" ,tb1.getName());
		 
		 tb1=(Tb1)baseDao.loadByFieldValue(Tb1.class, "name", "tom");
		 assertEquals("记录1的name是tom","tom" ,tb1.getName());
	}
	@Test
	public void find() {
		assertTrue(baseDao.find("select tb1 from Tb1 tb1 where tb1.name  like ?", "%tom%").size()>0);//"模糊查询"
		assertTrue(baseDao.find("select tb1 from Tb1 tb1 where tb1.name=?", "tom").size()>0); //"单个参数查询"
		assertTrue(baseDao.find("select tb1 from Tb1 tb1 where tb1.name=? and tb1.password=?", new String[]{"tom", "123"}).size()>0);//"多参数查询 "
		//assertEquals("模糊查询", 1, basedao.find("select tb1 from Tb1 tb1 where tb1.name  like ?", "%tom%").size()); 
		//assertEquals("单个参数查询", 1, basedao.find("select tb1 from Tb1 tb1 where tb1.name=?", "tom").size()); 
		//assertEquals("多参数查询 ", 1, basedao.find("select tb1 from Tb1 tb1 where tb1.name=? and tb1.password=?", new String[]{"tom", "123"}).size()); 
	}
	
	
	@Test
	public void page() throws Exception {
		Pagination pagination=new Pagination();
		pagination.setCurrentPage(0);
		pagination.setMaxSize(1);
		List tpltb1List = baseDao.page("from Tb1", pagination);
		assertEquals("", 1,  tpltb1List.size()); 
		pagination.setMaxSize(2);
		tpltb1List = baseDao.page("from Tb1", pagination);
		assertEquals("", 2,  tpltb1List.size()); 
	}	
	
	@Test
	public void user() {
	 
		assertTrue( baseDao.find("from User").size()>=2);  //加上初始化的1条，应该是2条
		  /*log.debug("admin user 个数:"+  baseDao.find("from User").size());
		log.debug("admin user:"+ baseDao.loadByFieldValue(User.class, "name", "admin"));
		Tb1 tb1=(Tb1)baseDao.loadByFieldValue(Tb1.class, "name", "tom");
		log.debug(tb1.getName());
		*/
	}
	
	@AfterClass
	public static void close() throws Exception {
		log.info("测试结束");
	}

}
