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

public class Tab {
	private String name;
	private UserFile userFile;
	private JScrollPane scroller;
	private JPanel panel;
	private JTextPane textPane;
	private int language = 0;
	private boolean enabled = false;
	private File diskLocation;
	private ArrayList<String> variables;
	private Color darkGreen = new Color(18, 119, 2);
	private Color purple = new Color(90, 2, 119);
	
	public Tab(String name) {
		userFile = new UserFile();
		this.name = name;
		panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		textPane = new JTextPane();
		
		userFile.setText("");
		
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				//System.out.println("changed");
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
		panel.add(scroller);
		
		TextLineNumber tln = new TextLineNumber(textPane);
		scroller.setRowHeaderView(tln);
	}
	
	public String getName() {
		return name;
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public JTextPane getTextPane() {
		return textPane;
	}
	
	private void highlight() {
		Runnable doHighlight = new Runnable() {
			@Override
			public void run() {
				//language == 0, plaintext. do nothing.
				if (language == 1) {
					String javaKeywords[] = {"abstract", "assert", "boolean",
			                "break", "byte", "case", "catch", "char", "class", "const",
			                "continue", "default", "do", "double", "else", "extends", "false",
			                "final", "finally", "float", "for", "goto", "if", "implements",
			                "import", "instanceof", "int", "interface", "long", "native",
			                "new", "null", "package", "private", "protected", "public",
			                "return", "short", "static", "strictfp", "super", "switch",
			                "synchronized", "this", "throw", "throws", "transient", "true",
			                "try", "void", "volatile", "while"};
					HighlightRule keywords = new HighlightRule("keywords", javaKeywords, purple, false);
					HighlightRule stringDef = new HighlightRule("string", new String[] {"(\"([^\"]*)\")"}, Color.blue, false);
					HighlightRule comment = new HighlightRule("comment", new String[] {"((?m)//(.*)$)|((?s)/\\*.*\\*/)"}, darkGreen, true);
					
					Language java = new Language("Java", new HighlightRule[] {keywords, comment, stringDef});
					
					String text = textPane.getText();
					StyledDocument doc = textPane.getStyledDocument();
					
					SimpleAttributeSet defSet = new SimpleAttributeSet();
					StyleConstants.setForeground(defSet, Color.BLACK);
		            doc.setCharacterAttributes(0, text.length(), defSet, true);
		            SimpleAttributeSet set = new SimpleAttributeSet();
		            
		            for (int i=0; i<java.getRules().length; i++) {
		            	StyleConstants.setForeground(set, java.getRules()[i].getColor());
		            	for (int j=0; j<java.getRules()[i].getDefinition().length; j++) {
			            	Pattern word = Pattern.compile(java.getRules()[i].getDefinition()[j]);
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
									if (java.getRules()[i].isMultiLine() == true) {
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
				else {
					//unsupported language. Need error handling here
				}
			}
		};
	    SwingUtilities.invokeLater(doHighlight);
	}
	
	public void enableTriggers() {
		enabled = true;
	}
	
	public void setLang(int language) {
		this.language = language;
		if (language == 0) {
			//special case for plaintext, run highlight only once to clear formatting
			StyledDocument doc = textPane.getStyledDocument();
			
			SimpleAttributeSet defSet = new SimpleAttributeSet();
			StyleConstants.setForeground(defSet, Color.BLACK);
            doc.setCharacterAttributes(0, textPane.getText().length(), defSet, true);
		}
	}
	
	public int getLang() {
		return language;
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
}