package cs360Project2;

public class CodeBlock {
	private int startIndex;
	private int endIndex;
	
	public CodeBlock(int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public int getStart() {
		return startIndex;
	}
	
	public int getEnd() {
		return endIndex;
	}
}