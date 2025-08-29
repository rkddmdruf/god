package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.LayoutManager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class sp {


	public static List<Row> user = new ArrayList<Row>() {{
		Row row = new Row();
		row.add(1);
		row.add("김지민");
		row.add("user01");
		row.add("user01!");
		row.add("271");
		row.add("264");
		this.add(row);
	}};
	public static boolean isAdmin = false;
	public static int Serchs = -1;
	public static final String n = "North";
	public static final String c = "Center";
	public static final String e = "East";
	public static final String w = "West";
	public static final String s = "South";
	
	
	public static DecimalFormat DF = new DecimalFormat("###,###");
	
	
	public static void logout() {
		user.clear();
		isAdmin = false;
	}
	
	 public static Font fontM(int i,int x ) {
		 return new Font("맑은 고딕",i,x);
	 }
	 //butC.add(new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/img/"+actionList.get(global-1).getInt(0)+".jpg"))
	//.getImage().getScaledInstance(140, 120, Image.SCALE_SMOOTH))));
	 public static Border line = BorderFactory.createLineBorder(Color.black);
	
	 public static ImageIcon getImg(String t,int x,int y) {
		 return new ImageIcon(new ImageIcon(t).getImage().getScaledInstance(x, y, 4));
	 }
	 public static void ErrMes(String str) {
		 JOptionPane.showMessageDialog(null, str, "경고", JOptionPane.ERROR_MESSAGE);
	 }
	 public static JPanel Cjp(LayoutManager lay) {
		return new JPanel(lay) {{setBackground(Color.white);}};
	 }
	 public static int YesNoMes(String str) {
		return JOptionPane.showConfirmDialog(null, str, "확인 질문", JOptionPane.YES_NO_OPTION);
	 }
	 public static void InforMes(String str) {
		 JOptionPane.showMessageDialog(null, str, "정보", JOptionPane.INFORMATION_MESSAGE);
	 }
	 public static void setBorderIE(JComponent p,int a, int b, int c, int d) {
		p.setBorder(BorderFactory.createEmptyBorder(a,b,c,d));
	 }
	 public static void setBorderLINE(JComponent p) {
			p.setBorder(BorderFactory.createLineBorder(Color.black));
	 }
	 public static JButton[] setButton(String...val) {
		JButton[] but = new JButton[val.length];
		for(int i = 0; i < val.length; i++) {but[i] = new JButton(val[i]);}
		return but;
	 }
	 public static class Cp extends JPanel {
		 public Cp(){
			 setBackground(Color.white);
		 }
	 }
}
