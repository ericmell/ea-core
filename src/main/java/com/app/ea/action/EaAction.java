package com.app.ea.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.hsql.Hsql;
import com.app.ea.model.Organize;
import com.app.ea.model.Organizegroup;
import com.app.ea.model.Role;
import com.app.ea.model.Rolegroup;
import com.app.ea.model.User;
import com.app.ea.model.Viewhistory;


import com.utils.cache.Cache;
import com.utils.file.FileProcessor;
import com.utils.time.TimeUtil;

import freemarker.template.SimpleHash;

@Component("eaAction")
@Scope("prototype")
public class EaAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(EaAction.class);
	//http://localhost:9090/ea/app/manager/ea_sort.do?object=Organize
	public String sort() throws Exception {
		List olist = baseDao.find(" from  "+getpara("object"));
		for (Iterator iterator = olist.iterator(); iterator
				.hasNext();) {
			Object o = (Object) iterator.next();
			try {
				Long sortNob = Long.parseLong(BeanUtils.getProperty(o, "sortNob"));
				
			} catch (Exception e) {
			    log.debug("sortNob排序字段为空");
				BeanUtils.setProperty(o, "sortNob", BeanUtils.getProperty(o, "id"));
			}	
		  
				String inputtime = BeanUtils.getProperty(o, "inputtime");
			 	if(inputtime==null){
			 		log.debug("createDate字段为空");
			 		BeanUtils.setProperty(o, "inputtime", TimeUtil.getTimeStr("yyyy-MM-dd hh-mm-ss"));
			 	}
						
			log.debug(BeanUtils.getProperty(o, "id")+"."+BeanUtils.getProperty(o, "name")+"."+BeanUtils.getProperty(o, "sortNob")+"."+BeanUtils.getProperty(o, "inputtime"));
			baseDao.update(o);
		}
		olist = baseDao.find(" from  "+getpara("object"));
		rhs.put("olist", olist);		
		return "success";
	}
	public String menuEaInit() {
		List organizeRootList = infEa.getOrganizeRootNods();
		rhs.put("organizeRootList", organizeRootList);
		List roleGroupRootList = infEa.getRolegroupRootNods();
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "menu_ea_init";
	}
	

	public String menu_relation() {
		List organizeRootList = infEa.getOrganizeRootNods();
		rhs.put("organizeRootList", organizeRootList);
		List roleGroupRootList = infEa.getRolegroupRootNods();
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";
	}

	
	public String update() throws Exception {
		try {
			
			common_update();
		} catch (Exception e) {
			rhs.put("info_type", "error");
			rhs.put("info", " 属性更新失败!");
			return "success";
		}
		return "success";
	}	
	
	
	
	public String menu_organize_view_v() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";

	}
	public String menu_organize_view_h() {
		List organizeRootList = infEa.getOrganizeRootNods();
		rhs.put("organizeRootList", organizeRootList);
		rhs.put("userList", infEa.getAllUser());
		return "success";
	}

	
	
		
	public String menu_mail_list() {
		List organizeRootList = infEa.getOrganizeRootNods();
		rhs.put("organizeRootList", organizeRootList);
		rhs.put("rolegroupRootList", infEa.getRolegroupRootNods());
		rhs.put("userList", infEa.getAllUser());
		return "success";
	}	
	
	public String send_email() {
		
		if(getpara("roleId")!=""){
			Role role=(Role)baseDao.loadById("Role",Long.parseLong(getpara("roleId")));
			
			rhs.put("userList", role.getUsers());
		}
		
		if(getpara("rolegroupId")!=""){
			Rolegroup Rolegroup=(Rolegroup)baseDao.loadById("Rolegroup",Long.parseLong(getpara("rolegroupId")));
		  rhs.put("userList", Rolegroup.allUserOfRolegroup());
		}
		return "success";
	}	
	public String select_userlist_by_organize() throws Exception {
		rhs.put("organizeRootList",infEa.getOrganizeRootNods());	
		return "success";	
	}	
	public String ajax_bat_create_role() throws Exception {
		String organizeId = getpara("organizeId");
		String rolegroupIdString = getpara("rolegroupIdString");
		log.debug("organizeId=" + organizeId);
		log.debug("rolegroupIdString=" + rolegroupIdString);
		String[] rolegroupId = rolegroupIdString.split("-");
		Organize organize = (Organize) baseDao.loadById("Organize",
				Long.parseLong(organizeId));
		for (int i = 0; i < rolegroupId.length; i++) {
			Rolegroup rolegroup = (Rolegroup) baseDao.loadById("Rolegroup",
					Long.parseLong(rolegroupId[i]));
			log.debug(rolegroup.getName());
			Role role = new Role();
			role.setName(organize.getName() + "-" + rolegroup.getName());
			role.setAlias(organize.getAlias() + "-" + rolegroup.getAlias());
			role.getOrganizes().add(organize);
			role.getRolegroups().add(rolegroup);
			baseDao.create(role);
		}
		rhs.put("info_type", "success");
		rhs.put("info", "'职位'创建成功!");
		ajax_organize_role_list();
		return "success";
	}

	public String delete_role() {
		String roleId = getpara("roleId");
		Role role = (Role) baseDao.loadById("Role", Long.parseLong(roleId));
		role.setOrganizes(null);
		role.setRolegroups(null);
		String organizeId = getpara("organizeId");
		baseDao.update(role);
		baseDao.delete(role);
		Organize organize = (Organize) baseDao.loadById("Organize",
				Long.parseLong(organizeId));
		rhs.put("organize", organize);
		return "success";
	}
	public String change_role_rank() throws Exception {
		common_change_rank(); 
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
		ajax_organize_role_list();
		return "success";
	}            
	public String change_role_delete() throws Exception {
		Role role = (Role) baseDao.loadById("Role",
				Long.parseLong(getpara("id")));
		/*
		Organize organize = (Organize) baseDao.loadById("Organize",
				Long.parseLong(getpara("organizeId")));
		log.debug("是否存在角色："+organize.getRoles().contains(role));
		organize.getRoles().remove(role);
		baseDao.update(organize);
        */
		role.setParentModel(null);
		role.setRolegroups(null);
		role.setUsers(null);
		role.setOrganizes(null);
		baseDao.delete(role);
		rhs.put("info_type", "success");
		rhs.put("info", "删除角色成功!");
		ajax_organize_role_list();
		return "success";
	}	
	
	public String ajax_role_delete() throws Exception {
		Role role = (Role) baseDao.loadById("Role",
				Long.parseLong(getpara("roleId")));
		role.setParentModel(null);
		role.setRolegroups(null);
		role.setUsers(null);
		role.setOrganizes(null);
		baseDao.delete(role);
		rhs.put("info_type", "success");
		rhs.put("info", "删除角色成功!");
		return "success";
	}		
	
	public String change_role_level() throws Exception {
		common_change_level();
		rhs.put("info_type", "success");
		rhs.put("info", "改变层级成功!");
		ajax_organize_role_list();
		return "success";
	}
	public String change_role_update() throws Exception {
		common_update();
		rhs.put("info_type", "success");
		rhs.put("info", "更新成功!");
		ajax_organize_role_list();
		return "success";
	}
	public String ajax_organize_role_list() throws Exception {
		String organizeId = getpara("organizeId");
		Organize organize = (Organize) baseDao.loadById("Organize",
				Long.parseLong(organizeId));
		List roleList=new ArrayList();
		roleList= infEa.getOrganizeRootRoleList(organizeId);
	    rhs.put("roleList", roleList);
	    //rhs.put("roleList", organize.getRoles());
		rhs.put("organize", organize);
		return "success";
	}

	public String menu_person_deploy_by_organize() {
		List organizeRootList = infEa.getOrganizeRootNods();
		rhs.put("organizeRootList", organizeRootList);
		rhs.put("userList", infEa.getAllUser());
		return "success";
	}

	public String menu_person_deploy() {
		List organizeRootList = infEa.getOrganizeRootNods();
		rhs.put("organizeRootList", organizeRootList);
		rhs.put("userList", infEa.getAllUser());
		return "success";
	}

	/*
	 * public String personDeployProject() { List organizeRootList =
	 * infEa.getOrganizeRootNods(); rhs.put("organizeRootList",
	 * organizeRootList); rhs.put("userList", infEa.getAllUser()); return
	 * "menu_person_deploy_project"; }
	 */
	public String ajaxUserToRole() {
		// infEa.roleAddUserById(getpara("roleId"),getpara("userId"));
		rhs.put("role", infEa.userToRole(getpara("roleId"), getpara("userId")));
		return "ajax_user_to_role";
	}
	
	
	public String ajaxOrganizegroupToRole() {
		// infEa.roleAddUserById(getpara("roleId"),getpara("userId"));
		rhs.put("role", infEa.userToRole(getpara("roleId"), getpara("userId")));
		return "ajax_user_to_role";
	}

	public String menu_organize_to_organizegroup() {
		List organizegroupAllList = infEa.getAllOrganizegroup();
		List organizeRootList = infEa.getOrganizeRootNods();
		rhs.put("organizeRootList", organizeRootList);
		rhs.put("organizegroupAllList", organizegroupAllList);
		return "success";
	}

	public String ajax_resource_rolegroup_list() {
		String resourceId = getpara("resourceId");
		com.app.ea.model.Resource resource = (com.app.ea.model.Resource) baseDao
				.loadById("Resource", Long.parseLong(resourceId));
		List roleGroupRootList = infEa.getRolegroupRootNods();
		rhs.put("resource", resource);
		rhs.put("roleGroupRootList", roleGroupRootList);
		rhs.put("rolegroup", resource.getRolegroups());
		return "success";
	}

	public String ajax_resource_role_list() {
		String resourceId = getpara("resourceId");
		List organizeRootList = infEa.getOrganizeRootNods();
		com.app.ea.model.Resource resource = (com.app.ea.model.Resource) baseDao
				.loadById("Resource", Long.parseLong(resourceId));
		rhs.put("resource", resource);
		rhs.put("role", resource.getRoles());
		rhs.put("organizeRootList", organizeRootList);
		return "success";
	}

	public String ajax_resource_user_list() {
		String resourceId = getpara("resourceId");
		List usersList = infEa.getAllUser();
		com.app.ea.model.Resource resource = (com.app.ea.model.Resource) baseDao
				.loadById("Resource", Long.parseLong(resourceId));
		rhs.put("resource", resource);
		rhs.put("usersList", usersList);
		return "success";
	}
	
	public String ajax_role_user_list() {
		String roleId = getpara("roleId");
		List usersList = infEa.getAllUser();
		Role role = (Role) baseDao
				.loadById("Role", Long.parseLong(roleId));
		rhs.put("role", role);
		rhs.put("organizeId", getpara("organizeId"));
		rhs.put("usersList", usersList);
		return "success";
	}	
	

	public String iframe_get_userlist_of_role() {
		String roleId = getpara("roleId");
		Role role = (Role) baseDao.loadById("Role", Long.parseLong(roleId));
		log.debug(role.getName());
		rhs.put("userList", infEa.getAllUser());
		rhs.put("role", role);
		return "success";
	}

	public String iframe_get_rolelist_of_user() throws Exception {
		User user = (User) baseDao.loadById("User", Long.parseLong(getpara("id")));
		rhs.put("user", user);
		//menu_role_view_v();
		rhs.put("rolegroupRootList", common_get_tree_root("Rolegroup"));
		return "success";
	}	
	
	public String create_resource_rolegroup_relation() {

		String resourceId = getpara("resourceId");
		String rolegroupId = getpara("rolegroupId");
		com.app.ea.model.Resource resource = (com.app.ea.model.Resource) baseDao
				.loadById("Resource", Long.parseLong(resourceId));
		Rolegroup rolegroup = (Rolegroup) baseDao.loadById("Rolegroup",
				Long.parseLong(rolegroupId));
		if (rolegroup.getResources().contains(resource)) {
			rolegroup.getResources().remove(resource);
		} else {
			rolegroup.getResources().add(
					(com.app.ea.model.Resource) resource);
		}
		baseDao.update(rolegroup);
		return "success";
	}

	public String create_resource_role_relation() {

		String resourceId = getpara("resourceId");
		String roleId = getpara("roleId");
		com.app.ea.model.Resource resource = (com.app.ea.model.Resource) baseDao
				.loadById("Resource", Long.parseLong(resourceId));
		Role role = (Role) baseDao.loadById("Role", Long.parseLong(roleId));
		if (role.getResources().contains(resource)) {
			role.getResources().remove(resource);
		} else {
			role.getResources().add(
					(com.app.ea.model.Resource) resource);
		}
		baseDao.update(role);
		return "success";
	}

	public String create_user_role_relation() {
		infEa.putUserToRole(getpara("userId"), getpara("roleId"));

		return "success";
	}

	public String create_resource_user_relation() {

		String userId = getpara("userId");
		String resourceId = getpara("resourceId");
		com.app.ea.model.Resource resource = (com.app.ea.model.Resource) baseDao
				.loadById("Resource", Long.parseLong(resourceId));
		User user = (User) baseDao.loadById("User", Long.parseLong(userId));

		if (resource.getUsers().contains(user)) {
			resource.getUsers().remove(user);
		} else {
			resource.getUsers().add(user);
		}
		baseDao.update(resource);
		return "success";
	}

	public String ajax_put_organize_to_organizegroup() {

		String organizegroupId = getpara("organizegroupId");
		String organizeId = getpara("organizeId");
		Organize organize = (Organize) baseDao.loadById("Organize",
				Long.parseLong(organizeId));
		Organizegroup organizegroup = (Organizegroup) baseDao.loadById(
				"Organizegroup", Long.parseLong(organizegroupId));
		infEa.putOrganizeToOrganizegroup(organize, organizegroup);
		List organizegroupAllList = infEa.getAllOrganizegroup();
		List organizeRootList = infEa.getOrganizeRootNods();
		rhs.put("organizeRootList", organizeRootList);
		rhs.put("organizegroupAllList", organizegroupAllList);

		return "success";
	}

	public String update_organize_role_list() {
		String id = getpara("id");
		String column = getpara("column");
		String columnValue = getpara("columnValue");
		Role role = (Role) baseDao.loadById("Role", Long.parseLong(id));
		if (column.equals("name")) {
			role.setName(columnValue);
		}
		if (column.equals("alias")) {
			role.setAlias(columnValue);
		}
		baseDao.update(role);
		return "success";
	}
    


	public String clean() throws Exception {
		Cache.delete("userlist");
		return "success";
	}
	public String menu_query_inf() {
		return "success";
	}

	public String ajax_query_resource() {
		String name = getpara("username");
		User user = (User) baseDao.loadByFieldValue(User.class, "name", name);
		List list = infEa.getAllResoucesByUserName(name);
		rhs.put("user", user);
		rhs.put("list", list);
		return "success";

	}

	
	
	
	
	
	
	// view 
	
	public String menu_position_define() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";

	}

	public String menu_position_view() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";

	}

	public String menu_role_view() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";

	}

	public String menu_role_define() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";

	}

	public String menu_user_handbook() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";

	}

	public String menu_user_handbook_define() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";

	}

	
	
	public String menu_user_kpi_define() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";

	}

	public String menu_view_user_handbook_detail() {
		String id = getpara("id");
		User user = (User) baseDao.loadById("User", Long.parseLong(id));
		rhs.put("user", user);
		return "success";

	}
	
		
	
	
	public String menu_user_duty_define_by_role() {
		List roleGroupRootList = baseDao
				.find(" from Role where parent_id = null");
		rhs.put("roleRootList", roleGroupRootList);
		return "success";

	}

	public String menu_user_duty_define() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";
	}


	public String menu_role_view_v() {
		List roleRootList = baseDao
				.find(" from Role where parent_id = null");
		rhs.put("roleRootList", roleRootList);
		return "success";

	}
	public String menu_organize() throws Exception {
		rhs.put("organizeRootList", common_get_tree_root("Organize"));
		rhs.put("rolegroupRootList", common_get_tree_root("Rolegroup"));
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}
	public String menu_workhomepage() {
		List ruleRootList = baseDao
				.find(" from Rule where parent_id = null");
		List viewhistory = baseDao.find("from Viewhistory");
		User user = (User) baseDao.loadById("User", getCurrentUser().getId());
		rhs.put("user", user);
		rhs.put("viewhistory", viewhistory);
		rhs.put("ruleRootList", ruleRootList);
		/**2014.2.12
		String year = getpara("year");
		rhs.put("year",year);
		*/
		return "success";

	}
	public String menu_kpi_define() {
		List roleGroupRootList = baseDao
				.find(" from Rolegroup where parent_id = null");
		rhs.put("roleGroupRootList", roleGroupRootList);
		return "success";

	}

	public String sumReport() throws Exception {
		String year = getpara("theYear");
		String type = getpara("type");
		String userA = getpara("userAccount");
		String userName = getpara("user");
		String account;
		if(userA.equals("")){
			account = getCurrentUser().getAccount();
			System.out.println("no account here");
		}
		else{
			account = userA;
			System.out.println("get account here");
		}
		ArrayList reportList= (ArrayList) baseDao.find("select r from Report r where r.type = '" + type +"' and r.userAccount = '" + account+ "'and date like '"+year+"%'");
		rhs.put("sumReport",reportList);
		rhs.put("year", year);
		rhs.put("type", type);
		rhs.put("name", userName);
		return "success";
	}	
	
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}	
	
}
