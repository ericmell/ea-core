package com.app.common.activiti.action;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.common.activiti.api.ProcessInstanceStatus;
import com.app.common.activiti.model.ProcessModel;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.common.spring.ssh.page.Pagination;


/**
 * @author chenzhijian
 */
@Component("activitiAction")
@Scope("prototype")
public class ActivitiAction extends BaseProcessAction {
	
	private final Logger log = LoggerFactory.getLogger(ActivitiAction.class);
	    
	/**
	 * 开启流程，传入的request必须有以下参数：
	 * id：业务表id
	 * assignee：开始流程后的处理人
	 * processKey：流程key
	 * busiDesc：该流程描述信息
	 * model：业务表类名
	 * @return
	 * @throws Exception
	 */
	public String start_process() throws Exception{
		log.debug("start_process()");
		String id = getpara("id");
		String assignee = getpara("assignee");
		String processKey = getpara("processKey");
		String busiDesc = getpara("busiDesc");
		busiDesc="工单处理";
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("model", getpara("model"));
		map.put("startUserName", getpara(getCurrentAccount()));
		String pid = infActiviti.startProcessAssigneeVar(processKey, id, busiDesc, getCurrentAccount(), assignee, map);
		ProcessModel pm = (ProcessModel)baseDao.loadById(getpara("model"), Long.parseLong(id));
		pm.setProcessInstanceId(pid);
		baseDao.update(pm);
		
		return "success";
	}
	
