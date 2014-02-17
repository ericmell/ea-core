package com.app.common.activiti.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.common.activiti.model.ProcessModel;
import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.page.Pagination;

//@Component("impActiviti")
public class ImpActiviti implements InfActiviti {
	private final Logger log = LoggerFactory.getLogger(ImpActiviti.class);
	public Properties properties;
	private RepositoryService repositoryService;
	private RuntimeService runtimeService;
	private TaskService taskService;
	private HistoryService historyService;
	private ManagementService managementService;
	private IdentityService identityService;
	private FormService formService;
	private BasicDataSource dataSource;
    private BaseDao baseDao;
	
    public String getDatabase(){
    	return dataSource.getUrl();
    }
    
	@Override
	public Map<String, Object> getOaTaskListByAccount(String account, int page, int maxSize) {
		Long count = taskService.createTaskQuery().taskAssignee(account).count();
		
		Pagination p = new Pagination();
		p.setCurrentPage(page);
		p.setMaxSize(maxSize);
		p.setTotalSize(count);
		
		List<Task> tasklist = taskService.createTaskQuery()
				.taskAssignee(account).orderByTaskCreateTime().desc()
				.listPage((int)p.getStart() - 1, p.getMaxSize());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pagination", p);
		
		map.put("dataList", taskToOaTask(tasklist));
		/*	
		map.put("dataList",tasklist);
		*/
		return map;
	}
	
	public List<OaTask> getHistoryTaskListByAccount(String account) {
		List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
				.taskAssignee(account)
				.finished()
				.orderByHistoricTaskInstanceEndTime()
				.desc()
				.list();

		return taskToOaHistoryTask(taskList);
	}
	@Override
	public List<OaTask> getCandidateTaskListByAccount(String account) {
		List<Task> candidateTaskList = taskService.createTaskQuery().taskCandidateUser(account).list();
		 return taskToOaTask(candidateTaskList);
	}
	
	public List<Task> getTaskListByAccountAndProcessKey(String account, String ProcessKey) {
		List<Task> tasklist = taskService.createTaskQuery()
				.processDefinitionKey(ProcessKey)
				.taskAssignee(account).orderByTaskCreateTime().desc()
				.list();
		return tasklist;
	}
	
   //old
	@Override
	public String startProcess(Map<String, Object> map) {
		Object assignee = map.get("assignee");
		if (assignee != null)
			map.remove("assignee");
		identityService.setAuthenticatedUserId(map.get("initiator").toString());
		map.remove("initiator");
        //这里通过 流程实例的查询，查出具体的任务，将让副返回
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(
				map.get("processKey").toString(), map);
		if (assignee != null) {
			List<Task> list = taskService.createTaskQuery()
					.processInstanceId(pi.getId()).list();
			for (Task t : list) {
				System.out.println("startProcess - Set assignee:["
						+ assignee.toString() + "] for task name:["
						+ t.getName() + "]");
				taskService.setAssignee(t.getId(), assignee.toString());
			}
		}
		return pi.getId();
	}
    //创建实例，取得实例的第一个task，设置好分配人
	@Override
	public String startProcessAssignee(String processKey, String formId, String busiDesc, String initiator, String assignee) {
		Map<String, Object> var = new HashMap<String, Object>();
		var.put("formId", formId); // 业务ID
		var.put("assignee", assignee); // 下一环节处理人 
		var.put("initiator",initiator); // 任务发起人
		var.put("processKey", processKey); // 流程key
		var.put("busiDesc", busiDesc);// 业务记录的标志字段的简单说明
		
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(processKey, var);
		return pi.getId();
	}   
	
    //创建实例，取得实例的第一个task，设置好分配人，并设置流程变量
	@Override
	public String startProcessAssigneeVar(String processKey, String formId, String busiDesc, String initiator, String assignee, Map<String, Object> map) {
		Map<String, Object> var = new HashMap<String, Object>();
		var.put("formId", formId); // 业务ID
		var.put("assignee", assignee); // 下一环节处理人 
		var.put("initiator",initiator); // 任务发起人
		var.put("processKey", processKey); // 流程key
		var.put("busi_desc", busiDesc);//业务记录的标志字段的简单说明
		
		//设置流程变量
		if(map != null)
			var.putAll(map);
		log.debug("busiDesc="+busiDesc);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(var.get("processKey").toString(), var);
		return pi.getId();
	}
	
	/**
	 启动任务
	 * */
	public String startProcessNoAssigee(String processKey, String formId,String initiator,String busi_desc) {
		Map<String, Object> var = new HashMap<String, Object>();
		var.put("formId", formId); // 业务ID
		var.put("initiator",initiator); // 任务发起人
		var.put("processKey", processKey); // 流程key
		var.put("busi_desc", busi_desc);//业务记录的标志字段的简单说明
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(
				var.get("processKey").toString(), var);
		return pi.getId();
	}	
	
