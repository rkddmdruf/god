package main;

import javax.swing.ImageIcon;

public class getter {
	public static ImageIcon getImageIcon(String file, int w, int h) {
		return new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(w, h, 4));
	}
}
