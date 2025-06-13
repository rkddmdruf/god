package utils;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class ButtonMaker extends JButton{
	
	public ButtonMaker(String name, Color color, Color fontColor, int W, int H) {
		this.setText(name);this.setBackground(color);
		this.setPreferredSize(new Dimension(W,H));
		this.setForeground(fontColor);
	}
}
