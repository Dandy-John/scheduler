package scheduler.model;

/*
 * ��¼���������ص�ִ����Ϣ
 */
public class ReadWriteStep {
	
	//ִ�в������߳�����
	public String threadName;
	
	//�������������
	public String objectName;
	
	//�������ͣ�����д��
	public String type;
	
	//ִ�в����Ĵ���λ��
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
