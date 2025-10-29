package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.text.DecimalFormat;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	public static Color gray = new Color(238,238,238);
	public static DecimalFormat df = new DecimalFormat("###,###");
	public static Border em(int top, int left, int bottom, int right) {
		return BorderFactory.createEmptyBorder(top, left, bottom, right);
	}
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
	public static ImageIcon getImg(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	public static Border line(Color color) {
		return BorderFactory.createLineBorder(color);
	}
	
	/////////////////////////////
	
	
	public static class cp extends JPanel{
		private cp() {}
		public cp(LayoutManager lay, Border b, Color c){
			setLayout(lay);
			setBorder(b);
			setBackground(c == null ? Color.white : c);
		}
		public cp size(int w, int h) {
			this.setPreferredSize(new Dimension(w, h));
			return this;
		}
	}
	
	
	public static class cl extends JLabel{
		private cl() {}
		public cl(ImageIcon img){
			setIcon(img);
		}
		public cl(String str){
			setText(str);
		}
		public cl font(Font font) {
			return (cl) fonts(this, font);
		}
		public cl fontColor(Color color) {
			return (cl) fontColors(this, color);
		}
		public cl setBorders(Border border) {
			this.setBorder(border);
			return this;
		}
		public cl setHo(int h) {
			this.setHorizontalAlignment(h);
			return this;
		}
		public cl size(int w, int h) {
			this.setPreferredSize(new Dimension(w, h));
			return this;
		}
	}
	public static class cc <T extends JComponent>{
		private T jcom;
		public cc(T jcom) {
			this.jcom = jcom;
		}
		public T font(Font font) {
			jcom.setFont(font);
			return jcom;
		}
		public T fontColor(Color color) {
			jcom.setForeground(color);
			return jcom;
		}
		public T setBorders(Border border) {
			jcom.setBorder(border);
			return jcom;
		}
		public T setHo(int h) {
			if(jcom instanceof JButton || jcom instanceof JLabel) {
				((AbstractButton) jcom).setHorizontalAlignment(h);
			}
			return jcom;
		}
		public T get() {
			return jcom;
		}
	}
	public static class cb extends JButton{
		
		public cb(String str){
			super(str);
		}
		public cb(ImageIcon img){
			super(img);
		}
		
		public cb font(Font font) {
			return (cb) fonts(this, font);
		}
		public cb fontColor(Color color) {
			return (cb) fontColors(this, color);
		}
		public cb BackColor(Color color) {
			this.setBackground(color);
			return this;
		}
		public cb setBorders(Border border) {
			this.setBorder(border);
			return this;
		}
		public cb size(int w, int h) {
			this.setPreferredSize(new Dimension(w, h));
			return this;
		}
		public cb setting() {
			this.setOpaque(true);
			this.setContentAreaFilled(false);
			this.setFocusable(false);
			return this;
		}
	}
	
	private static Component fonts(Component com,Font font) {
		com.setFont(font);
		return com;
	}
	private static Component fontColors(Component com, Color color) {
		com.setForeground(color);
		return com;
	}
	
}
