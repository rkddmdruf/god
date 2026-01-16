package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class SetFrames {
	
	public static void setframe(JFrame f, String title, int w, int h) {
		f.getContentPane().setBackground(Color.white);
		f.setTitle(title);
		f.setSize(new Dimension(w, h));
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		f.setVisible(true);
	}
}
