package cs360Project2;

import java.util.ArrayList;

public class Node<T> {
	private T data;
	private ArrayList<Node<T>> branches;
	
	public Node() {
	}
	
	public Node(T data) {
		this.data = data;
	}
	
	public void addBranch(Node<T> node) {
		branches.add(node);
	}
	
	public void removeBranch(int i) {
		branches.remove(i);
	}
	
	public Node<T> getBranch(int i) {
		return branches.get(i);
	}
	
	public T getElement() {
		return data;
	}
	
	public void setElement(T data) {
		this.data = data;
	}
}