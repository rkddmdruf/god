package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CFrame extends JFrame{
	
	protected JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
	}};
	protected void setFrame(String s, int w, int h) {
		setTitle(s);
		setSize(w + 16, h + 39);
		add(borderPanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(Color.white);
		setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	protected void addClosing(Runnable r) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				r.run();
			}
		});
	}
	
	protected void addClosed(Runnable r) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				r.run();
			}
		});
	}
}
