package cs360Project2;

import java.awt.BorderLayout;
import java.awt.Color;
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
	private TextFile text;
	private JScrollPane scroller;
	private JPanel panel;
	private JTextPane textPane;
	private int language = 0;
	private boolean enabled = false;
	private Color darkGreen = new Color(18, 119, 2);
	private Color purple = new Color(90, 2, 119);
	
	public Tab(String name) {
		text = new TextFile();
		this.name = name;
		panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		textPane = new JTextPane();
		
		text.setText("");
		
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				//System.out.println("changed");
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (enabled == true) {
					highlight();
					TextFile next = new TextFile();
					next.setText(textPane.getText());
					text.setNext(next);
					next.setPrev(text);
					text = next;
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
					String javaKeywords[] = {"public", "int"};
					WordGroup keywords = new WordGroup("keywords", javaKeywords, purple, false);
					WordGroup stringDef = new WordGroup("string", new String[] {"(\"([^\"]*)\")"}, Color.blue, false);
					WordGroup comment = new WordGroup("comment", new String[] {"((?m)//(.*)$)|((?s)/\\*.*\\*/)"}, darkGreen, true);
					
					Language java = new Language("Java", new WordGroup[] {keywords, comment, stringDef});
					
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
					//System.out.println("unsupported language");
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
	}
	
	public int getLang() {
		return language;
	}
	
	public void undo() {
		if (text.hasPrev()) {
			text = text.getPrev();
			textPane.setText(text.getText());
			if (text.hasPrev()) {
				text = text.getPrev();
			}
			System.out.println("success undo");
		}
		else {
			System.out.println("failed undo: " + text.getText());
		}
	}
	
	public void redo() {
		if (text.hasNext()) {
			text = text.getNext();
			String newText = text.getText();
			textPane.setText(newText);
			System.out.println("success redo: " + text.getText());
		}
		else {
			System.out.println("failed redo: " + text.getText());
		}
	}
}