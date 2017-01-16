package scheduler.listener;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.bytecode.FieldInstruction;
import gov.nasa.jpf.vm.choice.ThreadChoiceFromSet;
import scheduler.enumerate.ListenerState;

public class FalconImplListener extends PropertyListenerAdapter {
	
	private Config conf;
	
	//读写相关的instruction节点
	public class ReadWriteNode {
		public String element;
		public String field;
		public String type;
		public String thread;
		public String line;
		
		public ReadWriteNode(String element, String field, String type, String thread, String line) {
			this.element = element;
			this.field = field;
			this.type = type;
			this.thread = thread;
			this.line = line;
		}
	}
	
	class ChoiceGeneratorNode {
		public String thread;
		public String message;
		
		public ChoiceGeneratorNode(String thread, String message) {
			this.thread = thread;
			this.message = message;
		}
	}
	
	//一次运行得到的信息
	public class SequenceMessage {
		public List<ReadWriteNode> RWNodes;
		public List<ChoiceGeneratorNode> CGNodes;
		public boolean isSuccess;
		public String errorMessage;
		
		public List<ReadWriteNode> RWNodesfilter(String element, String field, String type, String thread, String line) {
			List<ReadWriteNode> result = new ArrayList<ReadWriteNode>();
			for (ReadWriteNode node : RWNodes) {
				boolean in = true;

				if (element != null && !node.element.contains(element)) {
					in = false;
				}
				else if (field != null && !node.field.contains(field)) {
					in = false;
				}
				else if (type != null && !node.type.equals(type)) {
					in = false;
				}
				else if (thread != null && !node.thread.equals(thread)) {
					in = false;
				}
				else if (line != null && !node.line.contains(line)) {
					in = false;
				}
				
				if (in) {
					result.add(node);
				}
			}
			return result;
		}
	}
	
	//多次运行得到的信息
	static class DataCollection {
		public static List<SequenceMessage> sequenceMessages = new ArrayList<SequenceMessage>();
		
		public static List<SequenceMessage> getAllPassedSequences() {
			List<SequenceMessage> result = new ArrayList<SequenceMessage>();
			for (SequenceMessage sm : sequenceMessages) {
				if (sm.isSuccess) {
					result.add(sm);
				}
			}
			return result;
		}
		
		public static List<SequenceMessage> getAllFailedSequences() {
			List<SequenceMessage> result = new ArrayList<SequenceMessage>();
			for (SequenceMessage sm : sequenceMessages) {
				if (!sm.isSuccess) {
					result.add(sm);
				}
			}
			return result;
		}
	}
	
	public FalconImplListener(Config conf) {
		super();
		this.conf = conf;
	}
	
	public List<SequenceMessage> getDataCollection() {
		return DataCollection.sequenceMessages;
	}
	
	public List<SequenceMessage> getAllPassedSequences() {
		return DataCollection.getAllPassedSequences();
	}
	
	public List<SequenceMessage> getAllFailedSequences() {
		return DataCollection.getAllFailedSequences();
	}
	
	@Override
	public void searchStarted(Search search) {
		SequenceMessage sm = new SequenceMessage();
		sm.isSuccess = true;
		sm.errorMessage = null;
		DataCollection.sequenceMessages.add(sm);
	}
	
	@Override
	public void propertyViolated(Search search) {
		SequenceMessage sm = DataCollection.sequenceMessages.get(DataCollection.sequenceMessages.size() - 1);
		sm.isSuccess = false;
		sm.errorMessage = search.getCurrentError().getDetails();
	}
	
	@Override
	public void searchFinished(Search search) {
		SequenceMessage sm = DataCollection.sequenceMessages.get(DataCollection.sequenceMessages.size() - 1);
		
		String elementFilter = conf.getProperty("filter.element", null);
		String fieldFilter = conf.getProperty("filter.field", null);
		String typeFilter = conf.getProperty("filter.type", null);
		String threadFilter = conf.getProperty("filter.thread", null);
		String lineFilter = conf.getProperty("filter.line", null);
		
		sm.RWNodes = sm.RWNodesfilter(elementFilter, fieldFilter, typeFilter, threadFilter, lineFilter);
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
	
	@Override
	public void choiceGeneratorSet (VM vm, ChoiceGenerator<?> newCG) {
		if (newCG.isSchedulingPoint()) {
			newCG.randomize();
		}
	}
	
	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
		if (executedInstruction instanceof FieldInstruction) {
			FieldInstruction fins = (FieldInstruction)executedInstruction;
			FieldInfo fi = fins.getFieldInfo();
			ElementInfo ei = fins.getElementInfo(currentThread);
			
			SequenceMessage sm = DataCollection.sequenceMessages.get(DataCollection.sequenceMessages.size() - 1);
			if (sm.RWNodes == null) {
				sm.RWNodes = new ArrayList<ReadWriteNode>();
			}
			
			String type = null;
			String finsClass = fins.getClass().getName();
			if (finsClass.contains("GET")) {
				type = "READ";
			}
			else if (finsClass.contains("PUT")) {
				type = "WRITE";
			}
			
			assert type != null;
			ReadWriteNode RWNode = new ReadWriteNode(ei.toString(), fi.getName(), type, currentThread.getName(), fins.getFileLocation());
			sm.RWNodes.add(RWNode);
		}
	}
}
