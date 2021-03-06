/*
 * Tab
 * 
 * contains a JTextPane in which the user can view and edit a file. Also contains information about the file open in that tab, including
 * its name, location on the disk, language, saved/unsaved status, and past file states (using a FileState object)
 *  
 * Syntax highlighting is handled here
 */

package cs360ProjectImplementation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Tab extends JPanel {
	private String name;
	private FileState fileStates;
	private JScrollPane scroller;
	//private JTextPaneCollapsible textPane;
	private JTextPane textPane;
	private int languageIndex = 0;
	private Language[] languages;
	private Language language = new Language("Plaintext", null, ".txt", null);
	private boolean enabled = false;
	private File diskLocation;
	private EditorWindow parentFrame; //not very pretty, but we need a reference back to the parent
	private boolean isSaved = true; //false if any changes have been made since the last save
	
	public Tab(String name, Language[] languages, EditorWindow parentFrame) {
		super();
		
		fileStates = new FileState();
		this.name = name;
		this.languages = languages;
		this.parentFrame = parentFrame;
		
		setLayout(new BorderLayout(0, 0));
		//textPane = new JTextPaneCollapsible();
		textPane = new JTextPane();
		((AbstractDocument) textPane.getDocument()).setDocumentFilter(new Filter()); //set a Filter object which ensures correct tab alignment
	    textPane.setFont(new Font("Arial", Font.PLAIN, 15));
		
		fileStates.setText("");
		
		textPane.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (enabled == true) {
					highlight();
					
					FileState next = new FileState();
					next.setText(textPane.getText());
					
					fileStates.setNext(next);
					next.setPrev(fileStates);
					fileStates = next;
					
					isSaved = false;
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (enabled == true) {
					highlight();
					
					isSaved = false;
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
		//syntax highlighting
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
	
	public void setLanguageByIndex(int languageIndex) {
		this.languageIndex = languageIndex;
		language = languages[languageIndex];
		highlight();
	}
	
	public void setLanguageByExtension(File file) {
		//set language from file extension
		
		if (file != null) {
			String name = file.getName();
			
			String extension = "";
			if (name.lastIndexOf('.') > 0) {
				extension = name.substring(name.lastIndexOf('.') + 1);
			}
			
			for (int i=0; i<languages.length; i++) {
				if (languages[i].getFileExtension().equals(extension)) {
					setLanguageByIndex(i);
				}
			}
		}
	}
	
	public int getLangIndex() {
		return languageIndex;
	}
	
	public void undo() {
		enabled = false;
		if (fileStates.hasPrev()) {
			fileStates = fileStates.getPrev();
			textPane.setText(fileStates.getText());
		}
		enabled = true;
	}
	
	public void redo() {
		enabled = false;
		if (fileStates.hasNext()) {
			fileStates = fileStates.getNext();
			textPane.setText(fileStates.getText());
		}
		enabled = true;
	}
	
	public void setDiskLocation(File diskLocation) {
		//sets the location the file is saved at, and the name that should be displayed in its tab
		this.diskLocation = diskLocation;
		
		//set name by cutting off everything up to the last path separator
		String name = diskLocation.toString();
		int index = name.lastIndexOf('\\');
		name = name.substring(index);
	}
	
	public File getDiskLocation() {
		return diskLocation;
	}
	
	public void setSaved() {
		//tells the Tab that EditorWindow has saved it, so closing is now safe
		isSaved = true;
		
		//also clear previous undo/redo states (fixes bug with loading an existing file, also keeps the linked list from growing too large. May find a better way later)
		fileStates.setPrev(null);
		fileStates.setNext(null);
	}
	
	public boolean canClose() {
		//check if we are allowed to close the tab or not. Can't close without explicit user permission if there are unsaved changes in the Tab
		if (isSaved == true) {
			return true;
		}
		else {
			int dialogResult = JOptionPane.showConfirmDialog(this, "This file has unsaved changes. Do you want to save it?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
			if (dialogResult == JOptionPane.YES_OPTION) { //user selected save
				int saveResult = parentFrame.saveFile();
				if (saveResult == 1) { //successfully saved, close
					return true;
				}
				else { //either the file did not successfully save, or the user canceled out of the save as dialog
					return false;
				}
			}
			else if (dialogResult == JOptionPane.NO_OPTION) { //user selected no, we can close
				return true;
			}
			else { //user selected cancel, don't close
				return false;
			}
		}
	}
}

class Filter extends DocumentFilter {
	//filter which ensures tab alignment. New lines auto-indent to the same position as the previous line
	
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        StringBuilder indentedString = new StringBuilder(string);
        if (string.equals("\n")) {
            AbstractDocument doc = ((AbstractDocument) fb.getDocument());
            Element line = doc.getParagraphElement(offset);
            int lineStart = line.getStartOffset(), lineEnd = line.getEndOffset();
            String content = doc.getText(lineStart, lineEnd - lineStart);
            int start = 0;
            while (content.charAt(start) == ' ' || content.charAt(start) == '	') { //works with either tabs or spaces
                indentedString.insert(1, content.charAt(start));
                start++;
            }
        }
        fb.insertString(offset, indentedString.toString(), attr);
    }
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text.length() == 0) {
        	insertString(fb, offset, text, attrs);
        }
        else if (text.length() > 0) {
        	remove(fb, offset, length);
        	insertString(fb, offset, text, attrs);
        }
        else {
        	fb.replace(offset, length, text, attrs);
        }
    }
}