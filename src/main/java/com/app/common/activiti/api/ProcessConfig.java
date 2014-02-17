package com.app.common.activiti.api;

public class ProcessConfig {

	private String formId;// 业务数据表的id
	private String assignee;// 下一环节处理人
	private String processKey;// bpmn图的process id，开始环节不能为空
	private String initiator;// 流程启动者，开始环节不能为空
	private String taskId;// 任务id，任务处理环节不能为空
	private String operate;//流转条件

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

}
