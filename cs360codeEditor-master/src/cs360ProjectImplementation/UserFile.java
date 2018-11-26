package cs360ProjectImplementation;

public class UserFile {
	private String text;
	private UserFile prevVersion;
	private UserFile nextVersion;
	private CodeBlock blocks;
	
	public UserFile() {
	}
	
	public UserFile(String text, UserFile prevVersion, UserFile nextVersion) {
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
	
	public UserFile getPrev() {
		return prevVersion;
	}
	
	public void setPrev(UserFile prevVersion) {
		this.prevVersion = prevVersion;
	}
	
	public UserFile getNext() {
		return nextVersion;
	}
	
	public void setNext(UserFile nextVersion) {
		this.nextVersion = nextVersion;
	}
}