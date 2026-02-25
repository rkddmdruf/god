package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.CFrame;
import utils.getter;

public class Login extends CFrame{

	
	
	public Login() {
		borderPanel.setBorder(BorderFactory.createEmptyBorder(7,7,7,7));
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			add(new JLabel(getter.getImageIcon("datafiles/icon/logo.png", 40, 40)));
			add(new JLabel("Skills Qualification Association"));
		}}, BorderLayout.NORTH);
		setFrame("로그인", 275, 175);
	}
	
	
	public static void main(String[] args) {
		new Login();
	}
}
