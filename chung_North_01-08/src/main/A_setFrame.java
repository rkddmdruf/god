package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class A_setFrame{
	private JFrame f;
	public A_setFrame(JFrame f, String string, int w, int h) {
		this.f = f;
		change(string, w, h);
		noChange();
	}
	
	private void change(String string, int w, int h) {
		f.setTitle(string);
		f.setSize(new Dimension(w + 16, h + 39));
	}
	
	private void noChange() {
		f.setBackground(Color.white);
		f.setDefaultCloseOperation(f.DISPOSE_ON_CLOSE);
		f.setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		f.setLocationRelativeTo(null);// 화면 중앙
		f.setVisible(true);
	}
}
