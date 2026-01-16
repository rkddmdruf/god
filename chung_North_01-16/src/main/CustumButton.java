package main;

import java.awt.Color;

import javax.swing.JButton;

public class CustumButton extends JButton{
	public CustumButton(String s) {
		this.setText(s);
		this.setBackground(Color.blue);
		this.setForeground(Color.white);
	}
}
