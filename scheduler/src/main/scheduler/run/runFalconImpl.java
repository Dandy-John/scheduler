package scheduler.run;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.listener.PreciseRaceDetector;
import gov.nasa.jpf.report.ConsolePublisher;
import scheduler.enumerate.ListenerState;
import scheduler.listener.FalconImplListener;
import scheduler.listener._UseForTest;

public class runFalconImpl {
	
	public static void main(String[] args){
		String[] str = new String[]{
				"+classpath=build/examples", 
				"+search.class=scheduler.search.WithoutBacktrack", 
				//"+listener=scheduler.listener.FalconImplListener", 
				//"+listener=gov.nasa.jpf.listener.InsnCounter",
				//"SimpleProgram"};
				"CheckField"};
		
		FalconImplListener.priority.put("main", 3);
		FalconImplListener.priority.put("Thread-1", 2);
		//FalconImplListener.priority.put("Thread-2", 1);
		
		FalconImplListener.state = ListenerState.RECORD;
		
		Config config = new Config(str);
		JPF jpf = new JPF(config);
		jpf.addPropertyListener(new FalconImplListener(jpf));
		//jpf.addPropertyListener(new _UseForTest());
		
		/*
		String[] str2 = {
				"+race.include=number",
				"+race.exclude="
			};
		jpf.addPropertyListener(new PreciseRaceDetector(new Config(str2)));
		*/
		
		jpf.run();
		
	}
}
