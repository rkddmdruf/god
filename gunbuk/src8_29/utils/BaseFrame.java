package utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;

public abstract class BaseFrame extends JFrame{
	
	public void setFrame(String title, int w, int h, Runnable run){
		setIconImage(new ImageIcon("src/logo.png").getImage());
		getContentPane().setBackground(Color.white);
		setTitle(title);
		setBounds((1920/2) - (w/2),(1080/2) - (h/2),w,h);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				run.run();
			}
		});
		desing();
		action();
		setVisible(true);
		revalidate();
		repaint();
	}
	
	public void rePainted() {
		revalidate();
		repaint();
	}
	protected abstract void desing();
	protected abstract void action();
}
