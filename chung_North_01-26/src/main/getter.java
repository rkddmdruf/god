package main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class getter {
	
	private static Runnable run;
	public static ImageIcon getImage(String s, int w, int h) {
		return new ImageIcon(new ImageIcon(s).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void mg(String s, int type) {
		JOptionPane.showMessageDialog(null, s, type == JOptionPane.ERROR_MESSAGE ? "경고" : "정보", type);
	}
	
	public static void setRun(Runnable r) {
		run = r;
	}
	
	public static void run_Dispose(JFrame f) {
		run.run();
		f.dispose();
	}
}
