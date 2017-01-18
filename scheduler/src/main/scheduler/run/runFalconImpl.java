package scheduler.run;

import java.util.List;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.listener.PreciseRaceDetector;
import gov.nasa.jpf.report.ConsolePublisher;
import scheduler.enumerate.ListenerState;
import scheduler.listener.FalconImplListener;
import scheduler.listener._UseForTest;
import scheduler.model.DataCollection;
import scheduler.model.Pattern;
import scheduler.model.ReadWriteNode;
import scheduler.model.SequenceMessage;

public class RunFalconImpl {
	
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
		
		FalconImplListener listener = new FalconImplListener(new Config(new String[] {
				"+filter.field=num"
		}));
		
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
		
		for (int i = 0; i < 20; i++) {
			JPF jpf = new JPF(config);
			jpf.addPropertyListener(listener);
			jpf.run();
		}
		
		System.out.println("\n---------------------------------------------------------");
		
		List<SequenceMessage> sequences = listener.getDataCollection();
		List<SequenceMessage> failedSequences = listener.getAllFailedSequences();
		List<SequenceMessage> passedSequences = listener.getAllPassedSequences();
		for (SequenceMessage sm : failedSequences) {
			//sm.RWNodes = sm.RWNodesfilter(null, "num", null, null, null);
			sm.removeDeprecatedRWNodes();
			for (ReadWriteNode node : sm.RWNodes) {
				System.out.println("element:" + node.element + "\tfield:" + node.field 
						+ "\ttype:" + node.type + "\tthread:" + node.thread + "\tline:" + node.line + "\tchanged:" + node.changed);
			}
			String message = "";
			if (sm.isSuccess == false) {
				message = sm.errorMessage;
			}
			System.out.println("isSuccess:" + sm.isSuccess + "\t" + message);
		}
		
		List<Pattern> patterns = DataCollection.getAllPatterns();
		System.out.println("Patterns:");
		for (Pattern pattern : patterns) {
			System.out.println("pattern:");
			for (ReadWriteNode node : pattern.nodes) {
				System.out.println(node.toString());
			}
		}
	}
}
