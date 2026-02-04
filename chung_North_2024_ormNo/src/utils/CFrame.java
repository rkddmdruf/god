package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;


public class CFrame extends JFrame{
	
	public CFrame() {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			
			if (value instanceof FontUIResource) {
				UIManager.put(key, new FontUIResource("맑은 고딕", 1, 12));
			}
			if (key.toString().contains(".foreground")) {
				UIManager.put(key, Color.black);
			}
		}
	}
	
	public void setFrame(String s, int w, int h) {
		setTitle(s);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(new Dimension(w + 19, h + 39));
		setIconImage(new ImageIcon("datafiles/게임아이콘.png").getImage());
		setLocationRelativeTo(null);
		getContentPane().setBackground(Color.white);
		setVisible(true);
	}
}
