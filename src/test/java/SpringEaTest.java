

import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.ea.api.InfEa;
import com.app.ea.model.Organize;
import com.app.ea.model.Organizegroup;
import com.app.ea.model.Resource;
import com.app.ea.model.Role;
import com.app.ea.model.Rolegroup;
import com.app.ea.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class SpringEaTest  {
	static Logger log = LoggerFactory.getLogger(SpringEaTest.class);
	@Autowired
	private BaseDao eaDaoTarget;
	@Autowired
	private InfEa infEa;

	public static final String RESOURCE_BY_USERID = "select r from Resource r  left join r.users u where   u.id =? ";
	public static final String RESOURCE_OF_ROLE_GROUP_BY_USERID = "select distinct res from Resource res left join res.rolegroups rolegroup left join rolegroup.roles role  left join role.users user where user.id=?";

	// 陈永雄 ID:55 要运行2个SQL语句
	@Test
	public void testLeftJoin() throws Exception {
		User user = (User) eaDaoTarget.loadById("User", Long.parseLong("55"));
		for (Iterator iterator5 = user.getResources().iterator(); iterator5
				.hasNext();) {
			Resource resource = (Resource) iterator5.next();
			System.out.println(resource.getName());
		}

	}
	
	@Test
	@Rollback
	public void testBaseDao() {
		System.out.println(eaDaoTarget);
		System.out.println("用户个数=" + eaDaoTarget.find("from User").size());
	}

	public void printResourceList(List listResource) {
		for (Iterator iterator5 = listResource.iterator(); iterator5.hasNext();) {
			Resource resource = (Resource) iterator5.next();
			System.out.println("获得资源：" + resource.getName());
		}
	}
	

	@Test
	public void testRESOURCE_BY_USERID() throws Exception {
		Long userid = 55l;
		User user = (User) eaDaoTarget.loadById("User", userid);
		log.debug(user.getName());
		// 获取用户角色和角色组所拥有的资源
		for (Iterator iterator = user.getRoles().iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			log.debug(role.getName());
			log.debug("============");
			for (Iterator iterator4 = role.getResources().iterator(); iterator4
					.hasNext();) {
				Resource resource = (Resource) iterator4.next();
				System.out.println("资源：" + resource.getName());
			}
			log.debug("============");
			for (Iterator iterator2 = role.getRolegroups().iterator(); iterator2
					.hasNext();) {
				Rolegroup rolegroup = (Rolegroup) iterator2.next();
				log.debug(rolegroup.getName());
			}
		}
		System.out.println("user可以访问的资源");
		List listResource = eaDaoTarget.find(RESOURCE_BY_USERID, 55l);
		printResourceList(listResource);
		System.out.println("role可以访问的资源");
		List listResourceRole = eaDaoTarget
				.find(
						"select distinct res from Resource res left join res.roles role  left join role.users user where user.id=?",
						55l);
		printResourceList(listResourceRole);
		System.out.println("rolegroup可以访问的资源");
		List listResourceRolegroup = eaDaoTarget
				.find(
						"select distinct res from Resource res left join res.rolegroups rolegroup left join rolegroup.roles role  left join role.users user where user.id=?",
						55l);
		printResourceList(listResourceRolegroup);
		System.out.println("默认可以访问的资源");
		List listResourceNull = eaDaoTarget
				.find("select  resource from Resource resource where resource not in ( select  res from Resource res left join res.rolegroups rolegroup left join rolegroup.roles role  )");
		printResourceList(listResourceNull);

	}

		
	
	@Test
	public void eaDaoTarget() {
		log.debug("用户个数=" + eaDaoTarget.find("from User").size());
	}

	@Test
	public void getAllUser() {
		log.debug("EA查出用户个数=" + infEa.getAllUser().size());
	}

	

	@Test
	@Rollback
	public void testEa() {
		System.out.println("EA查出用户个数=" + infEa.getAllUser().size());
	}

	@Test
	public void testEaGetAllUser() throws Exception {
		for (Iterator iterator = infEa.getAllUser().iterator(); iterator
				.hasNext();) {
			User user = (User) iterator.next();
			log.debug(user.getName());
		}

	}

	@Test
	public void testGetOrganizeByOrganizegroupAlias() throws Exception {
		for (Iterator iterator = infEa.getOrganizeByOrganizegroupAlias("team")
				.iterator(); iterator.hasNext();) {
			Organize organize = (Organize) iterator.next();
			log.debug(organize.getName());
		}

	}
	
	/**
	 取得组织，下所有的角色，循环组织的字
	 
	 * */
	@Test
	public void testGetOrganizeRootRoleList() throws Exception {
		List listRole= infEa.getOrganizeRootRoleList("17");
     
	/*	
		Organize organize = (Organize) eaDaoTarget.loadById("Organize", Long.parseLong("17"));
        log.debug(organize.getName());
        
		List listRole = eaDaoTarget
				.findbyhsql(
						"select  role from Role role left join role.organizes organize where organize.id=?",
						Long.parseLong("17"));
 		*/
        
	}

	@Test
	public void testGetAllOrganziegroup() throws Exception {
		for (Iterator iterator = infEa.getAllOrganizegroup().iterator(); iterator
				.hasNext();) {
			Organizegroup organizegroup = (Organizegroup) iterator.next();
			log.debug(organizegroup.getName());
		}

	}

	@Test
	public void testCheckUser() throws Exception {
		log.debug(infEa.checkLogin("wanghaiming", "abc123"));
	}

	@Test
	public void testGetBaseModelByAlias() throws Exception {
		com.app.ea.model.Resource resource = (com.app.ea.model.Resource) infEa
				.getBaseModelByAlias("Resource", "sys-assessment");
		log.debug(resource.getName());
		for (Iterator iterator = resource.getChildResources().iterator(); iterator
				.hasNext();) {
			com.app.ea.model.Resource resource1 = (com.app.ea.model.Resource) iterator
					.next();
			log.debug(resource1.getActionUrl());
		}
	}

	@Test
	public void testQuery() throws Exception {
		for (Iterator iterator = infEa.getOrganizeRootNods().iterator(); iterator
				.hasNext();) {
			Organize organize = (Organize) iterator.next();
			log.debug(organize.getName());
		}
		infEa.getOrganizeRootNods();
	}

	@Test
	public void testgetOrganzieOfUserByOrganizeGroupAlias() throws Exception {
		log.debug(infEa.getOrganzieOfUserByOrganizeGroupAlias(
				Long.parseLong("2"), "company").getName());

	}

	@Test
	public void testgetUserAllOrganizeByOrganizegroupAlias() throws Exception {

		List organize_list = infEa.getUserAllOrganizeByOrganizegroupAlias(
				Long.parseLong("6"), "project");

		for (Iterator iterator = organize_list.iterator(); iterator.hasNext();) {
			Organize organize = (Organize) iterator.next();
			log.debug(organize.getName());

		}

	}



	@Test
	public void testGetAllVisibleResource() throws Exception {
		/**/
		Set resourceSet = infEa.getAllVisibleResource("1");
		for (Iterator iterator3 = resourceSet.iterator(); iterator3.hasNext();) {
			com.app.ea.model.Resource resource = (com.app.ea.model.Resource) iterator3
					.next();
			log.debug(resource.getName());
		}

	}

	@Test
	public void testgetRoleUser() throws Exception {
		Role role = (Role) infEa.getbaseDao().loadById("Role",
				Long.parseLong("28"));
		log.debug(role.getName() + role.getUsers().size());

	}

	
	@Test
	public void testPerformer() throws Exception {
		infEa.getAllVisibleResource("2");

	}
	


	

}
