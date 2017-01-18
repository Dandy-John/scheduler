import java.util.ArrayList;
import java.util.List;

import scheduler.model.ReadWriteNode;

public class Test {
	public static void main(String[] args) {
		ReadWriteNode node1 = new ReadWriteNode("1", null, null, null, null, false);
		ReadWriteNode node2 = new ReadWriteNode("1", null, null, null, null, false);
		System.out.println(node1.equals(node2));
		
		List<ReadWriteNode> nodes = new ArrayList<ReadWriteNode>();
		nodes.add(node1);
		System.out.println(nodes.contains(node2));
	}
}
