package main;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class getter {
	
	public static ImageIcon getImageIcon(String file, int w, int h) {
		return new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void mg(String s, int type) {
		JOptionPane.showMessageDialog(null, s, type == JOptionPane.ERROR_MESSAGE ? "경고" : "정보", type);
	}
}
