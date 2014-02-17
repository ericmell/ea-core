package com.app.ea.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.ea.model.Organizegroup;


@Component("organizegroupAction")
@Scope("prototype")
public class OrganizegroupAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(OrganizegroupAction.class);
	
	public String menu_organizegroup() throws Exception {
		rhs.put("organizegroupRootList", common_get_tree_root("Organizegroup"));
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}
	public String change_rank() throws Exception {
		common_change_rank(); 
		rhs.put("organizegroupRootList", common_get_tree_root(getpara("beanname")));
		
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
		return "success";
	}
	public String change_level() throws Exception {
		common_change_level();
		rhs.put("organizegroupRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变层级成功!");
		return "success";
	}
	public String create() throws Exception {
		String id = getpara("id");
		if ("root".equals(id)) {
			Organizegroup organizegroup = new Organizegroup();
			organizegroup.setName("");
			baseDao.create(organizegroup);
		} else {
			Organizegroup parent_org = (Organizegroup) baseDao.loadById("Organizegroup",
					Long.parseLong(id));
			Organizegroup suborganizegroup = new Organizegroup();
			suborganizegroup.setName("");
			suborganizegroup.setParentModel(parent_org);
			baseDao.create(suborganizegroup);
		}
		rhs.put("organizegroupRootList", common_get_tree_root(getpara("beanname")));
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
		rhs.put("organizegroupRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "更新成功!");
		return "success";
	}

	public String delete() throws Exception {
		common_del_tree_node();
		rhs.put("organizegroupRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", " 删除成功!");
		return "success";
	}
}
