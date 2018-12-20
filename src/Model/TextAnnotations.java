//Made by Clément SAUVARD
package Model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.List;
import java.util.stream.Collectors;

public class TextAnnotations {

	private String text;
	private int xPos;
	private int yPos;
	public char[] chars;
	public int fontSize;
	public String font;
	public Color colorText;
	public boolean isSelected;
	public FontMetrics metrics;
	public int totalW;
	public int sautL;
	
	public TextAnnotations(String text, int xPos, int yPos, String f, int fs, Color col) {
		this.text = text;
		this.xPos = xPos;
		this.yPos = yPos;
		this.font = f;
		this.fontSize = fs;
		this.colorText = col;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = this.text + text;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	
	public void strToChar () {
		//chars = str.chars().mapToObj(e->(char)e).collect(Collectors.toList());
		chars = this.text.toCharArray();
	}
	
	public char[] getChars() {
		return chars;
	}

	public void setColorText(Color colorText) {
		this.colorText = colorText;
	}

	public FontMetrics getMetrics() {
		return metrics;
	}

	
}