	/**
	 * 打开任务
	 * 传入的request必须有以下参数
	 * taskId：task的Id
	 * taskPage：处理task的页面路径
	 * model：业务表类名
	 * formId：业务表id
	 * formProperties：控制表单的名称
	 * @return
	 * @throws Exception
	 */
	public String open_task() throws Exception{
		log.debug("open_task()");
		String taskId = getpara("taskId");
		String taskPage = getpara("taskPage");
		String modelStr = getpara("model");
		String formId = (String)infActiviti.getVariableByTaskId(taskId, "formId");
		if (formId == null || formId.equals("")) {
			rhs.put("model", null);
		} else {
			BaseModel model = (BaseModel) baseDao.loadById(modelStr, Long.parseLong(formId));
			rhs.put("model", model);
		}
		
		/* add by chenzhijian 20130419 -s */
		// 获取 formProperties 配置文件
		String formProperties = getpara("formProperties");
		HashMap<String, String> formPro = new HashMap<String, String>();
		Properties p = new Properties();
		try{
			//String filepath = System.getProperty("webroot", "./src/main/webapp/");
			String filepath =getWebroot();
			// eg: app/manager/wo/wo.properties
			filepath += "/app/manager/" + modelStr.toLowerCase() + "/" + modelStr.toLowerCase() + ".properties";
			FileInputStream in = new FileInputStream(filepath);
			p.load(in);
			in.close();
			Set<String> set = p.stringPropertyNames();
			for(String s : set){
				if(s.startsWith("default")){
					formPro.put(s.replace("default.", ""), p.getProperty(s));
				}
			}
			for(String s : set){
				if(s.startsWith(formProperties)){
					formPro.put(s.replace(formProperties + ".", ""), p.getProperty(s));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		rhs.put("formPro", formPro);
		/* add by chenzhijian 20130419 -e */
		
		Task task = infActiviti.getTaskById(taskId);
		rhs.put("task", task);
		rhs.put("taskPage", taskPage);
		
		return "success";
	}
	
	/**
	 * 代办任务查询，有2中类型的任务
	 * @return
	 */
	public String menu_task_list() {
		String pageId = getpara("pageId");
		String maxSize = getpara("maxSize");
		if(pageId.equals("")) pageId = "1";
		if(maxSize.equals("")) maxSize = "20";
		Map<String, Object> map = infActiviti.getOaTaskListByAccount(getCurrentAccount(), Integer.parseInt(pageId), Integer.parseInt(maxSize));
		
		Pagination p = (Pagination)map.get("pagination");
		rhs.put("oatasklist", map.get("dataList"));
		rhs.put("maxSize", p.getMaxSize());
		rhs.put("count", p.getTotalSize());
		rhs.put("maxPage", p.getTotalPage());
		rhs.put("currentPage", p.getCurrentPage());
		return "success";
	}
	
	/**
	 * 打开登录用户的历史任务列表
	 * @return
	 */
	public String menu_history_task_list() {
		rhs.put("oatasklist", infActiviti.getHistoryTaskListByAccount(getCurrentAccount()));
		return "success";
	}
	
	/**
	 * 查询已经部署流程文件
	 * @return
	 */
	public String menu_deployment() {
		List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymenTime().desc().list();
		rhs.put("list", list);
		return "success";
	}
	
	/**
	 * 查询部署了多少个流程
	 * @return
	 */
	public String menu_process_definition() {
		List<ProcessDefinition> list = repositoryService
				.createProcessDefinitionQuery().latestVersion().list();
		rhs.put("list", list);
		return "success";
	}
	
	/**
	 * 上传流程文件
	 * @return
	 */
	public String upload_process_file() {
		try {
			infActiviti.deployByRequest(); // 返回上传历史
		} catch (Exception e) {
			log.error("上传失败");
			rhs.put("info_type", "error");
			rhs.put("info", "上传失败");
		}
		return "success";
	}
	
	/**
	 * 跳转到流程图型显示页面
	 * @return
	 */
	public String process_diagram() {
		String processDefinitionId = getpara("processDefinitionId");
		String processInstanceId = getpara("processInstanceId");
		
		if(processDefinitionId.equals("")){
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			processDefinitionId = processInstance.getProcessDefinitionId();
		}
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		InputStream in = repositoryService.getResourceAsStream(
				processDefinition.getDeploymentId(),
				processDefinition.getDiagramResourceName());
		try {
			rhs.put("imgWidth", ImageIO.read(in).getWidth());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		rhs.put("processDefinitionId", processDefinitionId);
		rhs.put("processInstanceId", processInstanceId);
		return "success";
	}
	/**
	 * 流程图型显示
	 * @return
	 */
	public String process_diagram_simple() {
		try {
			HttpServletResponse resp = ServletActionContext.getResponse();
			String processDefinitionId = getpara("processDefinitionId");
			ProcessDefinition processDefinition = repositoryService
					.createProcessDefinitionQuery()
					.processDefinitionId(processDefinitionId).singleResult();
			InputStream in = repositoryService.getResourceAsStream(
					processDefinition.getDeploymentId(),
					processDefinition.getDiagramResourceName());
			ImageIO.read(in).getWidth();
			resp.setContentType("image/png");
			ServletOutputStream out = resp.getOutputStream();
			BufferedInputStream bin = new BufferedInputStream(in);
			byte[] b = new byte[1024];
			int l = bin.read(b);
			while (l != -1) {
				out.write(b);
				l = bin.read(b);
			}
			bin.close();
			in.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 流程图型显示，高亮流程所在环节
	 * @return
	 */
	public String process_diagram_complex(){
		try {
			HttpServletResponse resp = ServletActionContext.getResponse();
			
			String processInstanceId = getpara("processInstanceId");
			if(!infActiviti.processInstanceStatus(processInstanceId).equals(ProcessInstanceStatus.Done)){
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
				String processDefinitionId = processInstance.getProcessDefinitionId();
				
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);
				log.debug(runtimeService.getActiveActivityIds(processInstanceId).toString());
				InputStream in = ProcessDiagramGenerator.generateDiagram((ProcessDefinitionEntity)processDefinition, "png", runtimeService.getActiveActivityIds(processInstanceId));
				//InputStream in = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
				
				resp.setContentType("image/png");
				
				BufferedInputStream bin = new BufferedInputStream(in);
				ServletOutputStream out = resp.getOutputStream();
				byte[] b = new byte[1024];
				int l = bin.read(b);
				while(l != -1){
					out.write(b);
					l = bin.read(b);
				}
				bin.close();
				in.close();
				out.flush();
				out.close();
			}else{
				PrintWriter out = resp.getWriter();
				out.write("The Process have been completed......");
				out.flush();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String process_definition_activity(){
		String processDefinitionId = getpara("processDefinitionId");
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);
		List<ActivityImpl> activityImplList = processDefinition.getActivities();
		for(ActivityImpl a : activityImplList){
			log.debug(a.getProperties().toString());
		}
		rhs.put("list", activityImplList);
		return "success";
	}
	
	/* add by chenzhijian 20130423 -s*/
	/**
	 * 获取下一环节的任务名
	 * @return
	 */
	public String next_tasks(){
		String taskId = getpara("taskId");
		rhs.put("list", infActiviti.getNextTasks(taskId));
		return "success";
	}
	/* add by chenzhijian 20130423 -e*/
	
	public void getPageData(String sql) throws Exception {
		String pageId = getpara("pageId");
		String maxSize = getpara("maxSize");
		
		if (maxSize == null || "".equals(maxSize)) {
			maxSize = (String) getSessionValue("maxSize");
			if (maxSize == null || "".equals(maxSize))
				maxSize = "20";
		}else{
			pagination.setMaxSize(Integer.parseInt(maxSize));
		}
		
		if (pageId == null || "".equals(pageId)) {
			pageId = (String) getSessionValue("pageId");
			if (pageId == null || "".equals(pageId))
				pageId = "1";
		}
		putSessionValue("pageId", pageId);
		putSessionValue("maxSize", maxSize);
		pagination.setCurrentPage(Integer.parseInt(pageId));
		List tpltb1List = baseDao.page(sql, pagination);
		rhs.put("dataList", tpltb1List);
		List countList = baseDao.find(sql);
		rhs.put("maxSize", maxSize);
		rhs.put("count", countList.size());
		rhs.put("maxPage",
				countList.size() % pagination.getMaxSize() > 0 ? countList
						.size() / pagination.getMaxSize() + 1 : countList
						.size() / pagination.getMaxSize());
		rhs.put("currentPage", Integer.parseInt(pageId));
	}
	
	public static void main(String[] args){
		System.out.println(11/10);
	}
}
