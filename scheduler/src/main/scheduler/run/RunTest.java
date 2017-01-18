package scheduler.run;

import java.util.List;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import scheduler.listener.FalconImplListener;
import scheduler.listener._UseForTest;
import scheduler.model.ReadWriteNode;
import scheduler.model.SequenceMessage;

public class RunTest {
	public static void main(String[] args) {
		String[] str = new String[]{
				"+classpath=build/examples", 
				"+search.class=scheduler.search.WithoutBacktrack", 
				"ReadWriteMonitor"};
		
		
		FalconImplListener listener = new FalconImplListener(new Config(new String[] {
				"+filter.field=num"
		}));
		
		//_UseForTest listener = new _UseForTest();
		
		Config config = new Config(str);
		
		for (int i = 0; i < 10; i++) {
			JPF jpf = new JPF(config);
			jpf.addPropertyListener(listener);
			jpf.run();
		}
		List<SequenceMessage> dataCollection = listener.getDataCollection();
		
		for (SequenceMessage sm : dataCollection) {
			//sm.removeDeprecatedRWNodes();
			List<ReadWriteNode> nodes = sm.RWNodes;
			for (ReadWriteNode node : nodes) {
				System.out.println("element:" + node.element + "\tfield:" + node.field 
						+ "\ttype:" + node.type + "\tthread:" + node.thread + "\tline:" + node.line
						+ "\tchanged:" + node.changed);
			}
			String message = "";
			if (sm.isSuccess == false) {
				message = sm.errorMessage;
			}
			System.out.println("\nisSuccess:" + sm.isSuccess + "\t" + message);
			System.out.println("Vars:");
			List<ReadWriteNode> vars = sm.getAllVariables();
			for (ReadWriteNode var : vars) {
				System.out.println("element:\t" + var.element + "\tfield:\t" + var.field);
			}
			System.out.println("-----------------------------------------");
		}
		
	}
}
