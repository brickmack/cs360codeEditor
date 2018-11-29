package cs360ProjectImplementation;

public class InsertableCode {
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
	
	public String getCode() {
		return codeStart + " " + codeEnd;
	}
	
	public String getCode(String selected) {
		return codeStart + selected + codeEnd;
	}
}