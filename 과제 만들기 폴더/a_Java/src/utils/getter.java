package utils;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import main.Main1;
import main.Main2_LoginFrame;

public class getter {

	public static Main1 main1 = null;
	public static Main2_LoginFrame main2 = null;
	final public static DecimalFormat df = new DecimalFormat("###,###");
	public static Stack<Runnable> frames = new Stack<>();
	public static final Color color = new Color(33,33,33);
	public static ImageIcon getImage(String s, int w, int h) {
		return new ImageIcon(new ImageIcon(s).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void infor(String s) {
		JOptionPane jop = new JOptionPane();
		jop.setBackground(Color.white);
		for(int i = 0; i < jop.getComponentCount(); i++) {
			jop.getComponent(i).setBackground(Color.white);
		}
		jop.showMessageDialog(null, s, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void err(String s) {
		JOptionPane.showMessageDialog(null, s, "경고", JOptionPane.ERROR_MESSAGE);
	}
}
