package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

public class sp {

	public static Row user = new Row() {{
		add(1);
	}};
	
	public static String s = BorderLayout.SOUTH;
	public static String n = BorderLayout.NORTH;
	public static String w = BorderLayout.WEST;
	public static String e = BorderLayout.EAST;
	public static String c = BorderLayout.CENTER;
	
	public static Color color = new Color(250, 125, 0);
	
	public static Border line = BorderFactory.createLineBorder(Color.black);
	public static Font font(int font, int size) {
		return new Font("맑은 고딕", font, size);
	}
	public static void ErrMes(String string) {
		JOptionPane.showMessageDialog(null, string, "에러", JOptionPane.ERROR_MESSAGE);
	}
	public static void InforMes(String string) {
		JOptionPane.showMessageDialog(null, string, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	public static class Cb extends JButton {
		public Cb(){
			this.setFocusable(false); 
			this.setOpaque(true);
			this.setContentAreaFilled(false);
			this.setBorderPainted(false);
		}
	}
	public static JButton Cb(String string) {
		JButton but = Cb();
		but.setText(string);
		return but;
	}
	public static JButton Cb() {
		JButton but = new Cb();
		return but;
	}
	public static JButton Cb(ImageIcon img) {
		JButton but = new Cb();
		but.setIcon(img);
		return but;
	}
}
