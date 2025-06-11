package utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public abstract class BaseFrame extends JFrame implements Base{
	public void setFrame(String title, int W, int H, Runnable run){
		setTitle(title);
		setSize(W, H);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				run.run();
			}
		});
		desgin();
		action();
		setVisible(true);
	}
}
