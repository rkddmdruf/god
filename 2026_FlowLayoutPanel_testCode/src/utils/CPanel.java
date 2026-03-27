package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CPanel extends JPanel{
	int w;
	public CPanel(int w) {
		this.w = w;
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBackground(Color.white);
	}
	
	public int getRemain() {
		return w;
	}
	
	public void add(int w, int h, Component p) {
		p.setPreferredSize(new Dimension(w, h));
		add(p);
	}
	
	public void gap(int w, int h) {
		JLabel l = new JLabel("");
		l.setPreferredSize(new Dimension(w, h));
		add(l);
	}
	
	public void nextLine(int gap) {
		JLabel l = new JLabel("");
		l.setPreferredSize(new Dimension(w, gap));
		add(l);
	}
}
