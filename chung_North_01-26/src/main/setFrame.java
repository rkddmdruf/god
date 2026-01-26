package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class setFrame {
	
	public static void setframe(JFrame f, String s, int w, int h) {
		f.setTitle(s);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(new Dimension(w + 16, h + 39));
		f.setIconImage(getter.getImage("datafiles/로고1.jpg", w, h).getImage());
		f.setLocationRelativeTo(null);
		f.getContentPane().setBackground(Color.white);
		f.setVisible(true);
	}
}
