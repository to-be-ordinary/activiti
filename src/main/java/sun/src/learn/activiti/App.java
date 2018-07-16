package sun.src.learn.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * Hello world!
 *
 */
public class App 
{
	//activiti使用的所有service都通过processEngine对象来创建，所以，首先要知道ProcessEngine是怎么来的
	public static ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
			.setJdbcUrl("jdbc:mysql://localhost:3306/actsrclearn")
			.setJdbcUsername("root")
			.setJdbcPassword("123456")
			.setJdbcDriver("com.mysql.jdbc.Driver")
			.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
			.buildProcessEngine();//在buildProcessEngine时会创建数据库表
    @SuppressWarnings("static-access")
	public static void main( String[] args )
    {
        System.out.println( "Actviti src learn!" );
        
        System.out.println("当前activiti版本：" + processEngine.VERSION);
        App app = new App();
        app.deploy();
        app.start();
    }
    
    public void deploy() {
    	try {
    		
    		processEngine.getRepositoryService().createDeployment()
    		.addClasspathResource("MyProcess.bpmn")
    		.addClasspathResource("MyProcess.png")
    		.deploy();
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    		System.out.println("=====部署失败==========");
    	}
    }
    @Test
    public void start() {
    	try {
    		Map<String, Object> variables = new HashMap<>();
    		variables.put("user1", "user1");
    		variables.put("users2", "user21,user22,user23");
    		processEngine.getRuntimeService().startProcessInstanceByKey("myProcess",variables);
    	}catch(Exception e) {
    		
    		System.out.println(e.getMessage());
    		System.out.println("=====启动失败==========");
    	}
    }
    
    @Test
    public void excute() {
    	
    	String taskId = "15011";
    	processEngine.getTaskService().complete(taskId);
    }
    
    //查询多人任务
    @Test
    public void groupTaskQuery() {
    	List<Task> tasks = processEngine.getTaskService().createTaskQuery()
    	.taskCandidateUser("user23")
    	.list();
    	
    	for (Task task : tasks) {
			
    		System.out.println(task.getId() + "\t" + task.getName());
    		//多人任务加人
    		processEngine.getTaskService().addCandidateUser(task.getId(), "user25");
    		//多人任务减人
    		processEngine.getTaskService().deleteCandidateUser(task.getId(), "user25");
		}
    }
    //认领多人任务
    @Test
    public void groupTaskClaim() {
    	processEngine.getTaskService().claim("2502", "usere24");
    }
}
