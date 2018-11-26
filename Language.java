package cs360ProjectImplementation;

public class Language {
	private String name;
	private HighlightRule[] rules;
	private String fileExtension;
	private String blockDesignator;
	private InsertableCode[] insertable;
	
	public Language(String name, HighlightRule[] rules, String fileExtension) {
		this.name = name;
		this.rules = rules;
		this.fileExtension = fileExtension;
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
		return insertable[i];
	}
	
	public InsertableCode[] getInsertableCode() {
		return insertable;
	}
}