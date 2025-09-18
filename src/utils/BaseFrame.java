package utils;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public abstract class BaseFrame extends JFrame{
	public void setFrame(String title, int w, int h, Runnable run){
		setTitle(title);
		getContentPane().setBackground(Color.white);
		setIconImage(new ImageIcon("src/icon/icon.png").getImage());
		setBounds((1920/2) - (w/2), (1080/2) - (h/2), w, h);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				run.run();
			}
		});
		
		
		desing();
		action();
		RePaint();
		setVisible(true);
	}
	protected abstract void desing();
	protected abstract void action();
	public void RePaint() {
		revalidate();
		repaint();
	}
}
