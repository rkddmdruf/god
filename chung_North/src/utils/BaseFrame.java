package utils;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import main.Login;
import main.MovieSerch;
import utils.sp.cb;
import utils.sp.cl;

public abstract class BaseFrame extends JFrame{
	public JButton loginAll = new cb(sp.user.isEmpty() ? "로그인" : "내 정보").BackColor(sp.color).fontColor(Color.white).size(90, 50);
	public JButton movieSerch = new cb("영화 검색").BackColor(sp.color).fontColor(Color.white).size(90,  50);
	public JLabel logo = new cl(sp.getImg("datafiles/로고1.jpg", 150, 50)).size(150, 50);
	
	public void setFrame(String t, int w, int h, Runnable run) {
		setTitle(t);
		getContentPane().setBackground(Color.white);
		this.setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		setBounds((1920/2) - (w/2), (1080/2) - (h/2), w, h);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				run.run();
			}
		});
		desing();
		action();
		RePaint();
		setVisible(true);
		ActionListener action1 = e->{
			new Login();
			dispose();
		};
		ActionListener action2 = e->{
			new MovieSerch();
			dispose();
		};
		loginAll.removeActionListener(action1);
		if(loginAll.getText().equals("로그인")) 
		loginAll.addActionListener(action1);
		movieSerch.removeActionListener(action2);
		movieSerch.addActionListener(action2);
		
	}
	
	protected abstract void desing();
	protected abstract void action();
	public void RePaint() {
		revalidate();
		repaint();
	}
}
