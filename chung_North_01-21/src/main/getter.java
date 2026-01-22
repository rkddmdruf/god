package main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class getter {
	
	public static void moveFrom(int index, JFrame f) {
		if(index == 1) new Main();
		if(index == 2) new Login();
		if(index == 3) new MovieSerch();
	}
	
	public static ImageIcon getImageIcon(String file, int w, int h) {
		return new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void mg(String mg, int type) {
		JOptionPane.showMessageDialog(null, mg, type == JOptionPane.ERROR_MESSAGE ? "경고" : "정보", type);
	}
}
