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


public class NorthPanel extends JPanel{
	Font font = new Font("맑은 고딕", 0, 14);
	JLabel logo = new JLabel(getter.getImage("datafiles/로고1.jpg", 150, 50));
	JButton login = new CustumButton("로그인") {{
		setFont(font);
	}};
	JButton movieSerchBut = new CustumButton("영화 검색") {{
		setFont(font);
	}};
	
	public NorthPanel(JFrame f) {
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		
		JPanel p = new JPanel(new GridLayout(0, 2, 3, 3));
		p.setBackground(Color.white);
		
		p.add(login);
		p.add(movieSerchBut);
		
		this.add(logo, BorderLayout.WEST);
		this.add(p, BorderLayout.EAST);
		
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(User.getUser() == null) return;
				User.setUser(null);
				getter.mg("로그아웃 되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		login.addActionListener(e->{
			if(User.getUser() != null) return;
			new Login();
			f.dispose();
		});
		
		movieSerchBut.addActionListener(e->{
			new MovieSerch();
			f.dispose();
		});
	}
}
