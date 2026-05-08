package utils;

import java.awt.Color;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class getter {

	public static Stack<Runnable> frames = new Stack<>();
	public static final Color color = new Color(33,33,33);
	public static ImageIcon getImage(String s, int w, int h) {
		return new ImageIcon(new ImageIcon(s).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void infor(String s) {
		JOptionPane.showMessageDialog(null, s, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void err(String s) {
		JOptionPane.showMessageDialog(null, s, "경고", JOptionPane.ERROR_MESSAGE);
	}
}
