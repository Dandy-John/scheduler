
public class CheckField {
	static InstanceExample ex;
	private int num;
	public static void main(String[] args) throws InterruptedException {
		ex = new InstanceExample();
		Thread t1 = new Thread() {
			public void run() {
				ex.number = 12;
				ex.number = 13;
			}
		};
		Thread t2 = new Thread() {
			public void run() {
				int a = ex.number - 12;
				int b = 5 / a;
				System.out.println("a=" + a + " b=" + b);
			}
		};
		t1.start();
		t2.start();
	}
	
	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
}
