package scheduler.run;

import java.util.List;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import scheduler.listener.FalconImplListener;
import scheduler.model.DataCollection;
import scheduler.model.Pattern;
import scheduler.model.ReadWriteNode;
import scheduler.model.SequenceMessage;

public class Implementation {
	public static void main(String[] args){
		String[] str = new String[]{
				"+classpath=build/examples", 
				"+search.class=scheduler.search.WithoutBacktrack", 
				"CheckField"};
		
		FalconImplListener listener = new FalconImplListener(new Config(new String[] {
				"+filter.field=num"
		}));
		
		Config config = new Config(str);
		
		for (int i = 0; i < 50; i++) {
			JPF jpf = new JPF(config);
			jpf.addPropertyListener(listener);
			jpf.run();
		}
		
		System.out.println("\n---------------------------------------------------------");
		
		List<SequenceMessage> sequences = listener.getDataCollection();
		List<SequenceMessage> failedSequences = listener.getAllFailedSequences();
		List<SequenceMessage> passedSequences = listener.getAllPassedSequences();
		List<Pattern> patterns = DataCollection.getAllPatterns();
		
		List<Pattern> interleavings = Pattern.getInterleavings();
		
		for (SequenceMessage sm : sequences) {
			sm.removeDeprecatedRWNodes();
		}
		
		int allPassed = passedSequences.size();
		int allFailed = failedSequences.size();
		for (Pattern pattern : patterns) {
			int passed = 0;
			int failed = 0;
			for (SequenceMessage sm : passedSequences) {
				if (sm.containsPattern(pattern)) {
					passed++;
				}
			}
			for (SequenceMessage sm : failedSequences) {
				if (sm.containsPattern(pattern)) {
					failed++;
				}
			}
			double suspiciousness = ((double)failed / allFailed)/((double)failed / allFailed + (double)passed / allPassed);
			System.out.println("----------------------------------------------------");
			System.out.println(pattern);
			System.out.println("suspiciousness:" + suspiciousness);
			System.out.println("----------------------------------------------------");
		}
	}
}
