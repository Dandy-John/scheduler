package scheduler.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * ��¼���������ص�ִ����Ϣ
 */
@XmlRootElement(name = "readWriteStep")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadWriteStep {
	
	//ִ�в������߳�����
	@XmlElement
	public String threadName;
	
	//�������������
	@XmlElement
	public String objectName;
	
	//�������ͣ�����д��
	@XmlElement
	public String type;
	
	//ִ�в����Ĵ���λ��
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
