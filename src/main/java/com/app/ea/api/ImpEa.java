package com.app.ea.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.demo.InfDemo;
import com.app.ea.hsql.Hsql;
import com.app.ea.model.Organize;
import com.app.ea.model.Organizegroup;
import com.app.ea.model.Resource;
import com.app.ea.model.Role;
import com.app.ea.model.Rolegroup;
import com.app.ea.model.Smtp;
import com.app.ea.model.Systempara;
import com.app.ea.model.User;
import com.utils.mail.SendMail;


@Component("impEa") 
public class ImpEa implements InfEa  {
	

	private final Logger log = LoggerFactory.getLogger(ImpEa.class);
	protected BaseDao baseDao;
	protected InfDemo infDemo;
	
	
	
	
	
	@Override
	public List getOrganizeRootNods() {
		return baseDao.find(" from Organize where parent_id = null");

	}
	
	

	public InfDemo getInfDemo() {
		return infDemo;
	}
	@javax.annotation.Resource(name = "impItCompany")
	public void setInfDemo(InfDemo infDemo) {
		this.infDemo = infDemo;
	}

	@Override
	public List getRolegroupRootNods() {
		return baseDao.find(" from Rolegroup where parent_id = null");

	}

	

	public BaseDao getbaseDao() {
		return baseDao;
	}
	@javax.annotation.Resource(name = "eaDaoTarget")
	public void setbaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public List getAllUser() {
		System.out.println("查询所有的用户接口调用");
		ArrayList rootList = (ArrayList) baseDao.find(Hsql.All_USER);
		return rootList;
	}

	@Override
	public List getAllUserByOrganize(Organize organize) {
		ArrayList userList = new ArrayList();
		organize.getRoles();
		for (Iterator iterator = organize.getRoles().iterator(); iterator
				.hasNext();) {
			Role role = (Role) iterator.next();
			userList.addAll(role.getUsers());
		}
		return userList;
	}

	@Override
	public void roleAddUserById(String roleId, String userId) {
		Role role = (Role) baseDao.loadById("Role", Long.parseLong(roleId));
		User user = (User) baseDao.loadById("User", Long.parseLong(userId));
		role.getUsers().add(user);
		baseDao.update(role);
	}

	@Override
	public List getUserByRoleId(String roleId) {
		Role role = (Role) baseDao.loadById("Role", Long.parseLong(roleId));
		return new ArrayList(role.getUsers());
	}

	public void resetPassword(String account, String password) {
		User user = getUserbyAccount(account);
		user.setPasswd(password);
		baseDao.update(user);

	}

	/* 删除用户 */
	public void deleteUser(User user) {
		baseDao.delete(user);
	}

	/* 更新用户，外部系统更新 */
	public void updateOjbect(Object object) {
		baseDao.update(object);
	}

	// public String create_organizegroup_organize_relation() {
	public void putOrganizeToOrganizegroup(Organize organize,
			Organizegroup organizegroup) {
		if (organizegroup.getOrganizes().contains(organize)) {
			organizegroup.getOrganizes().remove(organize);
		} else {
			organizegroup.getOrganizes().add(organize);
		}
		baseDao.update(organizegroup);

	}

	public void putUserToRole(String userId, String roleId) {
		Role role = (Role) baseDao.loadById("Role", Long.parseLong(roleId));
		User user = (User) baseDao.loadById("User", Long.parseLong(userId));
		if (role.getUsers().contains(user)) {
			role.getUsers().remove(user);
		} else {
			role.getUsers().add(user);
		}
		baseDao.update(role);
	}

	public Organize getUserFirestOrgNameByOrgGroup(long userId,
			String orggroupname) {
		User user = (User) baseDao.loadById("User", userId);
		List<Organize> result_organizes = new ArrayList();
		for (Iterator iterator = user.getRoles().iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			for (Iterator iterator2 = role.getOrganizes().iterator(); iterator2
					.hasNext();) {
				Organize organize = (Organize) iterator2.next();
				digui_get_department_by_organize(organize, result_organizes,
						orggroupname);
			}
		}
		if (result_organizes.size() > 0)
			return (Organize) result_organizes.get(0);
		else {
			return null;
		}
	}

