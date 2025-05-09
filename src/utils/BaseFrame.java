package utils;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public abstract class BaseFrame extends JFrame implements Base{
	public void setFrame(String title, int w, int h, Runnable runnable) {
		setTitle(title);
		setSize(w, h);
		setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				runnable.run();
			}
		});
		
		design();
		action();
		
		setVisible(true);
	}
}
