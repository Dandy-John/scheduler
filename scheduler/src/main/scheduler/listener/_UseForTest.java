package scheduler.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.report.ConsolePublisher;
import gov.nasa.jpf.report.Publisher;
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
import scheduler.io.XmlTransfer;
import scheduler.model.ListenerStep;
import scheduler.model.ReadWriteStep;
import scheduler.model.Step;

public class _UseForTest extends PropertyListenerAdapter{
	/*
	 * 测试代码1
	 */
	
	/*
	private int line = 28;
	private ElementInfo prev;
	private String className = "CheckField";
	
	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
		ThreadInfo ti = currentThread;
		Instruction ins = ti.getPC();
		if (ins instanceof FieldInstruction) {
			FieldInstruction fins = (FieldInstruction)ins;
			ElementInfo ei = fins.getElementInfo(ti);
			String str = ei.toString();
			ieldInfo fi = fins.getFieldInfo();
			if (fi.getFullName().contains("num")) {
				if (ins.getFilePos().equals(className + ".java:" + line)) {
					System.out.println("ed " + line + ": " + ei.get1SlotField(fi) + " " + ei.hashCode());
					System.out.println(str);
					System.out.println(prev == ei);
				}
			}
		}
	}
	
	@Override
	public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
		ThreadInfo ti = currentThread;
		Instruction ins = ti.getPC();
		if (ins instanceof FieldInstruction) {
			FieldInstruction fins = (FieldInstruction)ins;
			ElementInfo ei = fins.getElementInfo(ti);
			String str = ei.toString();
			FieldInfo fi = fins.getFieldInfo();
			if (fi.getFullName().contains("num")) {
				if (ins.getFilePos().equals(className + ".java:" + line)) {
					System.out.println("to " + line + ": " + ei.get1SlotField(fi) + " " + ei.hashCode());
					System.out.println(str);
					prev = ei;
				}
			}
		}
	}
	*/
	/*
	@Override
	public void choiceGeneratorSet (VM vm, ChoiceGenerator<?> newCG) {
		if (newCG instanceof ThreadChoiceFromSet) {
			ThreadInfo[] threads = ((ThreadChoiceFromSet) newCG).getAllThreadChoices();
			
			for (int i = 0; i < threads.length; i++) {
				ThreadInfo ti = threads[i];
				Instruction ins = ti.getPC();
				if (ins instanceof FieldInstruction) {
					FieldInstruction fins = (FieldInstruction)ins;
					ElementInfo ei = fins.peekElementInfo(ti);
				}
			}
		}
	}
	*/
	
	/*
	 * ----------------------------------------------------------------------------------------
	 * 测试代码2
	 */
	
	public static Map<String, Integer> priority = new HashMap<String, Integer>();
	public static ListenerState state;
	public static List<ReadWriteStep> readWriteSteps = new ArrayList<ReadWriteStep>();
	
	public static ElementInfo ei1;
	public static FieldInfo fi1;
	public static ElementInfo ei2;
	public static FieldInfo fi2;
	public static ElementInfo ei3;
	public static FieldInfo fi3;
	public static ElementInfo ei4;
	public static FieldInfo fi4;
	
	public final String PATH = "f:\\xml.txt";
	
	public ListenerStep listenerStep;
	
	public _UseForTest(JPF jpf) {
		super();
		jpf.addPublisherExtension(ConsolePublisher.class, this);
	}
	
