package com.app.common.base.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.ea.model.Resource;

@Component("menuAction")
@Scope("prototype")
public class MenuAction extends BaseEaAction {
	private final Logger log = LoggerFactory.getLogger(MenuAction.class);
	public String execute() {
		String sysName = getpara("sysName");
		Resource resource = (Resource) infEa.getBaseModelByAlias("Resource",
				sysName);
		rhs.put("resource", resource);
		rhs.put("userlist", infEa.getAllUser());
		log.debug("用户数" + infEa.getAllUser().size());
		return "success";
	}
}
