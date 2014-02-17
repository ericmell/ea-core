package com.app.ea.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.ea.model.Resource;
@Component("resourceAction")
@Scope("prototype")
public class ResourceAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(ResourceAction.class);
	public String menu_resource() throws Exception {
		rhs.put("resourceRootList", common_get_tree_root("Resource"));
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}

	public String create() throws Exception {
		String id = getpara("id");
		if ("root".equals(id)) {
			Resource resource = new Resource();
			resource.setName("");
			baseDao.create(resource);
		} else {
			Resource parent_org = (Resource) baseDao.loadById("Resource",
					Long.parseLong(id));
			Resource subresource = new Resource();
			subresource.setName("");
			subresource.setParentModel(parent_org);
			baseDao.create(subresource);
		}
		rhs.put("resourceRootList", common_get_tree_root(getpara("beanname")));
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
		rhs.put("resourceRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "更新成功!");
		return "success";
	}

	public String delete() throws Exception {
		common_del_tree_node();
		rhs.put("resourceRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", " 删除成功!");
		return "success";
	}
	
	public String change_rank() throws Exception {
		common_change_rank(); 
		rhs.put("resourceRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
		return "success";
	}
	public String change_level() throws Exception {
		common_change_level();
		rhs.put("resourceRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变层级成功!");
		return "success";
	}
}
