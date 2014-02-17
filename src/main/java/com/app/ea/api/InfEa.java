package com.app.ea.api;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.model.Organize;
import com.app.ea.model.Organizegroup;
import com.app.ea.model.Role;
import com.app.ea.model.User;

public interface InfEa {

	public List getOrganizeRootRoleList(String id) throws Exception ;	

	//查询systepara表，构造参数map
	public HashMap getParaMap();
	
	/**
	 * 根据组织架构分类的别名，求的该类别下所有的组织节点， 比如： getOrganizeByOrganizegroupAlias("company")
	 * getOrganizeByOrganizegroupAlias("team")
	 * 
	 * */
	public List getOrganizeByOrganizegroupAlias(String alias) throws Exception;
	/**
	 可以用来求某个组织架构下的子节点,在选人模块中有用到
	 * */
	public Organize getOrganizeByAlias(String alias) throws Exception;

	public List getAllUser();


	public User getUserbyAccount(String account);

	public User getUserbyName(String name);

	public User createUser(String account, String password);

	public void deleteUser(User user);
	public void deleteRole(Role role);
	public void deleteOrgainize(Organize organize);
	public void updateOjbect(Object o);

	public void initData() throws Exception;

	public List getOrganizeRootNods();

	public List getRolegroupRootNods();

	public List getAllRoles();

	public List getOrganizegroupRootNods();

	public List getAllOrganizegroup();

	public List getResourceRootNods();

	public String checkLogin(String account, String password);

	public List getUserByRoleId(String roleId);

	public void roleAddUserById(String roleId, String userId);

	public Role userToRole(String roleId, String userId);

	public BaseModel getBaseModelByAlias(String beanname, String alias);

	public String updateOrganizeRoleList();

	public BaseDao getbaseDao();

	// public InfResourceService getInfResourceService();
	//
	// public InfSmtpService getInfSmtpService();

	public void sendMail(String to, String cc, String bcc, String subject,
			String text, String[] filename);
	
	
	/**
	 * 修改用户密码
	 * 
	 * @param user
	 *            用户对象
	 * @param newPwd
	 *            新密码
	 * @return
	 * @throws Exception
	 */
	public void resetPassword(String account, String password);

	public void putOrganizeToOrganizegroup(Organize organize,
			Organizegroup organizegroup);

	/**
	 * 根据用户ID和组织架构的类别的别名，求用户的组织， 逻辑：先求得用户所有的角色，就 能得到所在的组织，跟据组织的的类别，判断别名
	 * */
	public Organize getOrganzieOfUserByOrganizeGroupAlias(long userId,
			String organizegroupAlias);

	public List getUserAllOrganizeByOrganizegroupAlias(long userId,
			String organizegroupAlias);

	public Set getAllVisibleResource(String userId) throws Exception;

	Organize getUserFirestOrgNameByOrgGroup(long userId, String orggroupname);

	public List getAllResoucesByUserName(String username);

	List getAllUserByOrganize(Organize organize);

	public void putUserToRole(String userId, String roleId);
}