	public void digui_get_department_by_organize(Organize organize,
			List<Organize> result_organizes, String orggroupname) {
		Set organizegroups = organize.getOrganizegroups();
		for (Iterator iterator2 = organizegroups.iterator(); iterator2
				.hasNext();) {
			Organizegroup organizegroup = (Organizegroup) iterator2.next();
			if (organizegroup.getAlias().equals(orggroupname)) {
				result_organizes.add(organize);
			}
		}
		if (organize.getParentModel() != null) {
			digui_get_department_by_organize(organize.getParentModel(),
					result_organizes, orggroupname);
		} else {
			return;
		}

	}

	@Override
	public Role userToRole(String roleId, String userId) {
		Role role = (Role) baseDao.loadById("Role", Long.parseLong(roleId));

		if (!userId.equals("")) {
			User user = (User) baseDao.loadById("User", Long.parseLong(userId));
			if (role.getUsers().contains(user)) {
				role.getUsers().remove(user);
			} else {
				role.getUsers().add(user);
			}
		}
		baseDao.update(role);
		return role;
	}

	/**
	 * 显示不同的错误
	 * */
	@Override
	public String checkLogin(String account, String password) {
		try {

			User user = (User) baseDao.loadByFieldValue(User.class, "account",
					account);
			if (user != null && user.getPasswd() != null
					&& user.getPasswd().toString().equals(password)) {
				return "0000";// 成功
			}
			return "0001"; // 失败
		} catch (Exception e) {
			return "0001"; // 失败
		}
	}

	@Override
	public User getUserbyAccount(String account) {
		return (User) baseDao.loadByFieldValue(User.class, "account", account);
	}

	public User getUserbyName(String name) {
		return (User) baseDao.loadByFieldValue(User.class, "name", name);
	}

	public BaseModel getBaseModelByAlias(String beanname, String alias) {
		String sql = "select o from " + beanname + " o where o.alias='" + alias
				+ "'";
		log.debug(sql);
		List resultList = baseDao.find(sql);
		if (resultList.size() > 0)
			return (BaseModel) resultList.get(0);
		else
			return null;
	}

	@Override
	public List getOrganizegroupRootNods() {
		return baseDao.find(" from Organizegroup where parent_id = null");
	}

	@Override
	public List getResourceRootNods() {
		return baseDao.find(" from Resource where parent_id = null");
	}

	@Override
	public String updateOrganizeRoleList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAllOrganizegroup() {
		ArrayList organizegroupList = (ArrayList) baseDao
				.find("from Organizegroup");
		return organizegroupList;
	}

	public Organize getOrganizeByAlias(String alias) throws Exception {
		String sql = "select o from Organize o where o.alias='" + alias + "'";
		List ls = baseDao.find(sql);
		if (ls.equals(null) || ls.size() == 0) {
			throw new Exception(alias + "：does not exit!");
		}
		Organize org = (Organize) ls.get(0);
		return org;
	}
	/*
	 * 该角色有父，但是该角色的父的组织不属于当前组织
	 * 如果父的组织也是该组织，
	*/
    public boolean ifParentRoleInSameOrganize(Role role,Organize organize){
    	/*	
    	while(role.getParentModel()!=null){
    		//if(role.getOrganizes().contains(organize)&&!(role.getParentModel().getOrganizes().contains(organize))){
    		if(!(role.getParentModel().getOrganizes().contains(organize))){
	    		return false;
	    	}
	    	role=role.getParentModel();
    	}
    	*/
    	if((role.getParentModel()!=null)&&!(role.getParentModel().getOrganizes().contains(organize))){
    		return false;
    	}
    	return true;
    }
    
