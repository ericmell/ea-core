package com.app.ea.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.ea.model.Organize;
import com.app.ea.model.Role;


@Component("roleAction")
@Scope("prototype")
public class RoleAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(RoleAction.class);
	
	public String menu_role() throws Exception {
		rhs.put("roleRootList", common_get_tree_root("Role"));
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}
	public String change_rank() throws Exception {
		common_change_rank(); 
		rhs.put("roleRootList", common_get_tree_root(getpara("beanname")));
		
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
		return "success";
	}
	public String change_level() throws Exception {
		common_change_level();
		rhs.put("roleRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变层级成功!");
		return "success";
	}
	public String create() throws Exception {
		String id = getpara("id");
		if ("root".equals(id)) {
			Role role = new Role();
			role.setName("");
			baseDao.create(role);
		} else {
			Role parent_org = (Role) baseDao.loadById("Role",
					Long.parseLong(id));
			Role subrole = new Role();
			subrole.setName("");
			subrole.setParentModel(parent_org);
			baseDao.create(subrole);
		}
		rhs.put("roleRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", " 添加新节点!");
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
		rhs.put("roleRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "更新成功!");
		return "success";
	}

	public String delete() throws Exception {
		Role role = (Role) baseDao.loadById("Role",
				Long.parseLong(getpara("id")));
		Organize organize = (Organize) baseDao.loadById("Organize",
				Long.parseLong(getpara("organizeId")));
		organize.getRoles().remove(role);
		baseDao.update(organize);
		rhs.put("roleRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", " 删除成功!");
		return "success";
	}
}
