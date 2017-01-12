package scheduler.listener;

import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.bytecode.FieldInstruction;
import gov.nasa.jpf.vm.choice.ThreadChoiceFromSet;

public class _UseForTest extends PropertyListenerAdapter{
	
	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
		ThreadInfo ti = currentThread;
		Instruction ins = ti.getPC();
		if (ins instanceof FieldInstruction) {
			FieldInstruction fins = (FieldInstruction)ins;
			ElementInfo ei = fins.getElementInfo(ti);
		}
	}
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
}
