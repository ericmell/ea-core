package com.app.common.activiti.api;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.common.activiti.model.TaskLog;
import com.app.common.spring.ssh.dao.BaseDao;
import com.utils.time.TimeUtil;

public class HandleServiceTaskDelegate implements JavaDelegate {

	private final Logger log = LoggerFactory.getLogger(HandleServiceTaskDelegate.class);
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.debug(execution.getCurrentActivityName());
		
		BaseDao baseDao = (BaseDao)SpringContext.getBean("eaDaoTarget");
		TaskLog model = new TaskLog();
		model.setLog("Task is OK.");
		model.setTaskId(execution.getId());
		model.setTaskName(execution.getCurrentActivityName());
		model.setProcessInstanceId(execution.getProcessInstanceId());
		model.setCreateTime(TimeUtil.getTimeStr("yyyy-MM-dd HH:mm:ss"));
		model.setUserAccount("system");
		model.setUserName("system");
		baseDao.create(model);
	}
}
