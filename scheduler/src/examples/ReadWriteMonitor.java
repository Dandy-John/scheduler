
public class ReadWriteMonitor {
	public static int num1;
	public int num2;
	private int num3;
	public static void main(String[] args) {
		ReadWriteMonitor instance = new ReadWriteMonitor();
		num1 = 12;
		instance.num2 = 3;
		instance.num3 = 1;
		int a = instance.num2;
	}
}
