package scheduler.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/*
 * ִ�����е�ÿһ���ڵ�ļ�¼
 */
@XmlRootElement(name = "step")
@XmlAccessorType(XmlAccessType.FIELD)
public class Step {
	
	@XmlElement
	public String thread;
	
	@XmlElement
	public String message;
	
	public Step() {
		
	}
	
	public Step(String thread, String message) {
		this.thread = thread;
		this.message = message;
	}
}