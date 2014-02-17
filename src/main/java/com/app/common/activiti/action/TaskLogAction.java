package com.app.common.activiti.action;

import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.activiti.model.TaskLog;
import com.app.ea.model.User;
import com.utils.time.TimeUtil;

/**
 * @author chenzhijian
 */
@Component("tasklogAction")
@Scope("prototype")
public class TaskLogAction extends BaseProcessAction {

	//private final Logger log = LoggerFactory.getLogger(TaskLogAction.class);
	public TaskLog model = new TaskLog();
	private Task task;
	
	public Task getTask(String taskId){
		if(task == null){
			task = infActiviti.getTaskById(taskId);
		}
		return task;
	}
	
	public String list(){
		String taskId = getpara("taskId");
		String processInstanceId = getpara("processInstanceId");
		if(processInstanceId.equals(""))
			processInstanceId = getTask(taskId).getProcessInstanceId();

		rhs.put("list", baseDao.find("from TaskLog where processInstanceId='" + processInstanceId + "' order by createTime desc"));
		rhs.put("taskId", taskId);
		rhs.put("processInstanceId", processInstanceId);
		return "list";
	}
	
//	public String load() throws Exception {
//		String id = getpara("id");
//		if ("".equals(getpara("id"))) {
//			rhs.put("bugFix", null);
//		} else {
//			BugFix bugFix = (BugFix) baseDao.loadById("BugFix", Long.parseLong(id));
//			rhs.put("bugFix", bugFix);
//		}
//		return "success";
//	}
	
	public String save() throws Exception {
		Task t = getTask(getpara("taskId"));
		User user = (User) getSessionValue("userlogined");
		model = new TaskLog();
		model.setLog(getpara("log"));
		model.setTaskId(t.getId());
		model.setTaskName(t.getName());
		model.setProcessInstanceId(t.getProcessInstanceId());
		model.setCreateTime(TimeUtil.getTimeStr("yyyy-MM-dd HH:mm:ss"));
		model.setUserAccount(user.getAccount());
		model.setUserName(user.getName());
		baseDao.create(model);
//		if (model.getId() == null || model.getId().toString().equals("")) { // create
//			baseDao.create(model);
//		} else { // edit
//			baseDao.update(model);
//		}
		return list();
	}
	
	public String export(){
		String taskId = getpara("taskId");
		String processInstanceId = getpara("processInstanceId");
		if(processInstanceId.equals(""))
			processInstanceId = getTask(taskId).getProcessInstanceId();
		
		rhs.put("list", baseDao.find("from TaskLog where processInstanceId='" + processInstanceId + "' order by createTime desc"));
		return "success";
	}

	public String delete() throws Exception {
		TaskLog model = new TaskLog();
		model.setId(Long.parseLong(getpara("id")));
		baseDao.delete(model);
		return list();
	}
}
