package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.border.*;

public class sp {
	
	public static Row user = Query.selectText("select * from user where uno = 1").get(0);
	public static String n = "North";
	public static String s = "South";
	public static String c = "Center";
	public static String w = "West";
	public static String e = "East";
	public static DecimalFormat form = new DecimalFormat("###,###");
	public static Color color = new Color(250, 125, 0);
	public static Color gray = new Color(238,238,238);
	
	public static ImageIcon getImg(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	public static Font font(int font, int size) { return new Font("맑은 고딕", font, size); }
	public static Border line = BorderFactory.createLineBorder(Color.black);
	public static Border line(Color c) { return BorderFactory.createLineBorder(c); }
	public static Border em(int top, int left, int bottom, int right) { return BorderFactory.createEmptyBorder(top, left, bottom, right);}
	public static Border com(Border out, Border in) { return BorderFactory.createCompoundBorder(out, in); }
	
	public static void errMes  (String string) { JOptionPane.showMessageDialog(null, string, "경고", JOptionPane.ERROR_MESSAGE); }
	public static void InforMes(String string) { JOptionPane.showMessageDialog(null, string, "정보", JOptionPane.INFORMATION_MESSAGE); }
	
	public static class cp extends JPanel{
		private cp() {};
		public cp(LayoutManager lay, Border b, Color color) {
			setLayout(lay);
			setBorder(b);
			setBackground(color == null ? Color.white : color);
		}
		public cp size(int w, int h) {
			this.setPreferredSize(new Dimension(w, h));
			return this;
		}
	}
	
	public static class cb extends JButton{
		private cb() {};
		public cb(String string) {
			super(string);
		}
		public cb(ImageIcon img) {
			super(img);
		}
		public cb font(Font font) {
			this.setFont(font);
			return this;
		}
		public cb fontColor(Color color) {
			this.setForeground(color);
			return this;
		}
		public cb backColor(Color color) {
			this.setBackground(color);
			return this;
		}
		public cb size(int w, int h) {
			this.setPreferredSize(new Dimension(w, h));
			return this;
		}
		public cb setHo(int ha) {
			this.setHorizontalAlignment(ha);
			return this;
		}
		public cb setBorders(Border border) {
			this.setBorder(border);
			return this;
		}
	}
	
	public static class cl extends JLabel{
		private cl() {};
		public cl(String string) {
			super(string);
			setBackground(Color.white);
		}
		public cl(ImageIcon img) {
			super(img);
			setBackground(Color.white);
		}
		public cl font(Font font) {
			this.setFont(font);
			return this;
		}
		public cl fontColor(Color color) {
			this.setForeground(color);
			return this;
		}
		public cl backColor(Color color) {
			this.setBackground(color);
			return this;
		}
		public cl size(int w, int h) {
			this.setPreferredSize(new Dimension(w, h));
			return this;
		}
		public cl setHo(int ha) {
			this.setHorizontalAlignment(ha);
			return this;
		}
		public cl setBorders(Border border) {
			this.setBorder(border);
			return this;
		}
	}
	public static class ca extends JTextArea{
		public ca(String string) {
			this.setText(string);
		}
		public ca setting() {
			setLineWrap(true);
			setFocusable(false);
			setCursor(getCursor().getDefaultCursor());
			return this;
		}
		public ca font(Font font) {
			this.setFont(font);
			return this;
		}
	}
}
