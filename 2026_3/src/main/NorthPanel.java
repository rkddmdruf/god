package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utils.CustumButton;
import utils.User;
import utils.getter;

public class NorthPanel extends JPanel{
	
	JButton login = new CustumButton(User.getUser() == null ? "로그인" : "로그아웃");
	JButton movieSerch = new CustumButton("영화 검색");
	JFrame f;
	public NorthPanel(JFrame f) {
		this.f = f;
		setBackground(Color.white);
		setLayout(new BorderLayout());
		add(new JLabel(getter.getImageIcon("datafiles/로고1.jpg", 175, 50)), BorderLayout.WEST);
		
		JPanel p = new JPanel(new GridLayout(0, 2, 10, 10));
		p.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
		p.setBackground(Color.white);
		p.add(login);
		p.add(movieSerch);
		loginAction();
		movieSerchAction();
		add(p, BorderLayout.EAST);
	}
	
	void movieSerchAction() {
		movieSerch.addActionListener(e->{
			new MovieSerch();
			f.dispose();
		});
	}

	void loginAction() {
		login.addActionListener(e->{
			if(login.getText().equals("로그인")) {
				new Login();
				f.dispose();
			}else {
				getter.mg("로그아웃 되었습니다.", JOptionPane.INFORMATION_MESSAGE);
				User.setUser(null);
				login.setText("로그인");
			}
		});
	}
}
