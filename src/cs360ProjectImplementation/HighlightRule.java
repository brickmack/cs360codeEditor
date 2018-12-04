package cs360ProjectImplementation;

import java.awt.Color;

public class HighlightRule implements java.io.Serializable {
	private String name;
	private String[] definition;
	private Color color;
	private boolean multiLine;
	
	public HighlightRule(String name, String[] definition, Color color, boolean multiLine) {
		this.name = name;
		this.definition = definition;
		this.color = color;
		this.multiLine = multiLine;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getDefinition() {
		return definition;
	}
	
	public Color getColor() {
		return color;
	}
	
	public boolean isMultiLine() {
		return multiLine;
	}
}