	@Override
	public String processInstanceStatus(String processInstanceId) {
		if (processInstanceId == null || "".equals(processInstanceId))
			return ProcessInstanceStatus.New;
		else {
			HistoricProcessInstance hpi = historyService
					.createHistoricProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
			if (hpi == null) {
				return ProcessInstanceStatus.New;
			} else if (hpi.getEndTime() != null) {
				return ProcessInstanceStatus.Done;
			} else {
				ProcessInstance pi = runtimeService
						.createProcessInstanceQuery()
						.processInstanceId(processInstanceId).singleResult();
				if (pi.isSuspended())
					return ProcessInstanceStatus.Suspended;
				else
					return ProcessInstanceStatus.Ongoing;
			}
		}
	}
	//old
	@Override
	public void completeTask(ProcessConfig config) {
		Task task = taskService.createTaskQuery().taskId(config.getTaskId())
				.singleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		if (config.getOperate() != null)
			map.put("operate", config.getOperate());
		if (config.getFormId() != null)
			map.put("formId", config.getFormId());
		taskService.complete(config.getTaskId(), map);
		
		//如果有下一个就往下走, 但是如何知道下一个task呢，通过查询
		if (config.getAssignee() != null) {
			if (!processInstanceStatus(task.getProcessInstanceId()).equals(
					ProcessInstanceStatus.Done)) {
				setNextTaskAssignee(task.getProcessInstanceId(),
						config.getAssignee());
			}
		}
	}
	
	public void completeTaskVar(String taskId, String formId, String assignee, Map<String, Object> var) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("assignee", assignee);
		if (formId != null)
			map.put("formId", formId);
		if(var != null)
			map.putAll(var);
		taskService.complete(taskId, map);
	}
	/* add by chenzhijian -s */
	public void completeTaskForBusinessAction(HttpServletRequest request,String taskId, String formId, Map<String, Object> var) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> nextTasks = this.getNextTasks(taskId);
		for(Map<String, String> m : nextTasks){
			map.put(m.get("taskAssigneeName"), request.getParameter(m.get("taskAssigneeName")));
		   // String nametodo=(String)request.getParameter(m.get("taskAssigneeName"));

		}
		
		if (formId != null)
			map.put("formId", formId);
		if(var != null)
			map.putAll(var);
		log.debug(map.toString());
		
		
		//taskService.complete(taskId, map);
	}
	/* add by chenzhijian -e */
