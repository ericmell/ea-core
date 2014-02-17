package com.app.common.spring.ssh.action;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.common.spring.ssh.dao.BaseDao;

/*
 通过此类可以设置不同数据源的注入
 * */
public class BaseBusinessAction extends BaseAction {
	private final Logger log = LoggerFactory
			.getLogger(BaseBusinessAction.class);

	@Resource(name = "baseDaoTarget")
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

}
