package com.app.common.base.action;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.model.Organize;
import com.app.ea.model.User;

public class BaseOaAction extends BaseCoreAction {
	private final Logger log = LoggerFactory
			.getLogger(BaseOaAction.class);

	@Resource(name = "baseDaoTarget")
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

}
