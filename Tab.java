package cs360Project2;

import java.awt.BorderLayout;
import java.awt.Color;
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
	private JScrollPane scroller;
	private JPanel panel;
	private JTextPane textPane;
	private int language = 0;
	private boolean enabled = false;
	private Color darkGreen = new Color(18, 119, 2);
	private Color purple = new Color(90, 2, 119);
	
	public Tab(String name) {
		this.name = name;
		panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		textPane = new JTextPane();
		
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				//System.out.println("changed");
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (enabled == true) {
					//System.out.println("insert");
					highlight();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				//System.out.println("remove");
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
	
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	public void setTextPane(JTextPane textPane) {
		this.textPane = textPane;
		scroller = new JScrollPane(this.textPane);
	}
	
	private void highlight() {
		Runnable doHighlight = new Runnable() {
			@Override
			public void run() {
				if (language == 0) { //plaintext. In the finished version, we wont have text highlighting for this anyway, so dont need a case for it. just testing
					String text = textPane.getText();
					
					String[] keyPurple = new String[] {"bee"};
					
					StyledDocument doc = textPane.getStyledDocument();
					
		            SimpleAttributeSet defSet = new SimpleAttributeSet();
		            StyleConstants.setForeground(defSet, Color.BLACK); //we need this line so it resets any uncolored text to black.
		            //That way if a word was highlighted but is edited to no longer be a keyword ("while" -> "whale") it automatically un-highlights
		            doc.setCharacterAttributes(0, text.length(), defSet, true);
		            SimpleAttributeSet set = new SimpleAttributeSet();
					
					for (String s : keyPurple) {
						Pattern word = Pattern.compile(s + "[\\.\\s,\n]"); //keyword, followed by either a space, period, or comma to ensure its not part of another word (beer, been, beet)
						
						Matcher match = word.matcher(text);
						
			            StyleConstants.setForeground(set, Color.yellow);
						
						while (match.find()) {
							try {
								//offset for newlines. will find a more efficient implementation later, because this will very quickly become time consuming
								int count = 0;
								for (int i=0; i<match.start(); i++) {
									if (text.charAt(i) == '\n') {
										count++;
									}
								}
			                    doc.setCharacterAttributes(match.start() - count, s.length(), set, true);
							}
							catch (Exception e) {
								System.out.println(e);
							}
						}
					}
				}
				else if (language == 1) {
					String text = textPane.getText();
					
					String[] keyPurple = new String[] {"public", "class", "static", "void", "for", "int"};
					
					StyledDocument doc = textPane.getStyledDocument();
					
		            SimpleAttributeSet defSet = new SimpleAttributeSet();
		            StyleConstants.setForeground(defSet, Color.BLACK); //we need this line so it resets any uncolored text to black.
		            //That way if a word was highlighted but is edited to no longer be a keyword ("while" -> "whale") it automatically un-highlights
		            doc.setCharacterAttributes(0, text.length(), defSet, true);
		            SimpleAttributeSet set = new SimpleAttributeSet();
					
					for (String s : keyPurple) {
						Pattern word = Pattern.compile(s + "[\\.\\s,\n]"); //keyword, followed by either a space, period, or comma to ensure its not part of another word (beer, been, beet)
						
						Matcher match = word.matcher(text);
						
			            StyleConstants.setForeground(set, purple);
						
						while (match.find()) {
							try {
								//offset for newlines. will find a more efficient implementation later, because this will very quickly become time consuming
								int count = 0;
								for (int i=0; i<match.start(); i++) {
									if (text.charAt(i) == '\n') {
										count++;
									}
								}
			                    doc.setCharacterAttributes(match.start() - count, s.length(), set, true);
							}
							catch (Exception e) {
								System.out.println(e);
							}
						}
					}
					
					Pattern commentPattern = Pattern.compile("((?m)//(.*)$)|((?s)/\\*.*\\*/)"); //finds 2 slashes followed by any content followed by end of line OR a multi-line block with /* CONTENT */
					
					Matcher match = commentPattern.matcher(text);
					
					StyleConstants.setForeground(set, darkGreen);
					
					while (match.find()) {
						try {
							//offset for newlines. will find a more efficient implementation later, because this will very quickly become time consuming
							int count = 0;
							for (int i=0; i<match.start(); i++) {
								if (text.charAt(i) == '\n') {
									count++;
								}
							}
							
							//also count newlines within the match, since block comments can cover multiple lines
							int inMatchCount = 0;
							for (int i=match.start(); i<match.end(); i++) {
								if (text.charAt(i) == '\n') {
									inMatchCount++;
								}
							}
							
		                    doc.setCharacterAttributes(match.start() - count, (match.end() - match.start()) - inMatchCount, set, true);
						}
						catch (Exception e) {
							System.out.println(e);
						}
					}
					
					Pattern stringPattern = Pattern.compile("\".*\"");
					
					match = stringPattern.matcher(text);
					
					StyleConstants.setForeground(set, Color.BLUE);
					
					while (match.find()) {
						try {
							//offset for newlines. will find a more efficient implementation later, because this will very quickly become time consuming
							int count = 0;
							for (int i=0; i<match.start(); i++) {
								if (text.charAt(i) == '\n') {
									count++;
								}
							}
							
							//also count newlines within the match, since block comments can cover multiple lines
							int inMatchCount = 0;
							for (int i=match.start(); i<match.end(); i++) {
								if (text.charAt(i) == '\n') {
									inMatchCount++;
								}
							}
							
		                    doc.setCharacterAttributes(match.start() - count, (match.end() - match.start()) - inMatchCount, set, true);
						}
						catch (Exception e) {
							System.out.println(e);
						}
					}
				}
				
				else {
					System.out.println("unsupported language");
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
}