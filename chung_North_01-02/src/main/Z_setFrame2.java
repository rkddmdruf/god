package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Z_setFrame2 extends JFrame{
	public Z_setFrame2(String string, int w, int h) {
		change(string, w, h);
		noChange();
	}
	
	private void change(String string, int w, int h) {
		setTitle(string);
		setSize(new Dimension(w + 16, h + 39));
	}
	
	private void noChange() {
		setBackground(Color.white);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);  // X 버튼 동작
		setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		setLocationRelativeTo(null);  // 화면 중앙
		setVisible(true);  // 보이기
	}
}
