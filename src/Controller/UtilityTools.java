//Made by Clément SAUVARD
package Controller;

import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import View.BrowserUI;

public class UtilityTools extends JPanel{

	BrowserUI buUI;
	Vector visFonts;
	
	public UtilityTools(BrowserUI bu) {
		buUI = bu;
		setLayout(new FlowLayout());
		initTools();
	}
	
	//toolbar for font size, font, thickness stroke, color
	public void initTools() {
		
		//get all fonts
		String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		visFonts = new Vector(fontNames.length);
		for (int i=0; i<fontNames.length; i++) {
			visFonts.add(fontNames[i]);
		}
		
        JLabel thickness = new JLabel("Set thickness : ");
        JLabel fontF = new JLabel("Font : ");
        JLabel fontS = new JLabel("Font size : ");
        JButton buttonColor = new JButton ("Change Color");
        SpinnerNumberModel model1 = new SpinnerNumberModel(5, 1, 25, 1);  
        JSpinner inputThickness = new JSpinner(model1);
        SpinnerNumberModel model2 = new SpinnerNumberModel(32, 2, 100, 2);  
        JSpinner inputFontSize = new JSpinner(model2);
        JComboBox fontCombo = new JComboBox(visFonts); // JComboBox of fonts
        fontCombo.setSelectedItem("Arial");
        
        this.add(fontF);
        this.add(fontCombo);
        this.add(fontS);
        this.add(inputFontSize);
        this.add(thickness);
        this.add(inputThickness);
        this.add(buttonColor);
        
        // Listeners
        inputThickness.addChangeListener(
				ev-> buUI.component.setThickness(((Integer)inputThickness.getValue()))
        );
        
        inputFontSize.addChangeListener(
				ev-> buUI.component.setFontSize(((Integer)inputFontSize.getValue()))
        );
        
        fontCombo.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		if(e.getStateChange() == ItemEvent.SELECTED) {
        		  buUI.component.setFontText((String)e.getItem());
        		}
        	}
        });
        
        buttonColor.addActionListener(
				ev-> buUI.component.showColorChooser()
		);
	}
}
