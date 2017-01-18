package scheduler.model;

public class ReadWriteNode {
	public String element;
	public String field;
	public String type;
	public String thread;
	public String line;
	
	//在是写操作时有效：是否改变了数值
	public boolean changed;
	
	public ReadWriteNode(String element, String field, String type, String thread, String line, boolean changed) {
		this.element = element;
		this.field = field;
		this.type = type;
		this.thread = thread;
		this.line = line;
		this.changed = changed;
	}
	
	public String toString() {
		return "element:\t" + element + "\tfield:\t" + field + "\ttype:\t" + type + "\tthread:\t" + thread + "\tline:\t" + line;
	}
	
	public boolean equals(ReadWriteNode node) {
		boolean result = true;
		if (this.element == null) {
			if (node.element != null) {
				result = false;
			}
		}
		else {
			if (!this.element.equals(node.element)) {
				result = false;
			}
		}
		if (this.field == null) {
			if (node.field != null) {
				result = false;
			}
		}
		else {
			if (!this.field.equals(node.field)) {
				result = false;
			}
		}
		if (this.type == null) {
			if (node.type != null) {
				result = false;
			}
		}
		else {
			if (!this.type.equals(node.type)) {
				result = false;
			}
		}
		if (this.thread == null) {
			if (node.thread != null) {
				result = false;
			}
		}
		else {
			if (!this.thread.equals(node.thread)) {
				result = false;
			}
		}
		if (this.line == null) {
			if (node.line != null) {
				result = false;
			}
		}
		else {
			if (!this.line.equals(node.line)) {
				result = false;
			}
		}
		if (!(this.changed == node.changed)) {
			result = false;
		}
		
		return result;
	}
}
