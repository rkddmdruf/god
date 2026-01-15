package main;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class A_setFrame{
	JFrame f;
	public A_setFrame(JFrame f, String s, int w, int h) {
		this.f = f;
		chaged(s, w, h);
		noChaged();
	}
	
	private void chaged(String s, int w, int h) {
		f.setTitle(s);
		f.setSize(new Dimension(w, h));
	}
	
	private void noChaged() {
		
		f.setLocationRelativeTo(null);
		f.getContentPane().setBackground(Color.white);
		f.setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		f.setDefaultCloseOperation(f.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}
}
