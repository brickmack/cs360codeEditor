package cs360ProjectImplementation;

public class InsertableCode {
	/*
	private String name;
	private String code;
	
	public InsertableCode(String name, String code) {
		this.name = name;
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	*/
	
	// ... wow https://i.imgur.com/vl2hxYW.png
	
	public InsertableCode() {
		
	}
	
	public String getWhile() {
 		return "while()\n{\n}";
	}
	
	public String getFor() {
 		return "for( , , ){\n}";
	}
	
	public String getIfElse() {
 		return "if(){\n}\nelse{\n}";
	}
	
 	public String getIfElseIfElse() {
 		return "if(){\n}\nelse if(){\n}\nelse{\n}";
 	}
 	
 	public String getDoWhile() {
 		return "do {\n}while();";
 	}
}