package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class CFrame extends JFrame{

	protected JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
	}};
	public void setFrameCg(String s, int w, int h, Runnable r) {
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				r.run();
			}
		});
		setFrame(s, w, h);
	}
	
	public void setFrameCd(String s, int w, int h, Runnable r) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				r.run();
			}
		});
		setFrame(s, w, h);
	}
	
	private void setFrame(String s, int w, int h) {
		setTitle(s); 
		setSize(w + 16, h + 39);
		setLocationRelativeTo(null);
		add(borderPanel);
		setIconImage(new ImageIcon("datafiles/logo.png").getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
