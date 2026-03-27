package utils;

import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class getter {

	public static Stack<Runnable> frames = new Stack<>();
	
	public static ImageIcon getImage(String s, int w, int h) {
		return new ImageIcon(new ImageIcon(s).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void infor(String s) {
		Jop(s, true);
	}
	
	public static void err(String s) {
		Jop(s, false);
	}
	
	private static void Jop(String s, boolean b) {
		JOptionPane.showMessageDialog(null, s, b ? "정보" : "경고", b ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
	}
}
