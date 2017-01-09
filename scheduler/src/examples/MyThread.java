public class MyThread extends Thread{
	private int privateInt;
	
	public int publicInt;
	
	public static int staticInt;
	
	public MyThread(int number) {
		this.publicInt = number;
		this.privateInt = number + 2;
	}
	
	public void run() {
		int local = 1;
		MyThread.staticInt = publicInt;
		local = SimpleProgram.mainStatic;
		System.out.println("Thread " + publicInt + " seq 1");
		System.out.println("Thread " + publicInt + " seq 2");
	}
}