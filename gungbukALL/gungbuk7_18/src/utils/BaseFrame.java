package utils;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public abstract class BaseFrame extends JFrame implements Base{
	public void setFrame(String title, int W, int H, Runnable run) {
		setTitle(title);
		setSize(W, H);
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
	public Font setBoldFont(int size) {
		return new Font("맑은 고딕", Font.BOLD, size);
	}
}
