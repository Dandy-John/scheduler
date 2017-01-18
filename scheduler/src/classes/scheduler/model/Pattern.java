package scheduler.model;

import java.util.ArrayList;
import java.util.List;

public class Pattern {
	public List<ReadWriteNode> nodes;
	
	public Pattern() {
		nodes = new ArrayList<ReadWriteNode>();
	}
	
	public static List<Pattern> getInterleavings() {
		List<Pattern> patterns = new ArrayList<Pattern>();
		Pattern p;
		
		//R1-W2-R1
		p = new Pattern();
		p.nodes.add(new ReadWriteNode(null, null, "READ", "t1", null, false));
		p.nodes.add(new ReadWriteNode(null, null, "WRITE", "t2", null, false));
		p.nodes.add(new ReadWriteNode(null, null, "READ", "t1", null, false));
		patterns.add(p);
		
		//W1-W2-R1
		p = new Pattern();
		p.nodes.add(new ReadWriteNode(null, null, "WRITE", "t1", null, false));
		p.nodes.add(new ReadWriteNode(null, null, "WRITE", "t2", null, false));
		p.nodes.add(new ReadWriteNode(null, null, "READ", "t1", null, false));
		patterns.add(p);
		
		//W1-R2-W1
		p = new Pattern();
		p.nodes.add(new ReadWriteNode(null, null, "WRITE", "t1", null, false));
		p.nodes.add(new ReadWriteNode(null, null, "READ", "t2", null, false));
		p.nodes.add(new ReadWriteNode(null, null, "WRITE", "t1", null, false));
		patterns.add(p);
		
		//R1-W2-W1
		p = new Pattern();
		p.nodes.add(new ReadWriteNode(null, null, "READ", "t1", null, false));
		p.nodes.add(new ReadWriteNode(null, null, "WRITE", "t2", null, false));
		p.nodes.add(new ReadWriteNode(null, null, "WRITE", "t1", null, false));
		patterns.add(p);
		
		//W1-W2-W1
		p = new Pattern();
		p.nodes.add(new ReadWriteNode(null, null, "WRITE", "t1", null, false));
		p.nodes.add(new ReadWriteNode(null, null, "WRITE", "t2", null, false));
		p.nodes.add(new ReadWriteNode(null, null, "WRITE", "t1", null, false));
		patterns.add(p);
		
		return patterns;
	}
	
	public boolean equals(Pattern pattern) {
		for (int i = 0; i < pattern.nodes.size(); i++) {
			if (!this.nodes.get(i).equals(pattern.nodes.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		String result = "Pattern {\n";
		for (ReadWriteNode node : nodes) {
			result += ("\t" + node.toString() + "\n");
		}
		result += "}";
		return result;
	}
}
