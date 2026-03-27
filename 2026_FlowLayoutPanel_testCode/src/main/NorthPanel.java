package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.CButton;
import utils.User;
import utils.getter;

public class NorthPanel extends JPanel{

	JLabel img = new JLabel(getter.getImage("datafiles/로고1.jpg", 175, 50));
	JButton login = new CButton(User.getUser() == null ? "로그인" : "로그아웃");
	JButton movieSerch = new CButton("영화 검색");
	
	JFrame f;
	public NorthPanel(JFrame f) {
		this.f = f;
		setLayout(new BorderLayout());
		setBackground(Color.white);
		
		add(img, BorderLayout.WEST);
		JPanel p = new JPanel(new GridLayout(0, 2, 10, 10));
		p.setBackground(Color.white);
		
		setLoginAction();
		setMovieAction();
		p.add(login);
		p.add(movieSerch);
		add(p, BorderLayout.EAST);
	}
	
	protected void setLoginAction(){
		login.addActionListener(e -> {
			System.out.println("sdfdsf");
			if(login.getText().equals("로그인")) {
				//new Login();
				f.dispose();
			}else {
				User.setUser(null);
				getter.infor("로그아웃 되었습니다.");
				login.setText("로그인");
				return;
			}
		});
	}
	
	protected void setMovieAction() {
		movieSerch.addActionListener(e->{
			new MovieSerch();
			f.dispose();
		});
	}
}
