package utils;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public abstract class BaseFrame extends JFrame implements Base{
	public void setFrame(String title, int W, int H, Runnable run) {
		setTitle(title);
		setSize(W, H);
		setBounds((1920/2) - (W/2),(1080/2) - (H/2),W,H);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				run.run();
				dispose();
			}
		});
		desgin();
		action();
		setVisible(true);
	}


}
