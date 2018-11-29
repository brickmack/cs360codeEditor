package cs360ProjectImplementation;

public class Language {
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
}