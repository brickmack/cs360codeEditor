package cs360ProjectImplementation;

public class InsertableCode implements java.io.Serializable {
	private String name;
	private String codeStart;
	private String codeEnd;
	
	public InsertableCode(String name, String codeStart, String codeEnd) {
		this.name = name;
		this.codeStart = codeStart;
		this.codeEnd = codeEnd;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getCode(String selected) {
		if (selected == null) {
			//nothing selected, just return start and end with a placeholder
			return new String[] {codeStart, "", codeEnd};
		}
		else {
			//convert selected text into an array of lines, and indent each appropriately
			String[] splitSelected = selected.split("\n");
			
			String[] insertLines = new String[splitSelected.length+2];
			insertLines[0] = codeStart;
			insertLines[insertLines.length-1] = codeEnd;
			
			for (int i=1; i<insertLines.length-1; i++) {
				insertLines[i] = "	" + splitSelected[i-1];
			}
			
			return insertLines;
		}
	}
}