package com.app.ea.action;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.base.action.BaseEaAction;
import com.app.common.spring.ssh.action.BaseAction;
import com.app.common.spring.ssh.action.BaseBusinessAction;
import com.app.ea.model.Rule;

import org.apache.commons.beanutils.BeanUtils;

@Component("ruleAction")
@Scope("prototype")
public class RuleAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(RuleAction.class);
	public String menu_rule() throws Exception {
		rhs.put("ruleRootList", common_get_tree_root("Rule"));
		
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}
	public String list_rule() throws Exception {
		rhs.put("ruleRootList", common_get_tree_root("Rule"));
		rhs.put("info_type", "success");
		rhs.put("info", "");
		return "success";
	}
	public String create() throws Exception {
		String id = getpara("id");
		if ("root".equals(id)) {
			Rule rule = new Rule();
			rule.setName("");
			baseDao.create(rule);
		} else {
			Rule parent_org = (Rule) baseDao.loadById("Rule",
					Long.parseLong(id));
			Rule subrule = new Rule();
			subrule.setName("");
			subrule.setParentModel(parent_org);
			baseDao.create(subrule);
		}
		rhs.put("ruleRootList", common_get_tree_root(getpara("beanname")));
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
		rhs.put("ruleRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "更新成功!");
		return "success";
	}

	public String delete() throws Exception {
		common_del_tree_node();
		rhs.put("ruleRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", " 删除成功!");
		return "success";
	}
	
	public String detail() throws Exception {
		Rule rule = (Rule) baseDao.loadById("Rule",
				Long.parseLong(getpara("id")));
		rhs.put("rule", rule);
		rhs.put("info_type", "success");
		rhs.put("info", " 删除成功!");
		return "success";
	}
	
	public String change_rank() throws Exception {
		common_change_rank(); 
		rhs.put("ruleRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变顺序成功!");
		return "success";
	}
	public String change_level() throws Exception {
		common_change_level();
		rhs.put("ruleRootList", common_get_tree_root(getpara("beanname")));
		rhs.put("info_type", "success");
		rhs.put("info", "改变层级成功!");
		return "success";
	}
}