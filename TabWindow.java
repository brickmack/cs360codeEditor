package cs360ProjectImplementation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TabWindow extends JFrame {
	private JTabbedPaneCloseable tabbedPane = new JTabbedPaneCloseable();
	private ArrayList<Tab> tabs = new ArrayList<Tab>();
	private JMenuBar menuBar = new JMenuBar();
	
	private JRadioButtonMenuItem languageMenuPlaintext = new JRadioButtonMenuItem("Plaintext");
	private JRadioButtonMenuItem languageMenuJava = new JRadioButtonMenuItem("Java");
	private JRadioButtonMenuItem languageMenuC = new JRadioButtonMenuItem("C");
	
	private boolean setup = true; //Dont want to enable triggers for Highlight and stuff while everything is still loading, or it breaks stuff
	
	public static void main(String[] args) {
		new TabWindow();
	}
	
	public TabWindow() {
		setSize(900, 700);
		setTitle("Code Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		menuSetup();
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		//blank tab
		createNewTab("New tab");
		
		System.out.println(tabs.get(0).getTextPane().toString()); //this line needs to be here or it doesn't work. I have no idea what the actual fuck is happening here
		
		tabs.get(0).enableTriggers();
		
		setup = false;
	}
	
	public void menuSetup() {
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
		fileMenuSetup();
		editMenuSetup();
		languageMenuSetup();
		insertMenuSetup();
	}
	
	public void insertMenuSetup() {
		JMenu insertMenu = new JMenu("Insert");
		menuBar.add(insertMenu);
	}
	
	public void fileMenuSetup() {
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem fileMenuNew = new JMenuItem("New");
		fileMenu.add(fileMenuNew);
		fileMenuNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				createNewTab("New tab");
			}
		});
		
		JMenuItem fileMenuOpen = new JMenuItem("Open");
		fileMenuOpen = new JMenuItem("Open");
		fileMenuOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				int newIndex = tabs.size();
				
				createNewTab("javaFile.java");
				tabs.get(newIndex).setLang(1); //set to Java language
				openFile("javaFile.java", tabs.get(newIndex));
				tabs.get(newIndex).enableTriggers();
			}
		});
		fileMenu.add(fileMenuOpen);
		
		JMenuItem fileMenuSave = new JMenuItem("Save");
		fileMenu.add(fileMenuSave);
		
		JMenuItem fileMenuSaveAs = new JMenuItem("Save As");
		fileMenu.add(fileMenuSaveAs);
		
		JMenuItem fileMenuClose = new JMenuItem("Close");
		fileMenu.add(fileMenuClose);
		fileMenuClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
	public void editMenuSetup() {
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		JMenuItem editMenuUndo = new JMenuItem("Undo");
		editMenuUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("undo in tab " + tabbedPane.getSelectedIndex());
				tabs.get(tabbedPane.getSelectedIndex()).undo();
			}
		});
		editMenu.add(editMenuUndo);
		
		JMenuItem editMenuRedo = new JMenuItem("Redo");
		editMenuRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("redo in tab " + tabbedPane.getSelectedIndex());
				tabs.get(tabbedPane.getSelectedIndex()).redo();
			}
		});
		editMenu.add(editMenuRedo);
		
		JMenuItem editMenuFindReplace = new JMenuItem("Find and replace");
		editMenuFindReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FindReplacePopup findReplace = new FindReplacePopup();
				findReplace.getFindNextButton().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.out.println("Find " + findReplace.getFindQuery() + " in tab " + tabbedPane.getSelectedIndex());
					}
				});
			}
		});
		editMenu.add(editMenuFindReplace);
	}
	
	public void languageMenuSetup() {
		JMenu languageMenu = new JMenu("Languages");
		languageMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuCanceled(MenuEvent e) {
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuSelected(MenuEvent e) {
				//we need to check which language the current tab is set to and set the radiobutton appropriately
				int currentTabLang = tabs.get(tabbedPane.getSelectedIndex()).getLang();
				if (currentTabLang == 0) {
					languageMenuPlaintext.setSelected(true);
				}
				else if (currentTabLang == 1) {
					languageMenuJava.setSelected(true);
				}
				else {
					languageMenuC.setSelected(true);
				}
			}
		});
		menuBar.add(languageMenu);
		
		ButtonGroup languageGroup = new ButtonGroup();
		
		languageMenuPlaintext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				//we need to set the language in the current tab
				tabs.get(tabbedPane.getSelectedIndex()).setLang(0);
				tabs.get(tabbedPane.getSelectedIndex()).enableTriggers();
			}
		});
		languageGroup.add(languageMenuPlaintext);
		languageMenu.add(languageMenuPlaintext);
		languageMenuPlaintext.setSelected(true); //default
		
		languageMenuJava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				//we need to set the language in the current tab
				tabs.get(tabbedPane.getSelectedIndex()).setLang(1);
				tabs.get(tabbedPane.getSelectedIndex()).enableTriggers();
			}
		});
		languageGroup.add(languageMenuJava);
		languageMenu.add(languageMenuJava);
		
		languageMenuC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				//we need to set the language in the current tab
				tabs.get(tabbedPane.getSelectedIndex()).setLang(2);
				tabs.get(tabbedPane.getSelectedIndex()).enableTriggers();
			}
		});
		languageGroup.add(languageMenuC);
		languageMenu.add(languageMenuC);
	}
	
	public void createNewTab(String name) {
		Tab newTab = new Tab(name);
		tabbedPane.addTab(name, null, newTab.getPanel(), null);
		tabs.add(newTab);
		
		if (setup == false) {
			newTab.enableTriggers();
		}
	}
	
	public void openFile(String name, Tab tab) {
		try {
			Scanner scanner = new Scanner(new File("src\\cs360ProjectImplementation\\demoFiles\\javaDemoFile.java"));
			String file = "";
			while (true) {
				file = file + scanner.nextLine();
				if (scanner.hasNextLine()) { //simultaneously tests whether or not a newline is needed, and whether or not another loop iteration is needed
					file = file + "\r\n";
				}
				else {
					break;
				}
			}
			tab.getTextPane().setText(file);
			scanner.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}