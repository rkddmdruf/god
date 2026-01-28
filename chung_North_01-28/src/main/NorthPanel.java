package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utils.CustumButton;
import utils.User;
import utils.getter;

public class NorthPanel extends JPanel{
	Font font = new Font("맑은 고딕", 0, 14);
	private JLabel logo = new JLabel(getter.getImage("datafiles/로고1.jpg", 150, 50));
	JButton login = new CustumButton(User.getUser() == null ? "로그인" : "내정보") {{
		setFont(font);
	}};
	JButton movieSerchBut = new CustumButton("영화 검색") {{
		setFont(font);
	}};
	
	JFrame f;
	public NorthPanel(JFrame f) {
		this.f = f;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		add(logo, BorderLayout.WEST);
		
		JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
		p.setBackground(Color.white);
		p.add(login);
		p.add(movieSerchBut);
		
		add(p, BorderLayout.EAST);
		
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(User.getUser() == null) return;
				getter.mg("로그아웃 되었습니다.", JOptionPane.INFORMATION_MESSAGE);
				User.setUser(null);
			}
		});
	};
	
	public void loginAction() {
		login.addActionListener(e->{
			if(User.getUser() != null) return;
			new Login();
			f.dispose();
		});
	}
	public void movieButAction() {
		movieSerchBut.addActionListener(e->{
			new MovieSerch();
			f.dispose();
		});
	}
}
