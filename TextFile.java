package cs360Project2;

public class TextFile {
	private String text;
	private TextFile prevVersion;
	private TextFile nextVersion;
	private Node<CodeBlock> blocks;
	
	public TextFile() {
	}
	
	public TextFile(String text, TextFile prevVersion, TextFile nextVersion) {
		this.text = text;
		this.prevVersion = prevVersion;
		this.nextVersion = nextVersion;
	}
	
	public boolean hasPrev() {
		if (prevVersion == null) {
			return false;
		}
		return true;
	}
	
	public boolean hasNext() {
		if (nextVersion == null) {
			return false;
		}
		return true;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public TextFile getPrev() {
		return prevVersion;
	}
	
	public void setPrev(TextFile prevVersion) {
		this.prevVersion = prevVersion;
	}
	
	public TextFile getNext() {
		return nextVersion;
	}
	
	public void setNext(TextFile nextVersion) {
		this.nextVersion = nextVersion;
	}
}