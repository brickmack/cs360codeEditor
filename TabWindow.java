package cs360ProjectImplementation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class TabWindow extends JFrame {
	private JTabbedPaneCloseable tabbedPane = new JTabbedPaneCloseable();
	private JMenuBar menuBar = new JMenuBar();
	
	private JRadioButtonMenuItem languageMenuItems[];
	
	private static Color darkGreen = new Color(18, 119, 2);
	private static Color purple = new Color(90, 2, 119);
	
	private boolean setup = true; //Dont want to enable triggers for Highlight and stuff while everything is still loading, or it breaks stuff
	
	private JMenuItem fileMenuOpen;
	private JMenuItem fileMenuNew;
	private JMenuItem fileMenuSave;
	private JMenuItem fileMenuSaveAs;
	private JMenuItem fileMenuClose;
	
	private int menuLanguage = -1;
	
	private static Language[] languages;
	
	public static void main(String[] args) {
		loadLanguages();
		
		new TabWindow();
	}
	
	public static void loadLanguages() {
		languages = new Language[3];
		languages[0] = new Language("Plaintext", null, "txt", null);
		
		String keysString = "\\babstract\\b|\\bassert\\b|\\bboolean\\b|\\bbreak\\b|\\bbyte\\b|\\bcase\\b|\\bcatch\\b|\\bchar\\b|\\bclass\\b|\\bconst\\b|\\bcontinue\\b|\\bdefault\\b|\\bdo\\b|\\bdouble\\b|\\belse\\b|\\bextends\\b|\\bfalse\\b|\\bfinal\\b|\\bfinally\\b|\\bfloat\\b|\\bfor\\b|\\bgoto\\b|\\bif\\b|\\bimplements\\b|\\bimport\\b|\\binstanceof\\b|\\bint\\b|\\binterface\\b|\\blong\\b|\\bnative\\b|\\bnew\\b|\\bnull\\b|\\bpackage\\b|\\bprivate\\b|\\bprotected\\b|\\bpublic\\b|\\breturn\\b|\\bshort\\b|\\bstatic\\b|\\bstrictfp\\b|\\bsuper\\b|\\bswitch\\b|\\bsynchronized\\b|\\bthis\\b|\\bthrow\\b|\\bthrows\\b|\\btransient\\b|\\btrue\\b|\\btry\\b|\\bvoid\\b|\\bvolatile\\b|\\bwhile\\b\r\n";
		HighlightRule keywords = new HighlightRule("keywords", new String[] {keysString}, purple, false);
		HighlightRule stringDef = new HighlightRule("string", new String[] {"(\"([^\"]*)\")"}, Color.blue, false);
		HighlightRule comment = new HighlightRule("comment", new String[] {"((?m)//(.*)$)|((?s)/\\*(.*?)\\*/)"}, darkGreen, true);
		
		InsertableCode ifBlock = new InsertableCode("If", "if ( ) {\n", "\n}");
		InsertableCode whileBlock = new InsertableCode("While", "while ( ) {\n", "\n}");
		
		languages[1] = new Language("Java", new HighlightRule[] {keywords, comment, stringDef}, "java", new InsertableCode[] {ifBlock, whileBlock});
		
		languages[2] = new Language("C", null, "c", null);
	}
	
	public TabWindow() {
 		this.addKeyListener(new Key());
 		
		setSize(900, 700);
		setTitle("Code Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		//blank tab
		createNewTab("New tab");
		
		System.out.println(tabbedPane.getSelectedComponent().toString()); //this line needs to be here or it doesn't work. I have no idea what the actual fuck is happening here
		((Tab) tabbedPane.getSelectedComponent()).enableTriggers();
		
		menuSetup();
		
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
		if (tabbedPane.getSelectedComponent() != null) {
			//get language of active tab
			int langIndex = ((Tab) tabbedPane.getSelectedComponent()).getLangIndex();
			
			if (langIndex != menuLanguage) {
				menuLanguage = langIndex;
				JMenu insertMenu = new JMenu("Insert");
				
				Language selectedLang = languages[langIndex];
				
				if (menuBar.getMenu(3) != null) {
					System.out.println("removed");
					menuBar.remove(3);
				}
				
				String out = "Got insertables for";
				InsertableCode[] insertables = selectedLang.getInsertableCode();
				
				//validate that the Language actually has some insertables defined
				if (insertables != null) {
					for (int i=0; i<insertables.length; i++) {
						out = out + " " + insertables[i].getName();
						JMenuItem newItem = new JMenuItem(insertables[i].getName());
						newItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JMenuItem sourceItem = (JMenuItem) e.getSource();
								String sourceName = sourceItem.getText();
								
								System.out.println("Clicked " + sourceName);
								
								//loop through the language object until we find the one with this name (kludgy, will consider making InsertableCode extend JMenuItem later?)
								Language language = languages[((Tab) tabbedPane.getSelectedComponent()).getLangIndex()];
								InsertableCode[] insertables = language.getInsertableCode();
								for (int i=0; i<insertables.length; i++) {
									if (insertables[i].getName().equals(sourceName)) {
										System.out.println(insertables[i].getCode("test"));
									}
								}
								
							}
						});
						insertMenu.add(newItem);
					}
					System.out.println(out);
				}
				else {
					System.out.println(selectedLang.getName() + " Had no insertables");
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
				System.out.println("undo in tab " + tabbedPane.getSelectedIndex());
				((Tab) tabbedPane.getSelectedComponent()).undo();
			}
		});
		editMenu.add(editMenuUndo);
		
		JMenuItem editMenuRedo = new JMenuItem("Redo");
		editMenuRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("redo in tab " + tabbedPane.getSelectedIndex());
				((Tab) tabbedPane.getSelectedComponent()).redo();
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
						//System.out.println("Find " + findReplace.getFindQuery() + " in tab " + tabbedPane.getSelectedIndex());
						String text = ((Tab) tabbedPane.getSelectedComponent()).getTextPane().getText();
						String find = findReplace.getFindQuery();
						String replace = findReplace.getReplaceTerm();
						text = text.replaceFirst(find, replace);
						((Tab) tabbedPane.getSelectedComponent()).getTextPane().setText(text);
					}
				});
				findReplace.getReplaceAllButton().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent FR) {
						String text = ((Tab) tabbedPane.getSelectedComponent()).getTextPane().getText();
						String find = findReplace.getFindQuery();
						String replace = findReplace.getReplaceTerm();
						text = text.replaceAll(find, replace);
						((Tab) tabbedPane.getSelectedComponent()).getTextPane().setText(text);
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
							((Tab) tabbedPane.getSelectedComponent()).setLangIndex(i);
							((Tab) tabbedPane.getSelectedComponent()).enableTriggers();
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
				int currentTabLang = ((Tab) tabbedPane.getSelectedComponent()).getLangIndex();
				
				languageMenuItems[currentTabLang].setSelected(true);
			}
		});
		
		menuBar.add(languageMenu);
	}
	
	public void createNewTab(String name) {
		Tab newTab = new Tab(name, languages);
		tabbedPane.addTab(name, null, newTab, null);
		
		//automatically shift the selection to the new tab
		tabbedPane.setSelectedComponent(newTab);
		
		if (setup == false) {
			newTab.enableTriggers();
		}
	}
	
	public String getFileExtension(File file) {
		//convenience method. Will probably merge into openFile later, since thats the only place we use it
	    if (file == null) {
	        return "";
	    }
	    String name = file.getName();
	    int i = name.lastIndexOf('.');
	    String ext = i > 0 ? name.substring(i + 1) : "";
	    return ext;
	}
	
	public void openFile() {
		try {
			JFileChooser j = new JFileChooser();
			int result = j.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = j.getSelectedFile();
				System.out.println(file.toString());
				createNewTab(file.getName());
				
				Scanner scanner = new Scanner(file);
				String content = "";
				while (true) {
					content = content + scanner.nextLine();
					System.out.println(content);
					if (scanner.hasNextLine()) { // simultaneously tests whether or not a newline is needed, and whether
													// or not another loop iteration is needed
						content = content + "\r\n";
					}
					else {
						break;
					}
				}
				
				//set language
				String extension = getFileExtension(file);
				for (int i=0; i<languages.length; i++) {
					if (languages[i].getFileExtension().equals(extension)) {
						((Tab) tabbedPane.getSelectedComponent()).setLangIndex(i);
					}
				}
				
				((Tab) tabbedPane.getSelectedComponent()).getTextPane().setText(content);
				((Tab) tabbedPane.getSelectedComponent()).enableTriggers();
				scanner.close();
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void saveFile() {
		String name = ((Tab) tabbedPane.getSelectedComponent()).getName();
		if (name.equals("New tab")) {
			saveAsFile();
		}
		else {
			File file = new File(name);
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
				bw.write(((Tab) tabbedPane.getSelectedComponent()).getTextPane().getText());
				bw.close();
				System.out.println("Save success");
			}
			catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
	
	public void saveAsFile() {
		try {
			JFileChooser j = new JFileChooser();
			int result = j.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = j.getSelectedFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				//bw.write(tabs.get(tabbedPane.getSelectedIndex()).getTextPane().getText());
				bw.close();
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
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
				createNewTab("New tab");
			}
			else if (e.getSource() == fileMenuSaveAs) {
				saveAsFile();
			}
			else if (e.getSource() == fileMenuClose) {
				System.exit(0);
			}
		}
	}
	
	private class Key implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			 if ((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                 saveFile();
             }
			 else if ((e.getKeyCode() == KeyEvent.VK_O) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
				 openFile();
			 }
		}
		@Override
		public void keyReleased(KeyEvent arg0) {
		}
		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}
}