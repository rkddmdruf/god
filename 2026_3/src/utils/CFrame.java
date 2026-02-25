package utils;

import java.awt.Color;import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class CFrame extends JFrame{
	
	public CFrame() {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			
			if (value instanceof FontUIResource) {
				UIManager.put(key, new FontUIResource("맑은 고딕", 0, 12));
			}
			if (key.toString().contains(".foreground")) {
				UIManager.put(key, Color.black);
			}
		}
	}
	
	public void setFrame(String s, int w, int h) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(s);
		getContentPane().setBackground(Color.white);
		setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		setSize(w + 16, h + 39);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
