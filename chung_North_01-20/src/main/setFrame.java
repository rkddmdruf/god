package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class setFrame {

	public static void setframe(JFrame f, String s, int w, int h) {
		f.setTitle(s);
		f.getContentPane().setBackground(Color.white);
		f.setSize(new Dimension(w, h));
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		f.setVisible(true);
	}
}
