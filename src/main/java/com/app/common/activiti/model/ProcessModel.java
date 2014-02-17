package com.app.common.activiti.model;

import com.app.common.spring.ssh.model.BaseModel;

public class ProcessModel extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	public String processInstanceId;

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
}