    /**
     * 当我们点击组织时，要显示的根角色有2种
     * 1.正常点一个organize,该organize下的角色
     * 1.1。该orgnaize下创建的角色的角色，且父为空（因为在organize下面创建的角色已经建立的父子关系）
     * 1.2。该orgnaize下创建的角色的角色，且父不为空，而且父的角色的组织不属于该组织，则证明该角色也是根的
       
       2。该组织的子节点下的角色，如果没有设置关系的角色，也要列出来
       
     
     * */
	public  void getOrganizeRootRoleList(Long id,
			List<Role> result_root_Role) {
		Organize organize = (Organize) baseDao.loadById("Organize", id);
		List listRole = baseDao
				.find(
						"select  role from Role role left join role.organizes organize where organize.id=?",
						id);
		//第1种情况，该组织架构创建的角色
		for (Iterator iterator = listRole.iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			//角色的已经是organize; 比如组员的父不是空，但是已经//||(role.getParentModel()!=null&&!ifIsUnderOrganize(role,organize)
			if(role.getParentModel()==null){
				log.debug("父为空 加入："+role.getName()+ifParentRoleInSameOrganize(role,organize));
				result_root_Role.add(role);
			}else{
				//如果父不属于空，父不是此organize的
				if(!ifParentRoleInSameOrganize(role,organize)){
					result_root_Role.add(role);
				}
			}
		}
		//第2种情况, 这里应该是个递归，下面的做法，只是求了一层
		diguiGetRoleList(organize,result_root_Role);
		/*
		for (Iterator iterator =organize.getChildOrganizes().iterator(); iterator
				.hasNext();) {
			Organize suborganize = (Organize) iterator.next();
		    for (Iterator iterator2 = suborganize.getRoles().iterator(); iterator2.hasNext();) {
				Role role = (Role) iterator2.next();
				if(role.getParentModel()==null){
					result_root_Role.add(role);
				}			
			}
			
		}
		*/
	}
	public void diguiGetRoleList(Organize organize,
			List<Role> result_root_Role) {
		log.debug("递归:"+organize.getName());
		for (Iterator iterator =organize.getChildOrganizes().iterator(); iterator
				.hasNext();) {
			Organize suborganize = (Organize) iterator.next();
		    for (Iterator iterator2 = suborganize.getRoles().iterator(); iterator2.hasNext();) {
				Role role = (Role) iterator2.next();
				if(role.getParentModel()==null){
					result_root_Role.add(role);
				}			
				
			}
		    if(suborganize.getChildOrganizes()!=null) diguiGetRoleList(suborganize,result_root_Role);
		}
		
	}
	public List getOrganizeRootRoleList(String id) throws Exception {
		List<Role> result_role = new ArrayList();
		getOrganizeRootRoleList(Long.parseLong(id),result_role);;
		return result_role;
	}	
	public List getOrganizeByOrganizegroupAlias(String alias) throws Exception {
		List organizelist = new ArrayList();
		List<Organizegroup> list = baseDao.find(
				"from Organizegroup organizegroup where alias = ?", alias);
	
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Organizegroup organizegroup = (Organizegroup) iterator.next();
			log.debug("组织的类别：" + organizegroup.getName());
			organizelist = baseDao
					.find(
							"select organize from Organize organize left join organize.organizegroups organizegroup where   organizegroup.id =? ",
							organizegroup.getId());

		}
		return organizelist;
	}

	public Organize digui_get_department_by_organize(Organize organize,
			String organizegroupAlias) {

		Set organizegroups = organize.getOrganizegroups();
		for (Iterator iterator2 = organizegroups.iterator(); iterator2
				.hasNext();) {
			Organizegroup organizegroup = (Organizegroup) iterator2.next();

			if (organizegroup.getAlias().equals(organizegroupAlias)) {
				return organize;
			}
		}
		if (organize.getParentModel() != null) {
			digui_get_department_by_organize(organize.getParentModel(),
					organizegroupAlias);
		}
		return null;
	}

	public Organize getOrganzieOfUserByOrganizeGroupAlias(long userId,
			String organizegroupAlias) {
		User user = (User) baseDao.loadById("User", userId);
		for (Iterator iterator = user.getRoles().iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			for (Iterator iterator2 = role.getOrganizes().iterator(); iterator2
					.hasNext();) {
				Organize organize = (Organize) iterator2.next();
				for (Iterator iterator3 = organize.getOrganizegroups()
						.iterator(); iterator3.hasNext();) {
					Organizegroup organizegroup = (Organizegroup) iterator3
							.next();
					if (organizegroup.getAlias().equals(organizegroupAlias)) {
						return organize;
					}
				}
			}
		}
		return null;

	}

	public void digui_get_all_department_by_organize(Organize organize,
			List<Organize> result_organizes, String orggroupname) {

		Set organizegroups = organize.getOrganizegroups();
		for (Iterator iterator2 = organizegroups.iterator(); iterator2
				.hasNext();) {
			Organizegroup organizegroup = (Organizegroup) iterator2.next();
			if (organizegroup.getAlias().equals(orggroupname)) {
				result_organizes.add(organize); // 如果角色的组织类型是"DW"的，就得到该部门
			}
		}
		if (organize.getParentModel() != null) {
			digui_get_all_department_by_organize(organize.getParentModel(),
					result_organizes, orggroupname);
		} else {
			return;
		}

	}

	/** 跟组织架构分组的别名求部门 */
	public List<Organize> getUserAllOrganizeByOrganizegroupAlias(long userId,
			String organizegroupAlias) {
		User user = (User) baseDao.loadById("User", userId);
		List<Organize> result_organizes = new ArrayList();
		for (Iterator iterator = user.getRoles().iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			for (Iterator iterator2 = role.getOrganizes().iterator(); iterator2
					.hasNext();) {
				Organize organize = (Organize) iterator2.next();
				for (Iterator iterator3 = organize.getOrganizegroups()
						.iterator(); iterator3.hasNext();) {
					Organizegroup organizegroup = (Organizegroup) iterator3
							.next();
					if (organizegroup.getAlias().endsWith(organizegroupAlias)) {
						result_organizes.add(organize);
					}
				}

				/*
				 * digui_get_all_department_by_organize(organize,
				 * result_organizes, orggroupname);
				 */
			}
		}
		return result_organizes;
	}

	public User getUserByID(Long userID) {
		User user = (User) baseDao.loadById("User", userID);
		return user;
	}

	public Set getAllVisibleResource(String userId) throws Exception {

		User user = (User) baseDao.loadById("User", Long.parseLong(userId));
		Set resourceSet = new HashSet();
		log.debug(user.getName() + user.getRoles().size());
		// 获取用户角色和角色组所拥有的资源
		for (Iterator iterator = user.getRoles().iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			for (Iterator iterator2 = role.getRolegroups().iterator(); iterator2
					.hasNext();) {
				Rolegroup rolegroup = (Rolegroup) iterator2.next();
				for (Iterator iterator3 = rolegroup.getResources().iterator(); iterator3
						.hasNext();) {
					Resource resource = (Resource) iterator3.next();
					resourceSet.add(resource);
				}
			}
			for (Iterator iterator4 = role.getResources().iterator(); iterator4
					.hasNext();) {
				Resource resource = (Resource) iterator4.next();
				resourceSet.add(resource);
			}
		}
		// 获取用户是否拥有的资源
		for (Iterator iterator5 = user.getResources().iterator(); iterator5
				.hasNext();) {
			Resource resource = (Resource) iterator5.next();
			resourceSet.add(resource);
		}
		// 获取所有没有被授权的资源，即公共资源，默认公开
		// List resources = infResourceService.findAllRoot();
		
		
		List resources =baseDao.find(" from Resource r where r.id  not in (select r.id from Resource r  join r.users ) and  r.id not  in (select r.id from Resource r  join r.roles ) and r.id not in (select r.id from Resource r  join r.rolegroups ) ");
		
		/*
		List resources = baseDao.find(" from Resource");
		for (Iterator iterator6 = resources.iterator(); iterator6.hasNext();) {
			Resource resource = (Resource) iterator6.next();
			if (resource.getRolegroups().size() <= 0
					&& resource.getRoles().size() <= 0
					&& resource.getUsers().size() <= 0) {
				resourceSet.add(resource);
			}
		}
		*/
		return resourceSet;
	}

	 /*
	 
		sendMail.send("gscsystem@163.com", "tom.ling@ericsson.com", "", "",
				"from 163 用户发的", "test这是正文", null);
			
		 String from, String to, String cc, String bcc,
			String subject, String text, String[] filename
		 */
	@Override
	public void sendMail(String to, String cc, String bcc, String subject,
			String text, String[] filename) {
		ArrayList smtpList = (ArrayList) baseDao.find("from Smtp");
		for (Iterator iterator = smtpList.iterator(); iterator.hasNext();) {
			Smtp smtp = (Smtp) iterator.next();
			log.debug(smtp.getHost()+smtp.getPort()+smtp.getAccount()+smtp.getPasswd());
			try {
				SendMail sendMail = new SendMail();
				sendMail.connect(smtp.getHost(), smtp.getAccount(), smtp.getPasswd(), smtp.getPort());
				sendMail.send(smtp.getSender(), to, cc, bcc,subject,text, null);
				sendMail.close();
			} catch (Exception e) {
				log.debug("发送失败，重新服务器尝试");
				e.printStackTrace();
				continue;
			}
		}
		
	}

	@Override
	public List getAllRoles() {
		return baseDao.find(" from Role where parent_id = null");
	}

	@Override
	public User createUser(String account, String password) {
		User user = new User();
		user.setAccount(account);
		user.setPasswd(password);
		user.setName(account);
		baseDao.create(user);
		return user;
	}




	// 根据姓名 更新身份证、生日、入职日期
	public void update_user(Long id, String inentifyCard, String birthDate,
			String ruzhiDate) throws Exception {
		User user = (User) baseDao.loadById("User", id);
		user.setIdentityCard(inentifyCard);
		user.setBirthDate(birthDate);
		user.setRollDate(ruzhiDate);
		baseDao.update(user);
	}

	

	public void initData() throws Exception {
	  	infDemo.initData();
	}

	@Override
	public List getAllResoucesByUserName(String username) {
		User user = (User) baseDao.loadByFieldValue(User.class, "name",
				username);
		List list = new ArrayList();
		if (username != "" && user != null) {
			for (Iterator iterator = user.getResources().iterator(); iterator
					.hasNext();) {
				com.app.ea.model.Resource resource = (com.app.ea.model.Resource) iterator
						.next();
				list.add(resource.getName());
			}
			for (Iterator iterator = user.getRoles().iterator(); iterator
					.hasNext();) {
				Role role = (Role) iterator.next();
				for (Iterator iterator2 = role.getResources().iterator(); iterator2
						.hasNext();) {
					com.app.ea.model.Resource resource2 = (com.app.ea.model.Resource) iterator2
							.next();
					if (!list.contains(resource2.getName())) {
						list.add(resource2.getName());
					}
				}
				for (Iterator iterator3 = role.getRolegroups().iterator(); iterator3
						.hasNext();) {
					Rolegroup rolegroup = (Rolegroup) iterator3.next();
					for (Iterator iterator2 = rolegroup.getResources()
							.iterator(); iterator2.hasNext();) {
						com.app.ea.model.Resource resource = (com.app.ea.model.Resource) iterator2
								.next();
						if (!list.contains(resource.getName())) {
							list.add(resource.getName());
						}
					}
				}
			}
		}
		return list;
	}

	@Override
	public HashMap getParaMap() {
		HashMap paraMap=new HashMap();
		List root= baseDao.find(" from Systempara where parent_id = null");
		for (Iterator iterator = root.iterator(); iterator.hasNext();) {
			Systempara systempara = (Systempara) iterator.next();
			HashMap paraMapItem=new HashMap();
            for (Iterator iterator2 = systempara.getChildSystemparas().iterator(); iterator2.hasNext();) {
				Systempara systemparaItem = (Systempara) iterator2.next();
				paraMapItem.put(systemparaItem.getKey(), systemparaItem.getValue());
			}
            paraMap.put(systempara.key, paraMapItem);
		}
		return paraMap;
	}

	@Override
	public void deleteRole(Role role) {
		role.setRolegroups(null);
		role.setOrganizes(null);
		role.setUsers(null);
		role.setParentModel(null);
		baseDao.update(role);
		baseDao.delete(role);
	}

	@Override
	public void deleteOrgainize(Organize organize) {
		for (Iterator iterator = organize.getRoles().iterator(); iterator.hasNext();) {
			Role role = (Role) iterator.next();
			role.setUsers(null);
			role.setRolegroups(null);
			role.setResources(null);
			baseDao.delete(role);
		}
		
		organize.setParentModel(null);
		organize.setOrganizegroups(null);
		baseDao.update(organize);
		baseDao.delete(organize);
	}

	
}
