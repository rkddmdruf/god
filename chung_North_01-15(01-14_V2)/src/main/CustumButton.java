package main;

import java.awt.Color;

import javax.swing.JButton;

public class CustumButton extends JButton{
	public CustumButton(String text) {
		super(text);
		setBackground(Color.blue);
		setForeground(Color.white);
	}
}
