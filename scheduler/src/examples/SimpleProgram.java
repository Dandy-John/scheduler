public class SimpleProgram {
	
	public static int mainStatic = 2;
	
	public static void main(String[] args) 
			throws Exception{
		
		MyThread t1 = new MyThread(11);
		MyThread t2 = new MyThread(22);
		t1.setName("t1");
		t2.setName("t2");
		t1.setNeighbour(t2);
		t2.setNeighbour(t1);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println("Simple Program done.");

		//throw new Exception("123123123");

	}
}
