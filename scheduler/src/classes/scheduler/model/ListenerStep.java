package scheduler.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/*
 * 存储jpf一次执行的整个流程，可用于执行序列的再现
 */
@XmlRootElement(name = "listenerStep")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListenerStep {
	
	@XmlElement(name = "step")
	public List<Step> steps;
	
	@XmlElement(name = "index")
	public int index;
	
	@XmlElement(name = "readWriteStep")
	public List<ReadWriteStep> readWriteSteps;
	
	public ListenerStep() {
		steps = new ArrayList<Step>();
		readWriteSteps = new ArrayList<ReadWriteStep>();
		index = 0;
	}
	
	public void addStep(String thread, String message) {
		steps.add(new Step(thread, message));
	}
	
	public void addReadWriteStep(String threadName, String objectName, String type, String location, String offset) {
		readWriteSteps.add(new ReadWriteStep(threadName, objectName, type, location, offset));
	}
	
	@Override
	public String toString() {
		String result = "index=" + index + " steps {\n";
		for (int i = 0; i < steps.size(); i++) {
			result += "\t" + i + " thread:" + steps.get(i).thread + " message:" + steps.get(i).message + "\n";
		}
		result += "}\nreadWriteSteps {\n";
		for (int i = 0; i < readWriteSteps.size(); i++) {
			ReadWriteStep s = readWriteSteps.get(i);
			result += "\t" + i + " threadName:" + s.threadName + " objectName:" + s.objectName 
					+ " type:" + s.type + " location:" + s.location + " offset:" + s.offset + "\n";
		}
		result += "}";
		
		return result;
	}
}