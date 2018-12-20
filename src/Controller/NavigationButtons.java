//Made by Clément SAUVARD
package Controller;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import View.BrowserUI;

public class NavigationButtons extends JPanel{

	public ButtonGroup group;
	public JToggleButton family;
	public JToggleButton vacation;
	public JToggleButton school;
	BrowserUI buUI;
	
	public NavigationButtons(BrowserUI bu){
		buUI = bu;
		initNavButtons();
		this.setLayout(new GridLayout(3,0,0,10));
	}
	
	public void initNavButtons() {
		family = new JToggleButton("Family");
        family.setSelected(true);
        this.add(family);

        vacation = new JToggleButton("Vacation");
        this.add(vacation);
        
        school = new JToggleButton("School");
        this.add(school);
        
        group = new ButtonGroup();
        group.add(family);
        group.add(vacation);
        group.add(school);
        
        // Listener for toolbar
        family.addActionListener(
				e-> toggleCategory()
		);
        vacation.addActionListener(
				e-> toggleCategory()
		);
        school.addActionListener(
				e-> toggleCategory()
		);
	}
	//Self-explained in the function name (for the pictures)
	public void toggleCategory(){
		if (group.getSelection().equals(family.getModel())) {
			 buUI.statusBar.setMessage("Family photo selected");
		}
		else if (group.getSelection().equals(vacation.getModel())) {
			buUI.statusBar.setMessage("Vacation photo selected");
		}
		else if (group.getSelection().equals(school.getModel())) {
			buUI.statusBar.setMessage("School photo selected");
		}
	}
}
