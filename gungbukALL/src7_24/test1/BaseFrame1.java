package test1;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public abstract class BaseFrame1 extends JFrame{
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
		des();
		act();
		setVisible(true);
	}

	protected abstract void des();
	protected abstract void act();
	
}
