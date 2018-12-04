package cs360ProjectImplementation;

import java.util.ArrayList;

public class CodeBlock {
	private int startIndex;
	private int endIndex;
	private ArrayList<CodeBlock> children;
	private boolean folded = false;
	
	public CodeBlock(int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public int getStart() {
		return startIndex;
	}
	
	public void setStart(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public int getEnd() {
		return endIndex;
	}
	
	public void setEnd(int endIndex) {
		this.endIndex = endIndex;
	}
	
	public void addChild(CodeBlock child) {
		children.add(child);
	}
	
	public CodeBlock getChild(int i) {
		return children.get(i);
	}
	
	public void removeChild(int i) {
		children.remove(i);
	}
	
	public void toggleFold() {
		folded = !folded;
	}
	
	public boolean isFolded() {
		return folded;
	}
}