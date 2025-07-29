package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;

public abstract class BaseFrame extends JFrame{
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
		UIManager.put("OptionPane.background", new ColorUIResource(Color.white));
		UIManager.put("Panel.background", new ColorUIResource(Color.white));
		desgin();
		action();
		setVisible(true);
	}
	protected abstract void desgin();
	protected abstract void action();
}
