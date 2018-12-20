//Made by Clément SAUVARD
package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import Controller.Menus;
import Controller.NavigationButtons;
import Controller.PhotoComponent;
import Controller.UtilityTools;

public class BrowserUI extends JFrame{

	JPanel mainPanel;
	JPanel menuPanel;
	JPanel statusPanel;
	JPanel toolbar;
	JPanel photoPanel;
	JPanel testPanel;
	public Menus menuB;
	public NavigationButtons menuNav;
	public StatusBar statusBar;
	public PhotoComponent component;
	
	public BrowserUI() {
		super("PhotoBrowser");
		setupUI();
	}
	
	private void setupUI(){
		
		this.setMinimumSize(new Dimension(300,300));
		this.setPreferredSize(new Dimension(1000,600));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Look and feel from Windows (for JFileChooser, buttons, etc)
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Main panel of the frame
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		//Menu panel (located at the top of the frame) that contains File and view menu
		menuPanel = new JPanel(new BorderLayout());
		menuPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		//Status panel (located at the bottom of the frame)
		statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));
		
		//Toolbar panel (located at the left of the frame) 
		toolbar = new JPanel(new GridBagLayout());
		toolbar.setBorder(BorderFactory.createTitledBorder("Select"));
	
		//Center panel (empty for now) that will contains the pictures
		photoPanel = new JPanel(new BorderLayout());
		photoPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		testPanel = new JPanel(new FlowLayout());
		menuPanel.add(testPanel, BorderLayout.CENTER);
		
		//----------- Creating buttons and stuff------------//
		
        menuB = new Menus(this);
        
        menuPanel.add(menuB, BorderLayout.WEST);
        mainPanel.add(menuPanel, BorderLayout.NORTH);
        
        statusBar = new StatusBar();
        statusPanel.add(statusBar);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        menuNav = new NavigationButtons(this);
        toolbar.add(menuNav);
        mainPanel.add(toolbar,BorderLayout.WEST);
        
        mainPanel.add(photoPanel, BorderLayout.CENTER);
        
        //------------------------------------------//
        
        menuNav.toggleCategory();
        
        this.add(mainPanel);
        
        //Don't forget this /!\
		this.pack();
		this.setVisible(true);
	}
	
	
	
	//Action for File menu
	public void fileFunction(ActionEvent e) {
		
		//Quit application
		if(e.getSource()== menuB.menuQuit)
        {
			System.exit(0);
        }
		
		//Open a JFileChooser
		else if(e.getSource()== menuB.menuImport) {
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		    		"JPG & GIF Images", "jpg", "gif","png");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	statusBar.setMessage("You chose to open this file: " +
		            chooser.getSelectedFile().getAbsolutePath());
		    	
		    	setPhotoComponent(chooser);
		       	
		    }
		}
		
		//Delete photo
		else if(e.getSource()== menuB.menuDelete) {
			statusBar.setMessage("What picture(s) do you want to delete ?");
			photoPanel.removeAll();
			testPanel.removeAll();
			repaint();
		}
	}
	
	
	
	
	
	//Adding PhotoComponent and toolbar
	public void setPhotoComponent(JFileChooser jfc) {
		
		component = new PhotoComponent();
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.getViewport().add(component, null);
        photoPanel.add(jScrollPane);
        
        UtilityTools utilTools = new UtilityTools(this);
        
        testPanel.add(utilTools);
        //menuPanel.add(utilTools, BorderLayout.CENTER);
        component.newPhoto(jfc.getSelectedFile().getAbsolutePath());
        
	}
}
