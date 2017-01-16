package scheduler.run;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import scheduler.listener.FalconImplListener;

public class RunTest {
	public static void main(String[] args) {
		String[] str = new String[]{
				"+classpath=build/examples", 
				"+search.class=scheduler.search.WithoutBacktrack", 
				"ReadWriteMonitor"};
		
		FalconImplListener listener = new FalconImplListener(new Config(new String[] {
				"+filter.field=num"
		}));
		
		Config config = new Config(str);
		
		JPF jpf = new JPF(config);
		jpf.addPropertyListener(listener);
		jpf.run();
	}
}
