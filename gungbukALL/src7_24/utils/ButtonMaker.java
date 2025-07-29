package utils;

import java.awt.Dimension;

import javax.swing.JButton;

public class ButtonMaker extends JButton{
	
	public ButtonMaker(String name,int W, int H) {
		this.setText(name);
		this.setPreferredSize(new Dimension(W,H));
	}
}
