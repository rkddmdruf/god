package utils;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;

public class ButtonMake extends JButton{

	public ButtonMake(String str, int w, int h, Font font){
		this.setText(str);
		this.setPreferredSize(new Dimension(w, h));
		this.setFont(font);
	}
}
