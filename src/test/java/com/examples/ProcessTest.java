package com.examples;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.utils.time.TimeUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring.xml")
public class ProcessTest {
	private final Logger log = LoggerFactory.getLogger(ProcessTest.class);
  @Autowired
  private RuntimeService runtimeService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private RepositoryService repositoryService;
  @Autowired
  private HistoryService historyService;
  @Autowired
  private ManagementService managementService;
  @Autowired
  private IdentityService identityService;
  @Autowired
  private FormService formService;
  @Autowired
  private BasicDataSource dataSource;
  /*
  ClassPathXmlApplicationContext applicationContext = 
		    new ClassPathXmlApplicationContext("org/activiti/examples/spring/SpringTransactionIntegrationTest-context.xml");
  */
 
 
  @Test
  public void printDatabase() {
	 log.debug("测试启动的api");
	 log.debug(dataSource.getUrl());
	 //log.debug(impActiviti.getDatabase());
  }
 
  
  
  public void printTaskList(List<Task> taskList) {
	  for (Iterator iterator = taskList.iterator(); iterator.hasNext();) {
			Task task = (Task) iterator.next();
			log.debug("id:"+task.getId()
					+" 分配人:"+task.getAssignee()
					+" 拥有者:"+task.getOwner()
					+" 优先级:"+task.getPriority()
					+" 任务名:"+task.getName()
					
					+" 描述:"+task.getDescription()
					+" 时间"+TimeUtil.getTimeStrByDate(task.getCreateTime(), "yyyy-MM-dd hh-mm-ss")
		            +" 到期时间:"+task.getDueDate()
		            +" 拥ExecutionId者:"+task.getExecutionId()
		            +"getTaskDefinitionKey:"+task.getTaskDefinitionKey()
		            +"父任务ID:"+task.getParentTaskId()
		           
		           
			);
		}
  }
  
  /*
  @Test
  public void getDatabase() {
	 log.debug("测试启动的api"+infActiviti.getDatabase());
	
  }
  
 
 
  @Test
  public void deploy() throws FileNotFoundException {
	  infActiviti.deploy("src/main/resources/bpmn/SDR_h3g_Solution_Design_Simple_Solutions.bpmn");
	 
  }
  @Test
  public void task_java_service() throws FileNotFoundException{
	  infActiviti.deploy("src/main/resources/bpmn/task_java_service.bpmn");
	  runtimeService.startProcessInstanceByKey("task_java_service");
  }
  
  
  @Test
  public void helloprocess() throws FileNotFoundException{
	  infActiviti.deploy("src/main/resources/bpmn/hello_print.bpmn20.xml");
	  runtimeService.startProcessInstanceByKey("helloProcess");
  }
  */
  
}      

