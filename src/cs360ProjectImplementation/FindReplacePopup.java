/*
 * FindReplacePopup
 * 
 * JFrame window used to control find and replace operations
 */

package cs360ProjectImplementation;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

public class FindReplacePopup extends JFrame {
	private JTextField findField = new JTextField(25);
	private JTextField replaceField = new JTextField(25);
	private JPanel fieldsPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel findReplacePanel;
	private JLabel findLabel;
	private JLabel replaceLabel;
	private final JButton findNext = new JButton("Find Next");
	private final JButton skipbtn = new JButton("Skip");
	private final JButton replaceNext = new JButton("Replace next");
	private final JButton replaceAll = new JButton("Replace all");

	public FindReplacePopup() {
		setSize(400, 200);
		setTitle("Find/Replace");
		setVisible(true);

		findReplacePanel = new JPanel();
		getContentPane().add(findReplacePanel);
		findReplacePanel.setLayout(new BorderLayout(0, 0));

		findReplacePanel.add(fieldsPanel, BorderLayout.CENTER);
		fieldsPanel.setLayout(new GridLayout(2, 2, 0, 0));

		findLabel = new JLabel("Find: ");
		findLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fieldsPanel.add(findLabel);
		findField.setHorizontalAlignment(SwingConstants.CENTER);
		fieldsPanel.add(findField);

		replaceLabel = new JLabel("Replace with: ");
		replaceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fieldsPanel.add(replaceLabel);
		replaceField.setHorizontalAlignment(SwingConstants.CENTER);
		fieldsPanel.add(replaceField);

		findReplacePanel.add(buttonPanel, BorderLayout.SOUTH);
		
		buttonPanel.add(findNext);
		buttonPanel.add(skipbtn);
		
		buttonPanel.add(replaceNext);
		buttonPanel.add(replaceAll);
	}

	public JButton getreplaceNextButton() {
		return replaceNext;
	}
	
	public JButton getReplaceAllButton() {
		return replaceAll;
	}

	public String getFindQuery() {
		return findField.getText();
	}

	public String getReplaceTerm() {
		return replaceField.getText();
	}	
}