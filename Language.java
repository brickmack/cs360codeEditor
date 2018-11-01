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
		
		read(new File("src\\cs360ProjectImplementation\\javaConfig.txt")); //just for testing
	}
	
	public void read(File fileName) {
		try {
			Scanner scanner = new Scanner(fileName);
			
			String nameInner = scanner.nextLine().split(" ")[1];
			String fileExtensionInner = scanner.nextLine().split(" ")[1]; 
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.split(" ")[0].equals("BLOCKDESIGNATOR")) {
					String blockDesignatorInner = line.replaceAll("BLOCKDESIGNATOR ", "");
				}
				else if (line.split(" ")[0].equals("HIGHLIGHTRULE")) {
					Scanner lineScanner = new Scanner(line);
					lineScanner.next();
					String ruleName = lineScanner.next().replaceAll("name=", "");
					ruleName = ruleName.replaceAll(",", "");
					
					String regexDefinition = lineScanner.next().replaceAll("regexDefinition=", "");
					ArrayList<String> regexes = new ArrayList<String>();
					while (regexDefinition.startsWith("{")) {
						while (! regexDefinition.endsWith("}")) {
							//we only got part of the regex
							regexDefinition = regexDefinition + " " + lineScanner.next();
						}
						regexes.add(regexDefinition);
						regexDefinition = lineScanner.next();
					}
					
					String colorString = regexDefinition.replaceAll("color=", "");
					Color color = new Color(0,0,0);
					if (colorString.matches("\\((\\d)*,(\\d)*,(\\d)\\),")) {
						//RGB notation
						colorString = colorString.replaceAll("\\(|\\)", "");
						String[] colorArray = colorString.split(",");
						color = new Color(Integer.parseInt(colorArray[0]), Integer.parseInt(colorArray[1]), Integer.parseInt(colorArray[2]));
					}
					else if (colorString.matches("#([0-9]|[a-f]){6},")) { //need to actually test that this works
						//hex notation
						
					}
					
					String[] arrayedRegexes = new String[regexes.size()];
					for (int i=0; i<regexes.size(); i++) {
						arrayedRegexes[i] = regexes.get(i);
					}
					
					HighlightRule newGroup = new HighlightRule(ruleName, arrayedRegexes, color, false);
					
					System.out.println("lang definition:");
					System.out.println(nameInner);
					System.out.println(fileExtensionInner);
					System.out.println("\nRule: " + newGroup.getName() + "\n" + newGroup.getColor().toString() + "\n" + newGroup.getDefinition()[0].toString());
					
					lineScanner.close();
				}
				else if (line.split(" ")[0].equals("INSERTABLECODE")) {
					
				}
			}
			
			scanner.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public HighlightRule[] getRules() {
		return rules;
	}
}