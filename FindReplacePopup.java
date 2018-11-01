package cs360ProjectImplementation;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FindReplacePopup extends JFrame {
	private JButton findNext = new JButton("Find next"); 
	private JButton replaceAll = new JButton("Replace all");
	private JTextField findField = new JTextField(25);
	private JTextField replaceField = new JTextField(25);
	
	public FindReplacePopup() {
		setSize(400, 200);
		setTitle("Find/Replace");
		setVisible(true);
		
		JPanel panel = new JPanel();
		add(panel);
		
		panel.add(new JLabel("Find: "));
		panel.add(findField);
		
		panel.add(new JLabel("Replace with: "));
		panel.add(replaceField);
		
		panel.add(findNext);
		
		panel.add(replaceAll);
	}
	
	public JButton getFindNextButton() {
		return findNext;
	}
	
	public String getFindQuery() {
		return findField.getText();
	}
	
	public String getReplaceTerm() {
		return replaceField.getText();
	}
}