package com.app.common.base.action;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.common.spring.ssh.dao.BaseDao;

public class BaseEaAction extends BaseCoreAction {
	private final Logger log = LoggerFactory.getLogger(BaseEaAction.class);
	@Resource(name = "eaDaoTarget")
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	
}
