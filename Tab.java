package cs360ProjectImplementation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Tab extends JPanel {
	private String name;
	private UserFile userFile;
	private JScrollPane scroller;
	private JTextPaneCollapsible textPane; //replace with private JTextPane textPane to disable the test collapse feature
	private int languageIndex = 0;
	private Language[] languages;
	private Language language = new Language("Plaintext", null, ".txt");
	private boolean enabled = false;
	private File diskLocation;
	private ArrayList<String> variables;
	
	public Tab(String name, Language[] languages) {
		super();
		userFile = new UserFile();
		this.name = name;
		this.languages = languages;
		setLayout(new BorderLayout(0, 0));
		textPane = new JTextPaneCollapsible();
	    
		userFile.setText("");
		
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (enabled == true) {
					highlight();
					UserFile next = new UserFile();
					next.setText(textPane.getText());
					userFile.setNext(next);
					next.setPrev(userFile);
					userFile = next;
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (enabled == true) {
					highlight();
				}
			}
		});
		scroller = new JScrollPane(textPane);
		add(scroller);
		
		TextLineNumber tln = new TextLineNumber(textPane);
		scroller.setRowHeaderView(tln);
	}
	
	public String getName() {
		return name;
	}
	
	public JTextPane getTextPane() {
		return textPane;
	}
	
	private void highlight() {
		Runnable doHighlight = new Runnable() {
			@Override
			public void run() {
				String text = textPane.getText();
				StyledDocument doc = textPane.getStyledDocument();
				
				SimpleAttributeSet defSet = new SimpleAttributeSet();
				StyleConstants.setForeground(defSet, Color.BLACK);
	            doc.setCharacterAttributes(0, text.length(), defSet, true);
	            SimpleAttributeSet set = new SimpleAttributeSet();
	            
				if (language.getRules() != null) {
		            for (int i=0; i<language.getRules().length; i++) {
		            	StyleConstants.setForeground(set, language.getRules()[i].getColor());
		            	for (int j=0; j<language.getRules()[i].getDefinition().length; j++) {
			            	Pattern word = Pattern.compile(language.getRules()[i].getDefinition()[j]);
				            Matcher match = word.matcher(text);
				            
				            while (match.find()) {
								try {
									//offset for newlines. will find a more efficient implementation later, because this will very quickly become time consuming
									int count = 0;
									for (int k=0; k<match.start(); k++) {
										if (text.charAt(k) == '\n') {
											count++;
										}
									}
									
									//also count newlines within the match if applicable, since block comments and some other things can cover multiple lines
									int inMatchCount = 0;
									if (language.getRules()[i].isMultiLine() == true) {
										for (int l=match.start(); l<match.end(); l++) {
											if (text.charAt(l) == '\n') {
												inMatchCount++;
											}
										}
									}
									doc.setCharacterAttributes(match.start() - count, (match.end() - match.start()) - inMatchCount, set, true);
								}
								catch (Exception e) {
									System.out.println(e);
								}
							}
			            }
		            }
				}
			}
		};
	    SwingUtilities.invokeLater(doHighlight);
	}
	
	public void enableTriggers() {
		enabled = true;
	}
	
	public void disableTriggers() {
		enabled = false;
	}
	
	public void setLangIndex(int languageIndex) {
		this.languageIndex = languageIndex;
		language = languages[languageIndex];
		highlight();
	}
	
	public int getLangIndex() {
		return languageIndex;
	}
	
	public void undo() {
		if (userFile.hasPrev()) {
			userFile = userFile.getPrev();
			textPane.setText(userFile.getText());
			if (userFile.hasPrev()) {
				userFile = userFile.getPrev();
			}
			System.out.println("success undo");
		}
		else {
			System.out.println("failed undo: " + userFile.getText());
		}
	}
	
	public void redo() {
		if (userFile.hasNext()) {
			userFile = userFile.getNext();
			String newText = userFile.getText();
			textPane.setText(newText);
			System.out.println("success redo: " + userFile.getText());
		}
		else {
			System.out.println("failed redo: " + userFile.getText());
		}
	}
	
	public void setLocation(File diskLocation) {
		this.diskLocation = diskLocation;
		
		//set name by cutting off everything up to the last path separator
		String name = diskLocation.toString();
		int index = name.lastIndexOf('\\');
		name = name.substring(index);
		System.out.println(name);
	}
	
	public File getDiskLocation() {
		return diskLocation;
	}
}