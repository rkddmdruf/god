package main;

import java.awt.Color;

import javax.swing.JButton;

public class CustumButton extends JButton{

	public CustumButton(String string) {
		super(string);
		setBackground(Color.blue);
		setForeground(Color.white);
	}
}