	@Override
	public void choiceGeneratorSet (VM vm, ChoiceGenerator<?> newCG) {
		if (state == ListenerState.RECORD) {
			record(vm, newCG);
		}
		else if (state == ListenerState.REAPPEAR) {
			reappear(vm, newCG);
		}
	}
	/*
	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
		if (state == ListenerState.RECORD && executedInstruction instanceof FieldInstruction) {
			sharedVarRecord(currentThread, executedInstruction);
		}
	}
	*/
	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
		ThreadInfo ti = currentThread;
		Instruction ins = ti.getPC();
		if (ins instanceof FieldInstruction) {
			FieldInstruction fins = (FieldInstruction)ins;
			ElementInfo ei = fins.getElementInfo(ti);
			FieldInfo fi = fins.getFieldInfo();
			if (fi.getFullName().contains("number")) {
				if (ins.getFilePos().equals("CheckField.java:13")) {
					System.out.println("13: " + ei.get1SlotField(fi) + " " + ei.hashCode());
					ei1 = ei;
				}
				if (ins.getFilePos().equals("CheckField.java:8")) {
					System.out.println("8: " + ei.get1SlotField(fi) + " " + ei.hashCode() + " " + (ei1 == ei));
				}
			}
		}
	}
	
	//记录共享变量的访问
	private void sharedVarRecord(ThreadInfo ti, Instruction ins) {
		FieldInstruction fins = (FieldInstruction)ins;
		FieldInfo fi = fins.getFieldInfo();
		//ElementInfo ei = fins.peekElementInfo(ti);

		//String fieldName = fins.getFieldName();
		ElementInfo ei = fins.getElementInfo(ti);
		//fins.getElementInfo(ti);
		//FieldInfo fi = fins.getFieldInfo();
		//MethodInfo mi = ins.getMethodInfo();
		
		if (fi.getFullName().contains("number")) {
			if (ins.getFilePos().equals("CheckField.java:12")) {
				fi1 = fi;
				ei1 = ei;
			}
			
			if (ins.getFilePos().equals("CheckField.java:8")) {
				//boolean re = fi == fi1 && ei== ei1;
				System.out.println("不同线程中访问不同变量 element:" + (ei == ei1) + " field:" + (fi == fi1));
			}
			
			
			if (ins.getFilePos().equals("CheckField.java:13")) {
				fi2 = fi;
				ei2 = ei;
			}
			if (ins.getFilePos().equals("CheckField.java:8")) {
				//boolean re = fi == fi2 && ei== ei2;
				int num1 = ei.get1SlotField(fi);
				int num2 = ei2.get1SlotField(fi2);
				System.out.println("不同线程中访问同一变量 element:" + (ei == ei2) + " field:" + (fi == fi2) 
						+ " value1:" + num1 + " value2:" + num2);
			}
			
			if (ins.getFilePos().equals("CheckField.java:8")) {
				fi3 = fi;
				ei3 = ei;
			}
			if (ins.getFilePos().equals("CheckField.java:9")) {
				//boolean re = fi == fi3 && ei== ei3;
				System.out.println("同一线程中访问不同变量 element:" + (ei == ei3) + " field:" + (fi == fi3));
			}
			
			if (ins.getFilePos().equals("CheckField.java:12")) {
				fi4 = fi;
				ei4 = ei;
			}
			
			if (ins.getFilePos().equals("CheckField.java:14")) {
				//boolean re = fi == fi4 && ei== ei4;
				int num1 = ei.get1SlotField(fi);
				int num2 = ei4.get1SlotField(fi4);
				System.out.println("同一线程中访问同一变量element:" + (ei == ei4) + " field:" + (fi == fi4)
						+ " value1:" + num1 + " value2:" + num2);
			}
			
		}
		
		
		String type = ((FieldInstruction) ins).getClass().getName();
		String sig = fi.getName();
		
		//readWriteSteps.add(new ReadWriteStep(ti.getName(), fins.getFieldInfo().getFullName(), type, ins.getFilePos(), fi.getStorageOffset()));
		listenerStep.addReadWriteStep(ti.getName(), fins.getFieldInfo().getFullName(), type, ins.getFilePos(), sig);
	}
	
	private void record(VM vm, ChoiceGenerator<?> newCG) {
		if (newCG.isSchedulingPoint()) {
			
			ThreadChoiceFromSet tcgi = (ThreadChoiceFromSet) newCG;
			
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
			
			
			//newCG.randomize();
			//int index = 0;
			
			//记录序列
			
			ThreadInfo t = tcgi.getChoice(index);
			Instruction ins = t.getPC();
			
			listenerStep.addStep(t.getName(), ins.getFileLocation());
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
	    search.addProperty(this);
		if (state == ListenerState.RECORD) {
			listenerStep = new ListenerStep();
		}
		else if (state == ListenerState.REAPPEAR) {
			listenerStep = XmlTransfer.fromXML(PATH);
		}
	 }
	
	@Override
	public void searchFinished(Search search) {
		System.out.println("Search Finished.");
		listenerStep.readWriteSteps = filter(listenerStep.readWriteSteps);
		System.out.println(listenerStep.toString());
		if (state == ListenerState.RECORD) {
			XmlTransfer.toXML(listenerStep, PATH);
		}
		else if (state == ListenerState.REAPPEAR) {

		}
		
		//List<ReadWriteStep> result = filter(readWriteSteps);
		//showReadWriteList(result);
	}
	
	private List<ReadWriteStep> filter(List<ReadWriteStep> steps) {
		List<ReadWriteStep> result = new ArrayList<ReadWriteStep>();
		for (ReadWriteStep step: steps) {
			/*
			int count = 0;
			for (ReadWriteStep p : steps) {
				if (!p.threadName.equals(step.threadName) && p.objectName.equals(step.objectName)) {
					count++;
				}
			}
			if (step.objectName.equals("InstanceExample.number")){
			//if (count != 0) {
				result.add(step);
			}
			*/
			
			if (step.location.contains("CheckField") || step.location.contains("InstanceExample")) {
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
	
	@Override
	public void publishPropertyViolation (Publisher publisher) {
		System.out.println("publish.-------------------------------------------------------------");
	}
	
	@Override
	public void propertyViolated(Search search) {
		System.out.println("property violated.-------------------------------------------------------------");
	}
}
