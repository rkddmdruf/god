package utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class getter {
	
	private static List<Runnable> list = new ArrayList<>();
	
	public static void psuh(Runnable runnable) {
		list.add(runnable);
	}
	public static Runnable pop() {
		Runnable r = list.get(list.size() - 1);;
		list.remove(list.size() - 1);
		return r;
	}
	
	public static ImageIcon getImageIcon(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void mg(String string ,int tpye) {
		JOptionPane.showMessageDialog(null, string, tpye == JOptionPane.ERROR_MESSAGE ? "경고" : "정보", tpye);
	}
}
