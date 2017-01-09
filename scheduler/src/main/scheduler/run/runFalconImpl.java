package scheduler.run;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import scheduler.enumerate.ListenerState;
import scheduler.listener.FalconImplListener;

public class runFalconImpl {
	public static void main(String[] args){
		String[] str = new String[]{
				"+classpath=build/examples", 
				"+search.class=scheduler.search.WithoutBacktrack", 
				"+listener=scheduler.listener.FalconImplListener", 
				"SimpleProgram"};
		
		FalconImplListener.priority.put("main", 3);
		FalconImplListener.priority.put("t2", 2);
		FalconImplListener.priority.put("t1", 1);
		 
		FalconImplListener.state = ListenerState.RECORD;
		
		Config config = new Config(str);
		JPF jpf = new JPF(config);
		jpf.run();
		
	}
}
