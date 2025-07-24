package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

public class sp {

	
	 public static String n = "North";
	 public static String c = "Center";
	 public static String e = "East";
	 public static String w = "West";
	 public static String s = "South";
	
	 public static Font fontM(int i,int x ) {
		 return new Font("맑은 고딕",i,x);
	 }
	 //butC.add(new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/img/"+actionList.get(global-1).getInt(0)+".jpg"))
	//.getImage().getScaledInstance(140, 120, Image.SCALE_SMOOTH))));
	 public static Border line = BorderFactory.createLineBorder(Color.black);
	
	 public static ImageIcon getImg(String t,int x,int y) {
		 return new ImageIcon(new ImageIcon(t).getImage().getScaledInstance(x, y, 4));
	 }
	 
}
