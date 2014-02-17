package com.app.ea.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.ea.model.Systempara;

import com.opensymphony.util.BeanUtils;

@Component("systemparaAction")
@Scope("prototype")
public class SystemparaAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(SystemparaAction.class);
	public String menu_systempara() throws Exception {
		rhs.put("systemparaRootList", common_get_tree_root("Systempara"));
		//rhs.put("system_config", SystemInit.SYSTEM_CONFIG);
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}

	public String create() throws Exception {
		String id = getpara("id");
		if ("root".equals(id)) {
			Systempara systempara = new Systempara();
			systempara.setName("");
			baseDao.create(systempara);
		} else {
			Systempara parent_org = (Systempara) baseDao.loadById("Systempara",
					Long.parseLong(id));
			Systempara subsystempara = new Systempara();
			subsystempara.setName("");
			subsystempara.setParentModel(parent_org);
			baseDao.create(subsystempara);
		}
		rhs.put("systemparaRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", " 添加新节点!");
		return "success";
	}	

	public String updateSystemConfig() throws Exception {
		String paraname = getpara("paraname");
		String value = getpara("value");
		
		try {
			//BeanUtils.setValue(SystemInit.SYSTEM_CONFIG, paraname, value);
		} catch (Exception e) {
			log.error("属性修改有问题" + paraname + "=" + value);
			e.printStackTrace();
		}
	//	log.debug("编辑模式："+SystemInit.SYSTEM_CONFIG.woEditModel);
	//	SystemInit.saveSystemConfigXml(SystemInit.system_config_path, SystemInit.SYSTEM_CONFIG);
		rhs.put("info_type", "success");
		rhs.put("info", " 设置成功!");
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
		rhs.put("systemparaRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "更新成功!");
		return "success";
	}

	public String delete() throws Exception {
		common_del_tree_node();
		rhs.put("systemparaRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", " 删除成功!");
		return "success";
	}
	
	public String change_rank() throws Exception {
		common_change_rank(); 
		rhs.put("systemparaRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
		return "success";
	}
	public String change_level() throws Exception {
		common_change_level();
		rhs.put("systemparaRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变层级成功!");
		return "success";
	}
}