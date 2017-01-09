package scheduler.listener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.bytecode.FieldInstruction;
import gov.nasa.jpf.vm.choice.ThreadChoiceFromSet;
import scheduler.enumerate.ListenerState;
import scheduler.io.XmlTransfer;
import scheduler.model.ListenerStep;
import scheduler.model.ReadWriteStep;
import scheduler.model.Step;

public class FalconImplListener extends PropertyListenerAdapter {
	public static Map<String, Integer> priority = new HashMap<String, Integer>();
	public static ListenerState state;
	public static List<ReadWriteStep> readWriteSteps = new ArrayList<ReadWriteStep>();
	
	public final String PATH = "f:\\xml.txt";
	
	public ListenerStep listenerStep;
	
	@Override
	public void choiceGeneratorSet (VM vm, ChoiceGenerator<?> newCG) {
		if (state == ListenerState.RECORD) {
			record(vm, newCG);
		}
		else if (state == ListenerState.REAPPEAR) {
			reappear(vm, newCG);
		}
	}
	
	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
		if (executedInstruction instanceof FieldInstruction) {
			sharedVarRecord(currentThread, executedInstruction);
		}
	}
	
	private void sharedVarRecord(ThreadInfo ti, Instruction ins) {
		FieldInstruction fins = (FieldInstruction)ins;
		//String fieldName = fins.getFieldName();
		//ElementInfo ei = fins.peekElementInfo(ti);
		//fins.getElementInfo(ti);
		//FieldInfo fi = fins.getFieldInfo();
		//MethodInfo mi = ins.getMethodInfo();
		String type = ((FieldInstruction) ins).getClass().getName();
		
		readWriteSteps.add(new ReadWriteStep(ti.getName(), fins.getFieldInfo().getFullName(), type, ins.getFilePos()));
	}
	
	private void record(VM vm, ChoiceGenerator<?> newCG) {
		if (newCG.isSchedulingPoint()) {
			
			ThreadChoiceFromSet tcgi = (ThreadChoiceFromSet) newCG;
			/*
			Object[] ti = tcgi.getAllChoices();
			int index = -1;
			int pri = -1;
			for (int i = 0; i < ti.length; i++) {
				int p = priority.get(((ThreadInfo)ti[i]).getName());
				if (p > pri) {
					pri = p;
					index = i;
				}
			}
			
			for (int i = 0; i < index; i++) {
				tcgi.advance();
			}
			*/
			newCG.randomize();
			int index = 0;
			//¼ÇÂ¼ÐòÁÐ
			
			ThreadInfo t = tcgi.getChoice(index);
			Instruction ins = t.getPC();
			
			listenerStep.add(t.getName(), ins.getFileLocation());
		}
	}
	
	private void reappear(VM vm, ChoiceGenerator<?> newCG) {
		if (newCG.isSchedulingPoint()) {
			ThreadChoiceFromSet tcgi = (ThreadChoiceFromSet) newCG;
			Step step = listenerStep.steps.get(listenerStep.index);
			Object[] ti = tcgi.getAllChoices();
			ThreadInfo t = null;
			for (int i = 0; i < ti.length; i++) {
				t = (ThreadInfo)ti[i];
				Instruction ins = t.getPC();
				if (t.getName().equals(step.thread) && ins.getFileLocation().equals(step.message)) {
					break;
				}
				tcgi.advance();
			}
			listenerStep.index++;
		}
	}
	
	@Override
	 public void searchStarted(Search search) {
	    //search.addProperty(this);
		if (state == ListenerState.RECORD) {
			listenerStep = new ListenerStep();
		}
		else if (state == ListenerState.REAPPEAR) {
			listenerStep = XmlTransfer.fromXML(PATH);
		}
	 }
	
	@Override
	public void searchFinished(Search search) {
		//System.out.println("Search Finished.");
		//System.out.println(listenerStep.toString());
		if (state == ListenerState.RECORD) {
			XmlTransfer.toXML(listenerStep, PATH);
		}
		else if (state == ListenerState.REAPPEAR) {

		}
		
		List<ReadWriteStep> result = filter(readWriteSteps);
		showReadWriteList(result);
	}
	
	private List<ReadWriteStep> filter(List<ReadWriteStep> steps) {
		List<ReadWriteStep> result = new ArrayList<ReadWriteStep>();
		for (ReadWriteStep step: steps) {
			int count = 0;
			for (ReadWriteStep p : steps) {
				if (!p.threadName.equals(step.threadName) && p.objectName.equals(step.objectName)) {
					count++;
				}
			}
			if (count != 0) {
				result.add(step);
			}
		}
		
		return result;
	}
	
	private void showReadWriteList(List<ReadWriteStep> steps) {
		for (ReadWriteStep step : steps) {
			String str = "";
			/*
			if (step.type.contains("GET")) {
				str += "READ ";
			}
			else if (step.type.contains("PUT")) {
				str += "Write ";
			}
			*/
			str += step.type;
			str += "\t";
			
			str += step.threadName;
			str += ",\t";
			str += step.objectName;
			System.out.println(str);
		}
	}
}
