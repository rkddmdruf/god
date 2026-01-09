package main;

import java.awt.Color;

import javax.swing.JButton;

public class CustumButton extends JButton{
	public CustumButton(String string) {
		super(string);
		this.setBackground(Color.blue);
		this.setForeground(Color.white);
	}
	
	public void setBack(Color color) {
		this.setBackground(color);
	}
	
}
