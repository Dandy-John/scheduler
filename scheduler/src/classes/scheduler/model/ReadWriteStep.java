package scheduler.model;

/*
 * 记录共享变量相关的执行信息
 */
public class ReadWriteStep {
	
	//执行操作的线程名称
	public String threadName;
	
	//共享变量的名称
	public String objectName;
	
	//操作类型（读或写）
	public String type;
	
	//执行操作的代码位置
	public String location;
	
	
	public ReadWriteStep(String threadName, String objectName, String type, String location) {
		this.threadName = threadName;
		this.objectName = objectName;
		this.type = type;
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "{\n\tThread Name: " + threadName
				+ "\n\tObject Name: " + objectName
				+ "\n\tType: " + type
				+ "\n\tLocation: " + location
				+ "\n}";
	}
}
