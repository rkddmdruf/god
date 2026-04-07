package utils;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CFrame extends JFrame{

	protected JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
	}};
	
	protected void setFrame(String s, int w, int h) {
		add(borderPanel);
		setTitle(s);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(w + 16, h + 39);
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.white);
		setVisible(true);
	}
}
