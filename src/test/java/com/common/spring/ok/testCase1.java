package com.common.spring.ok;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.page.Pagination;
import com.app.model.Tb1;


/*
 每个方法之前会重新将数据库重新建一次，这样比较好，每个方法之间没有数据关联性
 */
public class testCase1 extends AbstractBaseTestCase {
	
	private BaseDao baseDao;
  
	@Before
	public void prepare() {
		baseDao = (BaseDao) applicationContext
		.getBean("eaDaoTarget");
		 Tb1 tb1=new Tb1();
		 tb1.setName("tom");
		 tb1.setPassword("123");
		 baseDao.create(tb1);
	}
	@Test
	public void create() {
	    Tb1 tb1=new Tb1();
	    baseDao.create(tb1);
	    assertEquals("加上初始化的1条，应该是2条", 2,  baseDao.find("from Tb1").size()); 
		assertEquals("加上初始化的1条，应该是2条", 2, baseDao.loadAll(Tb1.class).size()); 
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
		assertEquals("模糊查询", 1, baseDao.find("select tb1 from Tb1 tb1 where tb1.name  like ?", "%tom%").size()); 
		assertEquals("单个参数查询", 1, baseDao.find("select tb1 from Tb1 tb1 where tb1.name=?", "tom").size()); 
		assertEquals("多参数查询 ", 1, baseDao.find("select tb1 from Tb1 tb1 where tb1.name=? and tb1.password=?", new String[]{"tom", "123"}).size()); 
	}
	
	
	@Test
	public void page() throws Exception {
	    Tb1 tb1=new Tb1();
	    baseDao.create(tb1);
		Pagination pagination=new Pagination();
		pagination.setCurrentPage(0);
		pagination.setMaxSize(1);
		List tpltb1List = baseDao.page("from Tb1", pagination);
		assertEquals("", 1,  tpltb1List.size()); 
		pagination.setMaxSize(2);
		tpltb1List = baseDao.page("from Tb1", pagination);
		assertEquals("", 2,  tpltb1List.size()); 
	}	
	
	
	
}
