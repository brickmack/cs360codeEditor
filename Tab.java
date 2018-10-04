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
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Tab {
	private String name;
	private JScrollPane scroller;
	private JPanel panel;
	private JTextPane textPane;
	private int language = 0;
	private boolean enabled = false;
	
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
					
					Pattern word = Pattern.compile("bee[\\.\\s,]"); //"bee", followed by either a space, period, or comma to ensure its not part of another word (beer, been, beet)
					Matcher match = word.matcher(text);
					StyledDocument doc = textPane.getStyledDocument();
					
		            SimpleAttributeSet defSet = new SimpleAttributeSet();
		            StyleConstants.setForeground(defSet, Color.BLACK);
		            doc.setCharacterAttributes(0, text.length(), defSet, true);
		            SimpleAttributeSet set = new SimpleAttributeSet();
		            StyleConstants.setForeground(set, Color.YELLOW);
					
					while (match.find()) {
						try {
		                    doc.setCharacterAttributes(match.start(), 3, set, true);
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
}