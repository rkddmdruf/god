package utils;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

public abstract class BaseFrame extends JFrame{
	
	public void setFrame(String title, int w, int h, Runnable run){
		setIconImage(new ImageIcon("src/logo.png").getImage());
		getContentPane().setBackground(Color.white);
		setTitle(title);
		setSize(w, h);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				run.run();
				dispose();
			}
		});
		setVisible(true);
		desing();
		action();
		repaint();revalidate();
	}
	public void rePainted() {
		repaint();revalidate();
	}
	protected abstract void desing();
	protected abstract void action();
}
