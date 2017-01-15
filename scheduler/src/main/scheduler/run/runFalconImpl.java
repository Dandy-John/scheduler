package scheduler.run;

import java.util.List;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.listener.PreciseRaceDetector;
import gov.nasa.jpf.report.ConsolePublisher;
import scheduler.enumerate.ListenerState;
import scheduler.listener.FalconImplListener;
import scheduler.listener.FalconImplListener.ReadWriteNode;
import scheduler.listener.FalconImplListener.SequenceMessage;
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
		
		/*≤‚ ‘”√
		FalconImplListener.priority.put("main", 3);
		FalconImplListener.priority.put("Thread-1", 2);
		//FalconImplListener.priority.put("Thread-2", 1);
		//jpf.addPropertyListener(new _UseForTest(jpf));
		
		_UseForTest.state = ListenerState.RECORD;
		*/
		
		FalconImplListener listener = new FalconImplListener();
		
		Config config = new Config(str);
		//JPF jpf = new JPF(config);
		//jpf.addPropertyListener(listener);
		/*
		String[] str2 = {
				"+race.include=number",
				"+race.exclude="
			};
		jpf.addPropertyListener(new PreciseRaceDetector(new Config(str2)));
		*/
		
		//jpf.run();
		
		for (int i = 0; i < 50; i++) {
			JPF jpf = new JPF(config);
			jpf.addPropertyListener(listener);
			jpf.run();
		}
		
		List<SequenceMessage> sequenceMessages = listener.getDataCollection();
		for (SequenceMessage sm : sequenceMessages) {
			sm.RWNodes = sm.RWNodesfilter(null, "num", null, null, null);
			for (ReadWriteNode node : sm.RWNodes) {
				System.out.println("element:" + node.element + "\tfield:" + node.field 
						+ "\ttype:" + node.type + "\tthread:" + node.thread + "\tline:" + node.line);
			}
			String message = "";
			if (sm.isSuccess == false) {
				message = sm.errorMessage;
			}
			System.out.println("isSuccess:" + sm.isSuccess + "\t" + message);
		}
	}
}
