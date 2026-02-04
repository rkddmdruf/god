package utils;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class getter {
	private static Runnable r;
	public static Runnable r2;
	
	public static final Color COLOR = new Color(0, 160, 250);
	public static final DecimalFormat df = new DecimalFormat("###,###");
	public static ImageIcon getImage(String s, int w, int h) {
		return new ImageIcon(new ImageIcon("datafiles/" + s).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void mg(String s, int type) {
		JOptionPane.showMessageDialog(null, s, type == JOptionPane.ERROR_MESSAGE ? "경고" : "정보", type);
	}
	
	public static Runnable getR() {
		return r;
	}
	public static void setR(Runnable run) {
		r = run;
	}
	
	public static void fromMove(JFrame f) {
		r.run();
		f.dispose();
	}
}
