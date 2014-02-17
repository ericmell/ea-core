package com.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.page.Pagination;
import com.app.ea.api.InfEa;
import com.app.ea.model.Resource;
import com.app.ea.model.User;
import com.app.model.Tb1;

import com.common.spring.ok.AbstractBaseTestCase;

/*
 每个方法之前会重新将数据库重新建一次，这样比较好，每个方法之间没有数据关联性
 */
public class BaseDaoTest extends AbstractBaseTestCase {
	static Logger log = LoggerFactory.getLogger(BaseDaoTest.class);
	private BaseDao baseDao;
	private InfEa infEa;
  
	@Before
	public void prepare() throws Exception {
		baseDao = (BaseDao) applicationContext.getBean("eaDaoTarget");
		infEa = (InfEa) applicationContext.getBean("impEa");
		infEa.initData();
	}
	
	@Test
	public void getAllVisibleResource() throws Exception {
		//log.debug("admin resource numbers:"+infEa.getAllVisibleResource("1").size());
    	//log.debug("test resource numbers:"+infEa.getAllVisibleResource("2").size());
		// assertEquals("user:admin is 8", 8,  infEa.getAllVisibleResource("1").size()); 
		 assertEquals("user:test is 0", 0,  infEa.getAllVisibleResource("2").size()); 
	
	}
	
	public void UserLeftJoin() throws Exception {
		List reslist =baseDao.find("select u from User u  left join u.resources ");
		for (Iterator iterator = reslist.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			log.debug(user.account);
			
		}
	}
	public void resourceLeftJoin() throws Exception {
		List reslist =baseDao.find("select r from Resource r  join r.users");
		for (Iterator iterator = reslist.iterator(); iterator.hasNext();) {
			Resource resource = (Resource) iterator.next();
			log.debug(resource.getName()+resource.getUsers().size());
		}
	}
	public void resourcenNotInLeftJoin() throws Exception {
		List reslist =baseDao.find(" from Resource r where r.id  in (select r.id from Resource r  join r.users )");
		log.debug("授权给用户的资源个数："+reslist.size());

		 reslist =baseDao.find(" from Resource r where r.id  in (select r.id from Resource r  join r.roles )");
		log.debug("授权给角色的资源个数："+reslist.size());
	
		reslist =baseDao.find(" from Resource r where r.id  in (select r.id from Resource r  join r.rolegroups )");
		log.debug("授权给角色组的资源个数："+reslist.size());

		
		reslist =baseDao.find(" from Resource r where r.id  not in (select r.id from Resource r  join r.users ) and  r.id not  in (select r.id from Resource r  join r.roles ) and r.id not in (select r.id from Resource r  join r.rolegroups ) ");
		log.debug("没有授权给三种媒体的资源个数："+reslist.size());

		
		/*
		for (Iterator iterator = reslist.iterator(); iterator.hasNext();) {
			Resource resource = (Resource) iterator.next();
			log.debug(resource.getName()+resource.getUsers().size());
		}
		*/
		
		//All_USER
		// "and u.id not in ( select u.id from User u  left join u.organizes org where  u.status="
		// + status_ok + " and org.id=? )";
		
		
		
	}
	
}
