package com.app.common.activiti.action;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.common.activiti.api.InfActiviti;
import com.app.common.base.action.BaseEaAction;


public class BaseProcessAction extends BaseEaAction {

	public RepositoryService repositoryService;
	public RuntimeService runtimeService;
	public TaskService taskService;
	public HistoryService historyService;
	public ManagementService managementService;
	public IdentityService identityService;
	public FormService formService;
	public InfActiviti infActiviti;

	@javax.annotation.Resource(name = "repositoryService")
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	@javax.annotation.Resource(name = "runtimeService")
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	@javax.annotation.Resource(name = "taskService")
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	@javax.annotation.Resource(name = "historyService")
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	@javax.annotation.Resource(name = "managementService")
	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}

	@javax.annotation.Resource(name = "identityService")
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	@javax.annotation.Resource(name = "formService")
	public void setFormService(FormService formService) {
		this.formService = formService;
	}


	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public ManagementService getManagementService() {
		return managementService;
	}

	public IdentityService getIdentityService() {
		return identityService;
	}

	public FormService getFormService() {
		return formService;
	}

	public InfActiviti getInfActiviti() {
		return infActiviti;
	}
	@Resource(name = "impActiviti")
	@Autowired
	public void setInfActiviti(InfActiviti infActiviti) {
		this.infActiviti = infActiviti;
	}
}
