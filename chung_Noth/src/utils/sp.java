package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.*;
import javax.swing.border.*;

import utils.sp.cb;
import utils.sp.cl;

public class sp {
	
	public static Row user = new Row();
	public static String n = "North";
	public static String s = "South";
	public static String c = "Center";
	public static String w = "West";
	public static String e = "East";
	public static Color color = new Color(0, 0, 180);
	public static Border line = BorderFactory.createLineBorder(Color.black);
	public static Border line(Color c) {
		return BorderFactory.createLineBorder(Color.black);
	}
	public static Border em(int t, int l, int b, int r) {
		return BorderFactory.createEmptyBorder(t,l,b,r);
	}
	public static Border com(Border out, Border in) {
		return BorderFactory.createCompoundBorder(out, in);
	}
	
	public static void err(String string) {
		JOptionPane.showMessageDialog(null, string, "경고", JOptionPane.ERROR_MESSAGE);
	}
	public static void Infor(String string) {
		JOptionPane.showMessageDialog(null, string, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static Font font(int font, int size) {
		return new Font("맑은 고딕", font, size);
	}
	public static ImageIcon getImg(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	public static class cp extends JPanel{
		private cp() {};
		public cp(LayoutManager l, Border b, Color c) {
			this.setLayout(l);
			this.setBorder(b);
			this.setBackground(c != null ? c : Color.white);
		}
		public cp size(int w, int h) {
			this.setPreferredSize(new Dimension(w, h));
			return this;
		}
	}
	public static class cb extends JButton{
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
			this.setOpaque(true);
			this.setContentAreaFilled(false);
			this.setFocusable(false);
			return this;
		}
	}
	public static class cl extends JLabel{
		public cl(Object string) {
			super(string.toString());
		}
		public cl(ImageIcon img) {
			super(img);
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
}
