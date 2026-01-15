package main;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class A_setFrame{
	public static void  setting(JFrame f, String string, int w, int h) {
		f.setTitle(string);
		f.setSize(w, h);
		f.getContentPane().setBackground(Color.white);
		f.setLocationRelativeTo(null);
		f.setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		f.setDefaultCloseOperation(f.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}
}
