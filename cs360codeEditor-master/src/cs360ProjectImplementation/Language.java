package cs360ProjectImplementation;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Language {
	private String name;
	private HighlightRule[] rules;
	private String fileExtension;
	private String blockDesignator;
	private InsertableCode[] insertable;
	
	public Language(String name, HighlightRule[] rules) {
		this.name = name;
		this.rules = rules;
	}
	
	public String getName() {
		return name;
	}
	
	public HighlightRule[] getRules() {
		return rules;
	}
}