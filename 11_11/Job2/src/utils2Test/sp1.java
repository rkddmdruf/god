package utils2Test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import org.w3c.dom.Text;

public interface sp1{

	public static String s = BorderLayout.SOUTH;
	public static String n = BorderLayout.NORTH;
	public static String w = BorderLayout.WEST;
	public static String e = BorderLayout.EAST;
	public static String c = BorderLayout.CENTER;
	
	public static Border line = BorderFactory.createLineBorder(Color.black);
	public static Color Ocolor = new Color(250, 125, 0);
	public static DecimalFormat df = new DecimalFormat("###,###");
	
	default Border em(int top, int left, int bottom, int right) {
		return BorderFactory.createEmptyBorder(top, left, bottom, right);
	}
	default Font font(int font, int size) {
		return new Font("맑은 고딕", font, size);
	}
	default void ErrMes(String string) {
		JOptionPane.showMessageDialog(null, string, "에러", JOptionPane.ERROR_MESSAGE);
	}
	default void InforMes(String string) {
		JOptionPane.showMessageDialog(null, string, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	default ImageIcon getImg(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	default Border line(Color color) {
		return BorderFactory.createLineBorder(color);
	}
	
	default <T> T get(JComponent c, Z_Set...set) {
		for(Z_Set z_Sets : set) {
			z_Sets.set(c);
		}
		return (T) c;
	}
	default Z_Set Borders(Border border) {
		return c -> c.setBorder(border);
	}
	default Z_Set BackColor(Color color) {
		return c -> c.setBackground(color);
	}
	default Z_Set font(Font font) {
		return c -> c.setFont(font);
	}
	default Z_Set fontColor(Color color) {
		return c -> c.setForeground(color);
	}
	default Z_Set size(int w, int h) {
		return c -> c.setPreferredSize(new Dimension(w, h));
	}
}