//	@Override
//	public void completeTaskByTaskId(ProcessConfig config) {
//		Task task = taskService.createTaskQuery().taskId(config.getTaskId())
//				.singleResult();
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (config.getOperate() != null)
//			map.put("operate", config.getOperate());
//		if (config.getFormId() != null)
//			map.put("formId", config.getFormId());
//		taskService.complete(config.getTaskId(), map);
//		
//		//如果有下一个就往下走
//		if (config.getAssignee() != null) {
//			if (!processInstanceStatus(task.getProcessInstanceId()).equals(
//					ProcessInstanceStatus.Done)) {
//				setNextTaskAssignee(task.getProcessInstanceId(),
//						config.getAssignee());
//			}
//		}
//	}
	
	@Override
	public void changeAssignee(String processInstanceId, String assignee) {
		Task t = taskService.createTaskQuery()
				.processInstanceId(processInstanceId).taskName("处理issue")
				.singleResult();
		taskService.setAssignee(t.getId(), assignee);
	}

	@Override
	public void deleteProcessInstance(String processInstanceId) {
		if (processInstanceId != null) {
			ProcessInstance pi = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
			if (pi != null)
				runtimeService.deleteProcessInstance(processInstanceId,
						"用户删除操作");
		}
	}

	@Override
	public Object getVariableByTaskId(String taskId, String varName) {
		Object ret = null;
		if (taskId != null && !taskId.equals("")) {
			Task t = taskService.createTaskQuery().taskId(taskId).singleResult();
			if (t != null) {
				ret = runtimeService.getVariable(t.getProcessInstanceId(), varName);
			}
		}
		
		if(ret == null)
			return "";
		else
			return ret;
	}

	@Override
	public Object getVariableByProcessInstanceId(String processInstanceId,
			String varName) {
		if (processInstanceId != null && !processInstanceId.equals("")) {
			return runtimeService.getVariable(processInstanceId, varName);
		}
		return null;
	}

	@Override
	public void setVariableByProcessInstanceId(String processInstanceId,
			String varName, Object value) {
		runtimeService.setVariable(processInstanceId, varName, value);
	}

	public Object getHistoryVariableByProcessInstanceId(String processInstanceId, String varName) {
		if (processInstanceId != null && !processInstanceId.equals("")) {
			HistoricVariableInstanceQuery hq = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(processInstanceId);
			if (hq != null) {
				hq = hq.variableName(varName);
				if (hq != null) {
					HistoricVariableInstance h = hq.singleResult();
					if(h != null)
						return h.getValue();
				}
			}
			return null;
		}
		return null;
	}
	
	@Override
	public ProcessDefinition getProcessDefinitionByTask(Task task) {
		return repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(task.getProcessDefinitionId())
				.singleResult();
	}

	public Task getTaskById(String taskId){
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}
	
	/* add by chenzhijian 20130423 -s*/
	public List<Map<String, String>> getNextTasks(String taskId){
		List<Map<String, String>> nextTaskList = new ArrayList<Map<String, String>>();
		Task task = this.getTaskById(taskId);

		ExecutionEntity execution = (ExecutionEntity)runtimeService
				.createExecutionQuery()
				.executionId(task.getExecutionId())
				.singleResult();  
		String activityId = execution.getActivityId();  
		
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService)
				.getDeployedProcessDefinition(task.getProcessDefinitionId());
		List<ActivityImpl> activityImplList = processDefinition.getActivities(); // 获取流程所有节点
		
		Map<String, TaskDefinition> taskDefinitions = processDefinition.getTaskDefinitions(); // 获取流程所有 userTask 节点

		for(ActivityImpl a : activityImplList){
			//log.debug(a.getProperties().toString());
			String id = a.getId();  
			if(activityId.equals(id)){  
				log.debug("当前任务：" + a.getProperty("name")); //输出某个节点的某种属性  
				List<PvmTransition> outTransitions = a.getOutgoingTransitions();//获取从某个节点出来的所有线路  
				for(PvmTransition tr : outTransitions){  
					PvmActivity ac = tr.getDestination(); //获取线路的终点节点  

					if(ac.getProperty("type").equals("parallelGateway") || ac.getProperty("type").equals("inclusiveGateway")){// 并行网关，查找网关下的所有分支任务
						nextTaskList = new ArrayList<Map<String, String>>();
						List<PvmTransition> outTransitions2 = ac.getOutgoingTransitions();//获取从某个节点出来的所有线路  
						for(PvmTransition tr2 : outTransitions2){  
							//UelExpressionCondition condition = (UelExpressionCondition)tr2.getProperty("condition");
							
							PvmActivity ac2 = tr2.getDestination(); //获取线路的终点节点  
							addNextTask(nextTaskList, taskDefinitions, ac2);
						}
					}else{
						addNextTask(nextTaskList, taskDefinitions, ac);
					}
				}  
				break;  
			}
		}
		return nextTaskList;
	}
	
	private void addNextTask(List<Map<String, String>> nextTaskList, Map<String, TaskDefinition> taskDefinitions, PvmActivity pa){
		log.debug("下一步任务：" + pa.getProperty("name"));  
		Map<String, String> map = new HashMap<String, String>();
		map.put("taskType", pa.getProperty("type").toString());
		map.put("taskName", pa.getProperty("name").toString());
		
		if(taskDefinitions.containsKey(pa.getId())){
			String assigneeExpression = taskDefinitions.get(pa.getId()).getAssigneeExpression().getExpressionText().trim();
			log.debug("assigneeExpression：" + assigneeExpression);  
			assigneeExpression = assigneeExpression.replace("${", "");
			assigneeExpression = assigneeExpression.replace("}", "");
			map.put("taskAssigneeName", assigneeExpression);
		}else{
			map.put("taskAssigneeName", "assignee");
		}
		nextTaskList.add(map);
	}
	/* add by chenzhijian 20130423 -e*/
	
	private void setNextTaskAssignee(String processInstanceId, String assignee) {
		List<String> keys = runtimeService
				.getActiveActivityIds(processInstanceId);
		
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement p = conn
					.prepareStatement("SELECT * FROM ACT_RU_TASK where PROC_INST_ID_ = ? and TASK_DEF_KEY_ = ?");
			for (String key : keys) {
				System.out
						.println("completeTask - runtimeService.getActiveActivityIds:"
								+ key);

				p.setString(1, processInstanceId);
				p.setString(2, key);
				ResultSet rs = p.executeQuery();
				while (rs.next()) {
					String id = rs.getString("ID_");
					String name = rs.getString("NAME_");
					System.out.println("completeTask - ResultSet ID_:" + id);
					System.out.println("completeTask - Set assignee:["
							+ assignee + "] for task name:[" + name + "]");
					taskService.setAssignee(id, assignee);
				}
				rs.close();
			}
			p.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Resource(name = "repositoryService")
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	@Resource(name = "runtimeService")
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	@Resource(name = "taskService")
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	@Resource(name = "historyService")
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	@Resource(name = "managementService")
	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}

	@Resource(name = "identityService")
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}
	
	@Resource(name = "formService")
	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	@Resource(name = "dataSource")
	public void setDataSource(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Resource(name="eaDaoTarget")
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	public List<OaTask> taskToOaTask(List<Task> tasklist) {
		List<OaTask> oaTaskList = new ArrayList<OaTask>();
		for (Task task : tasklist) {
			OaTask oaTask = new OaTask();
			oaTask.setTask(task);
			oaTask.setHandleTaskUrl(formService.getTaskFormData(task.getId()).getFormKey());
			oaTask.setProcessDefinition(getProcessDefinitionByTask(task));
			String initiator = (String) getVariableByTaskId(task.getId(), "initiator");
			oaTask.setInitiator(initiator);
			String busiDesc = (String) getVariableByTaskId(task.getId(), "busiDesc");
			log.debug("查询busiDesc："+busiDesc);
			oaTask.setBusiDesc(busiDesc);
			String formId = (String) getVariableByTaskId(task.getId(), "formId");
			oaTask.setFormId(formId);
			String model = (String) getVariableByTaskId(task.getId(), "model");
			if(!model.equals("") && !formId.equals("")){
				ProcessModel pm = (ProcessModel)baseDao.loadById(model, Long.parseLong(formId));
				oaTask.setBusinessModel(pm);
			}
			
			oaTaskList.add(oaTask);
		}
		return oaTaskList;
	}
 
	public List<OaTask> taskToOaHistoryTask(List<HistoricTaskInstance> tasklist) {
		
		List<OaTask> oaTaskList = new ArrayList<OaTask>();
		for (HistoricTaskInstance task : tasklist) {
			OaTask oaTask = new OaTask();
			oaTask.setHistoryTask(task);
			
			String initiator = (String) getHistoryVariableByProcessInstanceId(task.getProcessInstanceId(),  "initiator");
			oaTask.setInitiator(initiator);
			
			String busiDesc = (String) getHistoryVariableByProcessInstanceId(task.getProcessInstanceId(),  "busiDesc");
			oaTask.setBusiDesc(busiDesc);
			
			String formId = (String) getHistoryVariableByProcessInstanceId(task.getProcessInstanceId(),  "formId");
			oaTask.setFormId(formId);
			
			//oaTask.setCreateTime(DateUtil.getTimeStrByDate(task.getCreateTime(), "yyyy-MM-dd hh-mm-ss"));
			oaTaskList.add(oaTask);
		}
		return oaTaskList;
	}

	@Override
	public void deploy(String filepath) throws FileNotFoundException {
		/*
		String deploymentId = repositoryService
			    .createDeployment()
			    .addClasspathResource(filepath)
			    .deploy()
			    .getId();
	    */
		String file_name = filepath;
		File file = new File(file_name);
		repositoryService.createDeployment()
		.addInputStream(file_name, new FileInputStream(file))
		.name(file.getName() + "-" + new Date()).deploy();
		log.debug("deploy finished");
	}
	@Override
	public void claim(String taskId, String accout) {
		taskService.claim(taskId, accout);
		
	}
	@Override
	public void deployByRequest() throws Exception {
		try {
			MultiPartRequestWrapper request = (MultiPartRequestWrapper) ServletActionContext
					.getRequest();
			File[] files = request.getFiles("file");
			String[] fileNames = request.getFileNames("file");
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
						repositoryService
								.createDeployment()
								.addInputStream(fileNames[i],
										new FileInputStream(files[i]))
								.name(fileNames[i]).deploy();
				}
			}
		} catch (Exception e) {
			log.error("上传失败");
			e.printStackTrace();
			throw e;
		}
	}


	@Override
	public String startProcessVarNOAssigee(String processKey, String formId,
			String initiator, String busi_desc, Map<String, Object> map) {
		Map<String, Object> var = new HashMap<String, Object>();
		var.put("formId", formId); // 业务ID
		var.put("initiator",initiator); // 任务发起人
		var.put("processKey", processKey); // 流程key
		var.put("busi_desc", busi_desc);//业务记录的标志字段的简单说明
		if(map != null) var.putAll(map);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(
				var.get("processKey").toString(), var);
		return pi.getId();
		
	}

}
