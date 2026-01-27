package utils;

import java.awt.Color;

import javax.swing.JButton;

public class CustumButton extends JButton{

	public CustumButton(String s) {
		super(s);
		setBackground(Color.blue);
		setForeground(Color.white);
	}
}
