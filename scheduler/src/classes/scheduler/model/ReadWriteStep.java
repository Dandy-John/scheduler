package scheduler.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * 记录共享变量相关的执行信息
 */
@XmlRootElement(name = "readWriteStep")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadWriteStep {
	
	//执行操作的线程名称
	@XmlElement
	public String threadName;
	
	//共享变量的名称
	@XmlElement
	public String objectName;
	
	//操作类型（读或写）
	@XmlElement
	public String type;
	
	//执行操作的代码位置
	@XmlElement
	public String location;
	
	@XmlElement
	public String offset;
	
	public ReadWriteStep() {
		
	}
	
	public ReadWriteStep(String threadName, String objectName, String type, String location, String offset) {
		this.threadName = threadName;
		this.objectName = objectName;
		this.type = type;
		this.location = location;
		this.offset = offset;
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
