package com.app.ea.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.ea.model.Rolegroup;


@Component("rolegroupAction")
@Scope("prototype")
public class RolegroupAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(RolegroupAction.class);
	
	public String menu_rolegroup() throws Exception {
		rhs.put("rolegroupRootList", common_get_tree_root("Rolegroup"));
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}
	public String change_rank() throws Exception {
		common_change_rank(); 
		rhs.put("rolegroupRootList", common_get_tree_root(getpara("beanname")));
		
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
		return "success";
	}
	public String change_level() throws Exception {
		common_change_level();
		rhs.put("rolegroupRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变层级成功!");
		return "success";
	}
	public String create() throws Exception {
		String id = getpara("id");
		if ("root".equals(id)) {
			Rolegroup rolegroup = new Rolegroup();
			rolegroup.setName("");
			baseDao.create(rolegroup);
		} else {
			Rolegroup parent_org = (Rolegroup) baseDao.loadById("Rolegroup",
					Long.parseLong(id));
			Rolegroup subrolegroup = new Rolegroup();
			subrolegroup.setName("");
			subrolegroup.setParentModel(parent_org);
			baseDao.create(subrolegroup);
		}
		rhs.put("rolegroupRootList", common_get_tree_root(getpara("beanname")));
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
		rhs.put("rolegroupRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "更新成功!");
		return "success";
	}

	public String delete() throws Exception {
		common_del_tree_node();
		rhs.put("rolegroupRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", " 删除成功!");
		return "success";
	}
}
