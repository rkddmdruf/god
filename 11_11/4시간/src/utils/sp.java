package utils;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.border.*;

public class sp {
	public static Row user = Query.selectText("select * from user where uno = 1").get(0);
	public static String s = BorderLayout.SOUTH;
	public static String n = BorderLayout.NORTH;
	public static String w = BorderLayout.WEST;
	public static String e = BorderLayout.EAST;
	public static String c = BorderLayout.CENTER;
	public static int serch = 0;
	
	public static Border line = BorderFactory.createLineBorder(Color.black);
	public static Color color = new Color(250, 125, 0);
	public static Color gray = new Color(238,238,238);
	public static DecimalFormat df = new DecimalFormat("###,###");
	
	public static Border em(int top, int left, int bottom, int right) {
		return BorderFactory.createEmptyBorder(top, left, bottom, right);
	}
	public static Border line(Color color) {
		return BorderFactory.createLineBorder(color);
	}
	/**
	 * 
	 * @param b1 = outBorder
	 * @param b2 = inBorder
	 * @return b1 + b2
	 */
	public static Border com(Border outBorder, Border inBorder) {
		return BorderFactory.createCompoundBorder(outBorder, inBorder);
	}
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
			super(img);
		}
		public cl(String str){
			super(str);
		}
		public cl font(Font font) {
			this.setFont(font);
			return this;
		}
		public cl fontColor(Color color) {
			this.setForeground(color);
			return this;
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

	public static class cb extends JButton{
		
		public cb(String str){
			super(str);
		}
		public cb(ImageIcon img){
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
		public cb BackColor(Color color) {
			this.setBackground(color);
			return this;
		}
		public cb setBorders(Border border) {
			this.setBorder(border);
			return this;
		}
		public cb size(int w, int h) {
			setPreferredSize(new Dimension(w, h));
			return this;
		}
		public cb setHo(int a) {
			this.setHorizontalAlignment(a);
			return this;
		}
		public cb setting() {
			
			this.setContentAreaFilled(false);
			this.setOpaque(true);
			this.setFocusable(false);
			this.setBorderPainted(false);
			return this;
		}
	}
	public static class cta extends JTextArea{
		public cta(String string) {
			super(string);
		}
		public cta font(Font font) {
			this.setFont(font);
			return this;
		}
		public cta fontColor(Color color) {
			this.setForeground(color);
			return this;
		}
		public cta size(int w, int h) {
			this.setPreferredSize(new Dimension(w, h));
			return this;
		}
		public cta setting() {
			this.setEditable(false);
			this.setFocusable(false);
			this.setLineWrap(true);
			return this;
		}
	}
}
