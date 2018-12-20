//Made by Clément SAUVARD
package Model;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class DrawAnnotations {

	private int thickness;
	private Color color;
	public GeneralPath path;
	public boolean isSelected;

	public DrawAnnotations(int thick, Color col, GeneralPath gp) {
		thickness = thick;
		color = col;
		path = gp;
		
	}

	public int getThickness() {
		return thickness;
	}

	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public GeneralPath getPath() {
		return path;
	}

	public void setPath(GeneralPath path) {
		this.path = path;
	}
}
