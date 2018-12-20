//Made by Clément SAUVARD
package Controller;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import View.BrowserUI;

public class Menus extends JMenuBar{

	public JMenuBar menuBar;
	public JMenuItem menuImport;
	public JMenuItem menuDelete;
	public JMenuItem menuQuit;
	public JRadioButtonMenuItem photoVMenu;
	public JRadioButtonMenuItem browserMenu;
	public JRadioButtonMenuItem splitMenu;
	public ButtonGroup groupRadioB;
	private BrowserUI buUI;
	
	public Menus(BrowserUI bu) {
		buUI = bu;
		initMenus();
	}
	
	public void initMenus() {

		JMenu menuFile = new JMenu("File");
		JMenu menuView = new JMenu("View");
        
        this.add(menuFile);
        this.add(menuView);
       
        menuImport = new JMenuItem("Import");
        menuFile.add(menuImport);
        menuDelete = new JMenuItem("Delete");
        menuFile.add(menuDelete);
        menuQuit = new JMenuItem("Quit");
        menuFile.add(menuQuit);
        
        // Listener for File menu
        menuImport.addActionListener(
				e-> buUI.fileFunction(e)
		);
        menuDelete.addActionListener(
				e-> buUI.fileFunction(e)
		);
        menuQuit.addActionListener(
				e-> buUI.fileFunction(e)
		);
        
        
        photoVMenu = new JRadioButtonMenuItem("PhotoViewer");
        menuView.add(photoVMenu);
        browserMenu = new JRadioButtonMenuItem("Browser");
        menuView.add(browserMenu);
        splitMenu = new JRadioButtonMenuItem("Split mode");
        menuView.add(splitMenu);
        
        groupRadioB = new ButtonGroup();
        groupRadioB.add(photoVMenu);
        groupRadioB.add(browserMenu);
        groupRadioB.add(splitMenu);
        
        // Listener for View menu
        photoVMenu.addActionListener(
				e-> toggleView()
		);
        browserMenu.addActionListener(
				e-> toggleView()
		);
        splitMenu.addActionListener(
				e-> toggleView()
		);
	}
	
	//Self-explained in the function name
		public void toggleView() {
			if (groupRadioB.getSelection().equals(photoVMenu.getModel())) {
				 buUI.statusBar.setMessage("Single photo viewer mode activated");
			}
			else if (groupRadioB.getSelection().equals(browserMenu.getModel())) {
				buUI.statusBar.setMessage("Browser mode activated");
			}
			else if (groupRadioB.getSelection().equals(splitMenu.getModel())) {
				buUI.statusBar.setMessage("Split mode activated");
			}
		}
	
}
