package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class getter {
	private static List<Runnable> frames = new ArrayList<>();
	
	public static void push(Runnable runnable) {
		frames.add(runnable);
	}
	
	public static Runnable pop() {
		Runnable r = frames.get(frames.size() - 1);
		frames.remove(frames.size() - 1);
		return r;
	}
	
	public static ImageIcon getImageIcon(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void mg(String msg, int tpye) {
		JOptionPane.showMessageDialog(null, msg, tpye == JOptionPane.ERROR_MESSAGE ? "경고" : "정보", tpye);
	}
}
