
public class CheckField {
	static InstanceExample ex1 = new InstanceExample();
	static InstanceExample ex2 = new InstanceExample();
	public static void main(String[] args) {
		new Thread() {
			public void run() {
				ex2.number = 12;
				ex1.number = 222;
			}
		}.start();
		ex1.number = 33;
		ex2.number = 42;
		ex1.number = 445;
		
		/*
		new Thread(){
			public void run() {
				System.out.println(ex1.number);
			}
		}.start();
		new Thread(){
			public void run() {
				System.out.println(ex1.number);
			}
		}.start();
		*/
	}
}
