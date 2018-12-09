/*
 * FileState
 * 
 * Contains a String representation of the entire open file, and links to two other FileState objects (previous and next)
 * 
 * Used to undo and redo changes.
 */

package cs360ProjectImplementation;

public class FileState {
	private String text;
	private FileState prevVersion;
	private FileState nextVersion;
	
	public FileState() {
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
	
	public FileState getPrev() {
		return prevVersion;
	}
	
	public void setPrev(FileState prevVersion) {
		this.prevVersion = prevVersion;
	}
	
	public FileState getNext() {
		return nextVersion;
	}
	
	public void setNext(FileState nextVersion) {
		this.nextVersion = nextVersion;
	}
}