package utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public abstract class BaseFrame extends JFrame{

	public void setFrame(String title, int w, int h, Runnable run) {
		setTitle(title);
		setIconImage(new ImageIcon("datafiles/icon/Icon.png").getImage());
		setBounds(960 - (w/2), 540 - (h/2), w, h);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				run.run();
				System.out.println("closing");
				dispose();
			}
		});
		desing();
		action();
		setVisible(true);
	}
	protected abstract void desing();
	protected abstract void action();
	public void rp() {
		revalidate();
		repaint();
	}
}
