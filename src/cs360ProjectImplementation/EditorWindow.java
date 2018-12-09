/*
 * EditorWindow
 * 
 * main window for the program
 */

package cs360ProjectImplementation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javax.swing.text.BadLocationException; 
import java.util.Scanner;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class EditorWindow extends JFrame {
	private JTabbedPaneCloseable tabbedPane = new JTabbedPaneCloseable();
	private JMenuBar menuBar = new JMenuBar();
	
	private static Language[] languages;
	private JRadioButtonMenuItem languageMenuItems[];
	private int menuLanguage = -1; //nonexistent, default
	
	private JMenuItem fileMenuOpen;
	private JMenuItem fileMenuNew;
	private JMenuItem fileMenuSave;
	private JMenuItem fileMenuSaveAs;
	private JMenuItem fileMenuClose;
	
	private File lastPath; //tracks the last path the user either saved to or opened from, to set path for file dialog
	
	public static void main(String[] args) {
		loadLanguages();
		
		new EditorWindow();
	}
	
	public static void loadLanguages() {
		File folder = new File("languages/");
		
		//error handling. If this configuration folder doesn't exist, we can't do anything, hard fail
		if (folder.exists() == true) {
			File[] listOfFiles = folder.listFiles();
			
			languages = new Language[listOfFiles.length+1];
			
			languages[0] = new Language("Plaintext", null, "txt", null); //dont need to load this one, its always present
			
			for (int i=1; i<languages.length; i++) {
				languages[i] = Language.deserializeLanguage(folder + "/" + listOfFiles[i-1].getName());
			}
		}
		else {
	        JOptionPane.showMessageDialog(null, "Language configuration files not found", "CS360 Editor", JOptionPane.ERROR_MESSAGE);
	        System.exit(1);
		}
	}
	
	public EditorWindow() {
		setSize(900, 700);
		setTitle("Code Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		//blank tab
		createNewTab(null);
		
		System.out.println(activeTab().toString()); //this line needs to be here or it doesn't work. I have no idea what the actual fuck is happening here
		activeTab().enableTriggers();
		
		menuSetup();
	}
	
	public void menuSetup() {
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
		fileMenuSetup();
		editMenuSetup();
		languageMenuSetup();
		insertMenuSetup();
	}
	
	public void insertMenuSetup() {
		if (tabbedPane.getSelectedComponent() != null) {
			//get language of active tab
			int langIndex = ((Tab) tabbedPane.getSelectedComponent()).getLangIndex();
			
			if (langIndex != menuLanguage) {
				menuLanguage = langIndex;
				JMenu insertMenu = new JMenu("Insert");
				
				Language selectedLang = languages[langIndex];
				
				if (menuBar.getMenu(3) != null) {
					menuBar.remove(3);
				}
				
				InsertableCode[] insertables = selectedLang.getInsertableCode();
				
				//validate that the Language actually has some insertables defined
				if (insertables != null) {
					for (int i=0; i<insertables.length; i++) {
						JMenuItem newItem = new JMenuItem(insertables[i].getName());
						newItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JMenuItem sourceItem = (JMenuItem) e.getSource();
								String sourceName = sourceItem.getText();
								
								//loop through the language object until we find the one with this name (kludgy, will consider making InsertableCode extend JMenuItem later?)
								Language language = languages[((Tab) tabbedPane.getSelectedComponent()).getLangIndex()];
								InsertableCode[] insertables = language.getInsertableCode();
								for (int i=0; i<insertables.length; i++) {
									if (insertables[i].getName().equals(sourceName)) {
										String selected = activeTextPane().getSelectedText();
										
										activeTextPane().replaceSelection(" ");
										
										int position = activeTextPane().getCaretPosition();
										
										String[] insert = insertables[i].getCode(selected);
										
										try {
											for (int j=0; j<insert.length; j++) {
												activeTextPane().getStyledDocument().insertString(position, "\n", null);
												activeTextPane().getStyledDocument().insertString(position, insert[j], null);
												position = activeTextPane().getCaretPosition();
											}
										}
										catch (BadLocationException ex) {
											JOptionPane.showMessageDialog(null, "IO error: " + ex.toString(), "CS360 Editor", JOptionPane.ERROR_MESSAGE);
										}
									}
								}
							}
						});
						insertMenu.add(newItem);
					}
				}
				
				insertMenu.addMenuListener(new MenuListener() {
					@Override
					public void menuCanceled(MenuEvent e) {
					}

					@Override
					public void menuDeselected(MenuEvent e) {
					}

					@Override
					public void menuSelected(MenuEvent e) {
						//each time the insert menu is selected, we check if it needs to be updated
						insertMenuSetup();
					}
				});
				
				menuBar.add(insertMenu);
				menuBar.revalidate();
			}
		}
	}
	
	public void fileMenuSetup() {
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		fileMenuNew = new JMenuItem("New");
		fileMenu.add(fileMenuNew);
		fileMenuNew.addActionListener(new ButtonHandler());
		
		fileMenuOpen = new JMenuItem("Open");
		fileMenuOpen.addActionListener(new ButtonHandler());
		fileMenu.add(fileMenuOpen);
		
		fileMenuSave = new JMenuItem("Save");
 		fileMenuSave.addActionListener(new ButtonHandler());
		fileMenu.add(fileMenuSave);
		
		fileMenuSaveAs = new JMenuItem("Save As");
 		fileMenuSaveAs.addActionListener(new ButtonHandler());
		fileMenu.add(fileMenuSaveAs);
		
		fileMenuClose = new JMenuItem("Close");
		fileMenu.add(fileMenuClose);
 		fileMenuClose.addActionListener(new ButtonHandler()); 
	}
	
	public void editMenuSetup() {
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		JMenuItem editMenuUndo = new JMenuItem("Undo");
		editMenuUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				activeTab().undo();
			}
		});
		editMenu.add(editMenuUndo);
		
		JMenuItem editMenuRedo = new JMenuItem("Redo");
		editMenuRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				activeTab().redo();
			}
		});
		editMenu.add(editMenuRedo);
		
		JMenuItem editMenuFindReplace = new JMenuItem("Find and replace");
		editMenuFindReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FindReplacePopup findReplace = new FindReplacePopup();
				
				findReplace.getreplaceNextButton().addActionListener(new ActionListener() {
					//Replace Next get the string from the replace text box and replaces the first instance of it in the current file
					public void actionPerformed(ActionEvent FR) {
						String text = activeTextPane().getText();
						String find = findReplace.getFindQuery();
						String replace = findReplace.getReplaceTerm();
						text = text.replaceFirst(find, replace);
						activeTextPane().setText(text);
					}
				});
				
				findReplace.getReplaceAllButton().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent FR) {
						String text = activeTextPane().getText();
						String find = findReplace.getFindQuery();
						String replace = findReplace.getReplaceTerm();
						text = text.replaceAll(find, replace);
						activeTextPane().setText(text);
					}
				});
			}
		});
		editMenu.add(editMenuFindReplace);
	}
	
	public void languageMenuSetup() {
		//procedurally generates menu listing all languages
		ButtonGroup languageGroup = new ButtonGroup();
		JMenu languageMenu = new JMenu("Languages");
		
		languageMenuItems = new JRadioButtonMenuItem[languages.length];
		
		for (int i=0; i<languages.length; i++) {
			languageMenuItems[i] = new JRadioButtonMenuItem(languages[i].getName());
			
			languageMenuItems[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					//first we need to find out the index of the clicked button (this is kludgy as hell, if someone can find a better way thatd be great)
					String clickedText = ((AbstractButton) ev.getSource()).getText();
					
					for (int i=0; i<languages.length; i++) {
						if (clickedText.equals(languages[i].getName())) {
							//we need to set the language in the current tab
							activeTab().setLanguageByIndex(i);
							activeTab().enableTriggers();
						}
					}
				}
			});
			languageGroup.add(languageMenuItems[i]);
			languageMenu.add(languageMenuItems[i]);
		}
		
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
				int currentTabLang = activeTab().getLangIndex();
				
				languageMenuItems[currentTabLang].setSelected(true);
			}
		});
		
		menuBar.add(languageMenu);
	}
	
	public JFileChooser chooserWithPath() {
		if (lastPath == null) {
			return new JFileChooser();
		}
		
		return new JFileChooser(lastPath);
	}
	
	public void createNewTab(File file) {
		Tab newTab;
		if (file == null) {
			newTab = new Tab("New tab", languages, this);
			tabbedPane.addTab("New tab", null, newTab, null);
		}
		else {
			String name = file.getName();
			newTab = new Tab(name, languages, this);
			newTab.setDiskLocation(file);
			
			newTab.setLanguageByExtension(file);
			
			try {
				Scanner scanner = new Scanner(file);
				String content = "";
				while (true) {
					content = content + scanner.nextLine();
					if (scanner.hasNextLine()) { //simultaneously tests whether or not a newline is needed, and whether or not another loop iteration is needed
						content = content + "\r\n";
					}
					else {
						break;
					}
				}
				scanner.close();
				
				newTab.getTextPane().setText(content);
				newTab.enableTriggers();
				newTab.setSaved();
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error: " + ex.toString(), "CS360 Editor", JOptionPane.ERROR_MESSAGE);
			}
			
			tabbedPane.addTab(name, null, newTab, file.toString());
		}
		
		//automatically shift the selection to the new tab
		tabbedPane.setSelectedComponent(newTab);
		
		newTab.enableTriggers();
	}
	
	public void openFile() {
		try {
			JFileChooser j = chooserWithPath();
			int result = j.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = j.getSelectedFile();
				lastPath = new File(file.getAbsoluteFile().getParent());
				createNewTab(file);
			}
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error: " + ex.toString(), "CS360 Editor", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public int saveFile() {
		//0 = success, 1 = error, -1 = canceled (in saveAsFile())
		File file = activeTab().getDiskLocation();
		
		if (file == null) {
			return saveAsFile();
		}
		else {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
				bw.write(activeTextPane().getText());
				bw.close();
				
				activeTab().setSaved(); //tell Tab we saved its file
				
				return 1;
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error: " + ex.toString(), "CS360 Editor", JOptionPane.ERROR_MESSAGE);
				return 0;
			}
		}
	}
	
	public int saveAsFile() {
		//0 = success, 1 = error, -1 = canceled
		try {
			JFileChooser j = chooserWithPath();
			int result = j.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = j.getSelectedFile();
				lastPath = new File(file.getAbsoluteFile().getParent());
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(activeTextPane().getText());
				bw.close();
				
				activeTab().setDiskLocation(file);
				
				//create a new tab with the same values as the old one, except name and tooltip, and set its disk location
				Tab oldTab = ((Tab) tabbedPane.getSelectedComponent());
				
				activeTab().setSaved(); //tell Tab we saved its file
				activeTab().setLanguageByExtension(file);
				tabbedPane.remove(tabbedPane.getSelectedIndex());
				
				tabbedPane.addTab(file.getName(), null, oldTab, file.toString());
				
				//automatically shift the selection to the new tab
				tabbedPane.setSelectedComponent(oldTab);
				
				return 1;
			}
			else {
				return -1;
			}
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error: " + ex.toString(), "CS360 Editor", JOptionPane.ERROR_MESSAGE);
			return 0;
		}
	}
	
	public JTextPane activeTextPane() {
		return ((Tab) tabbedPane.getSelectedComponent()).getTextPane();
	}
	
	public Tab activeTab() {
		return (Tab) tabbedPane.getSelectedComponent();
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == fileMenuOpen) {
				openFile();
			}
			else if (e.getSource() == fileMenuSave) {
				saveFile();
			}
			else if (e.getSource() == fileMenuNew) {
				createNewTab(null);
			}
			else if (e.getSource() == fileMenuSaveAs) {
				saveAsFile();
			}
			else if (e.getSource() == fileMenuClose) {
				System.exit(0);
			}
		}
	}
}