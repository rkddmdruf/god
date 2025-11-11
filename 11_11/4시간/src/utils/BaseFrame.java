package utils;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JFrame;

public abstract class BaseFrame extends JFrame{
	public  void setFrame(String title, int w, int h, Runnable run) {
		setTitle(title);
		getContentPane().setBackground(Color.white);
		setIconImage(sp.getImg("icon/icon.png", 100, 100).getImage());
		setBounds(960 - (w/2), 540 - (h/2), w, h);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				run.run();
				dispose();
			}
		});
		desing();
		action();
		setVisible(true);
		
		
	}
	
	protected abstract void desing();
	protected abstract void action();
	public void repaints() {
		revalidate();
		repaint();
	}
}
