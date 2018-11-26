package cs360ProjectImplementation;

import javax.swing.*;
import javax.swing.plaf.metal.MetalIconFactory;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JTabbedPaneCloseable extends JTabbedPane implements ActionListener, ItemListener {
	public JTabbedPaneCloseable() {
        super();
        createPopupMenu(this);
    }

    /* Override Addtab in order to add the close Button every time */
    @Override
    public void addTab(String title, Icon icon, Component component, String tip) {
        super.addTab(title, icon, component, tip);
        int count = this.getTabCount() - 1;
        CloseButtonTab newTab = new CloseButtonTab(component, title, icon, tip);
        setTabComponentAt(count, newTab);
    }
    
    public void createPopupMenu(Component c) {
        //Create the popup menu.
        JPopupMenu popup = new JPopupMenu();
        
        JMenuItem popupCloseTab = new JMenuItem("Close tab");
        popupCloseTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				remove(getSelectedComponent());
			}
		});
        popup.add(popupCloseTab);
        
        JMenuItem popupCloseAllTabs = new JMenuItem("Close all");
        popupCloseAllTabs.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ev) {
        		removeAll();
        	}
        });
        popup.add(popupCloseAllTabs);

        //Add listener to the tab bar so the popup menu can come up.
        MouseListener popupListener = new PopupListener(popup);
        c.addMouseListener(popupListener);
    }

    //Button
    public class CloseButtonTab extends JPanel {
        private Component tab;

        public CloseButtonTab(final Component tab, String title, Icon icon, String tip) {
            this.tab = tab;
            this.setToolTipText(tip);
            setOpaque(false);
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 3, 3);
            setLayout(flowLayout);
            JLabel jLabel = new JLabel(title);
            jLabel.setIcon(icon);
            add(jLabel);
            JButton button = new JButton(MetalIconFactory.getInternalFrameCloseIcon(16));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.addMouseListener(new CloseListener(tab));
            button.setToolTipText("Close");
            add(button);
        }
    }
    
    //ClickListener
    public class CloseListener implements MouseListener {
        private Component tab;

        public CloseListener(Component tab) {
            this.tab=tab;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() instanceof JButton) {
                JButton clickedButton = (JButton) e.getSource();
                JTabbedPane tabbedPane = (JTabbedPane) clickedButton.getParent().getParent().getParent();
                tabbedPane.remove(tab);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
	@Override
	public void itemStateChanged(ItemEvent arg0) {
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}
}

class PopupListener extends MouseAdapter {
    JPopupMenu popup;

    PopupListener(JPopupMenu popupMenu) {
        popup = popupMenu;
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}