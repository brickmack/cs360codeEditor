/*
 * Language
 * 
 * Collection of information describing a programming language
 * 
 * Contains a name, an array of HighlightRules, a default file extension for source code files, and an array of InsertableCodes
 * 
 * Language objects should be constructed and serialized externally using the LanguageBuilder, and the resulting .lng files should be
 * placed in the languages directory so they can be accessed by the program
 */

package cs360ProjectImplementation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Language implements java.io.Serializable {
	private String name;
	private HighlightRule[] rules;
	private String fileExtension;
	private String blockDesignator;
	private InsertableCode[] insertables;
	
	public Language(String name, HighlightRule[] rules, String fileExtension, InsertableCode[] insertables) {
		this.name = name;
		this.rules = rules;
		this.fileExtension = fileExtension;
		this.insertables = insertables;
	}
	
	public String getName() {
		return name;
	}
	
	public HighlightRule[] getRules() {
		return rules;
	}
	
	public String getFileExtension() {
		return fileExtension;
	}
	
	public String getBlockDesignator() {
		return blockDesignator;
	}
	
	public InsertableCode getInsertableCode(int i) {
		return insertables[i];
	}
	
	public InsertableCode[] getInsertableCode() {
		return insertables;
	}
	
	public static Language deserializeLanguage(String fileName) {
		//deserializes a language from a .lng configuration file (built using LanguageBuilder)
		Language lang = null;
		try {
			//read object from file
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file);
			
			//method for deserialization of an object
			lang = (Language) in.readObject();
			
			in.close();
			file.close();
			
			return lang;
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
			return null;
		}
		catch (ClassNotFoundException ex) {
			System.out.println(ex.toString());
			return null;
		}
	}
}