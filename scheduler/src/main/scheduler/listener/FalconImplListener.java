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
import scheduler.model.*;

public class FalconImplListener extends PropertyListenerAdapter {
	
	private Config conf;
	
	private ElementInfo lastElement = null;
	private ReadWriteNode lastNode = null;
	private FieldInstruction lastIns = null;
	private boolean bo;
	
	private int oneSlot;
		
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
	public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
		if (instructionToExecute instanceof FieldInstruction && !((FieldInstruction) instructionToExecute).isRead()) {
			FieldInstruction fins = (FieldInstruction)instructionToExecute;
			FieldInfo fi = fins.getFieldInfo();
			ElementInfo ei = fins.getElementInfo(currentThread);
			if (fi.is1SlotField())
				oneSlot = ei.get1SlotField(fi);
			//twoSlot = ei.get2SlotField(fi);
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
			
			boolean changed = false;
			if (fins.isRead()) {
				type = "READ";
			}
			else {
				type = "WRITE";
				if (fi.is1SlotField() && ei.get1SlotField(fi) != oneSlot) {
					changed = true;
				}
			}
			
			ReadWriteNode RWNode = new ReadWriteNode(ei.toString(), fi.getName(), type, currentThread.getName(), fins.getFileLocation(), changed);
			sm.RWNodes.add(RWNode);
			
			if (lastNode != null && lastNode.element.equals(RWNode.element) && lastNode.field.equals(RWNode.field)
					&& lastNode.type.equals(RWNode.type) && lastNode.thread.equals(RWNode.thread) && lastNode.line.equals(RWNode.line)) {
				FieldInstruction lastInsn = lastIns; 
				System.out.println("same with last. " + (fins == lastIns));
			}
			
			lastNode = RWNode;
			lastElement = ei;
			lastIns = fins;
			bo = fins.isReferenceField();
			if (RWNode.line.contains("ReadWriteMonitor.java"))
				System.out.println(RWNode.line);
		}
	}
	
	@Override
	public void threadScheduled (VM vm, ThreadInfo scheduledThread) {
		System.out.println("scheduled");
	}
}
