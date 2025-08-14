package utils;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Cb extends JButton{
	
	public Cb(ImageIcon imageIcon) {
		this.setIcon(imageIcon);
		this.setBackground(Color.white);
		this.setFocusable(false); 
		this.setContentAreaFilled(false);
		this.setOpaque(false);
	}
	public Cb() {
		this.setBackground(Color.white);
		this.setFocusable(false); 
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
	}
	public Cb(String string) {
		this.setText(string);
		this.setBackground(Color.white);
		this.setFocusable(false); 
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
	}
}
