package com.app.ea.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.model.Extv;
import com.app.ea.model.Organize;
import com.app.ea.model.Role;
import com.app.ea.model.Rolegroup;
import com.app.ea.model.User;
import com.opensymphony.util.BeanUtils;
@Component("organizeAction")
@Scope("prototype")
public class OrganizeAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(OrganizeAction.class);

	public String menu_organize() throws Exception {
		rhs.put("organizeRootList", common_get_tree_root("Organize"));
		rhs.put("system_para_map", 	infEa.getParaMap());
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}

	public String create() throws Exception {
		String id = getpara("id");
		if ("root".equals(id)) {
			Organize organize = new Organize();
			organize.setName("");
			baseDao.create(organize);
		} else {
			Organize parent_org = (Organize) baseDao.loadById("Organize",
					Long.parseLong(id));
			Organize suborganize = new Organize();
			suborganize.setName("");
			suborganize.setParentModel(parent_org);
			baseDao.create(suborganize);
		}
		rhs.put("organizeRootList", common_get_tree_root(getpara("beanname")));
		
		rhs.put("info_type", "success");
		rhs.put("info", " 添加新节点成功!");
		
		return "success";
	}	

	public String admin() throws Exception {
		common_get_extp("Organize");
		rhs.put("rolegroupRootList", common_get_tree_root("Rolegroup"));
		Organize organize = (Organize) baseDao.loadById("Organize",
				Long.parseLong(getpara("organizeId")));
		rhs.put("organize", organize);
		rhs.put("userList", infEa.getAllUser());
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
		rhs.put("organizeRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "更新成功!");
		return "success";
	}
	public String index() throws Exception {
		
		return "success";
	}
	public String top() throws Exception {
		
		return "success";
	}	

			

	  /*删除一个组织架构，需要将角色也删除掉，不然 显示人员部署图，是根据角色组的根节点，把第一层的角色列出来*/
		public String delete_role() throws Exception {
			Role role = (Role) baseDao.loadById("Role",
					Long.parseLong(getpara("roleId")));
			infEa.deleteRole(role);
			rhs.put("info", " 删除成功!");
			admin();
			return "success";
		}
		public String create_new_user_in_role() throws Exception {
			User user=new User();
			user.setName(getpara("name"));
			user.setAccount(getpara("account"));
			Role role=(Role)baseDao.loadById("Role",
					Long.parseLong(getpara("roleId")));
			
			baseDao.create(user);
			user.getRoles().add(role);
			baseDao.update(user);
			rhs.put("info", user.getName()+ " 添加成功!");
			admin();
			return "success";
		}
		
		public String change_role_level() throws Exception {
			common_change_level();
			rhs.put("info_type", "success");
			rhs.put("info", "改变层级成功!");
			admin();
			return "success";
		}
		
		public String role_user_list() throws Exception {
			String roleId = getpara("roleId");
			List usersList = infEa.getAllUser();
			Role role = (Role) baseDao
					.loadById("Role", Long.parseLong(roleId));
			rhs.put("role", role);
			rhs.put("organizeId", getpara("organizeId"));
			rhs.put("usersList", usersList);
			admin();
			return "success";
		}	
		public String create_user_role_relation() throws Exception {
			infEa.putUserToRole(getpara("userId"), getpara("roleId"));
			admin();
			return "success";
		}
		
		
		public String create_role_in_organize() throws Exception {
			Organize organize = (Organize) baseDao.loadById("Organize",
					Long.parseLong(getpara("organizeId")));
			Rolegroup rolegroup = (Rolegroup) baseDao.loadById("Rolegroup",
					Long.parseLong(getpara("rolegroupId")));
			
			Role role = new Role();
			String rolename=organize.getName() + "-" + rolegroup.getName();
			role.setName(rolename);
			role.setAlias(organize.getAlias() + "-" + rolegroup.getAlias());
			role.getRolegroups().add(rolegroup);
			role.getOrganizes().add(organize);
			baseDao.create(role);
			rhs.put("info_type", "success");
			rhs.put("info", rolename+":创建成功!");	
			admin();
	        /*
			if(rolegroup.getParentModel().getParentModel()==null){
				
				baseDao.create(role);
				rhs.put("info_type", "success");
				rhs.put("info", rolename+":创建成功!");			
	         }else{
	        	 if(!getpara("parentRoleId").equals("")){
		        	 Role parentModel=(Role) baseDao.loadById("Role",
		     				Long.parseLong(getpara("parentRoleId")));
		        	 role.setParentModel(parentModel);
		        	 baseDao.create(role);
		 			 rhs.put("info_type", "success");
		 			 rhs.put("info", rolename+":创建成功!");	
		 			 return "success";
	        	 }
	        	 rhs.put("info_type", "fail");
	 			 rhs.put("info", rolename+":创建失败，必须选择上级!");
			}
			*/
			return "success";
		}
			
     /*删除一个组织架构，需要将角色也删除掉，不然 显示人员部署图，是根据角色组的根节点，把第一层的角色列出来*/
	public String delete() throws Exception {
		Organize organize = (Organize) baseDao.loadById(getpara("beanname"),
				Long.parseLong(getpara("id")));
		
        infEa.deleteOrgainize(organize);

		rhs.put("organizeRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", " 删除成功!");
		return "success";
	}
	
	public String change_rank() throws Exception {
		common_change_rank(); 
		rhs.put("organizeRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
		return "success";
	}
	public String change_level() throws Exception {
		common_change_level();
		rhs.put("organizeRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变层级成功!");
		return "success";
	}
	
	
	 public String updateExtp() throws Exception {
		 common_update_extp();
		 rhs.put("organizeRootList", common_get_tree_root(getpara("beanname")));
		 rhs.put("info_type", "success");
		 rhs.put("info", "更新成功!");
		 return "success";
	    }
	
	
}
