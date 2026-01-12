package main;

import java.awt.Color;

import javax.swing.JButton;

public class CustumButton extends JButton{

	CustumButton(String string){
		setText(string);
		setBackground(Color.blue);
		setForeground(Color.white);
	}
}
