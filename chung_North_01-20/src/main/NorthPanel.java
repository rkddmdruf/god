package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NorthPanel extends JPanel{
	Font font = new Font("맑은 고딕", 0, 15);
	JLabel logo = new JLabel(getter.getImageIcon("datafiles/로고1.jpg", 150, 50));
	JButton login = new CustumButton(User.getUser() != null ? "내정보" : "로그인") {{
		setFont(font);
	}};
	JButton movieSerchBut = new CustumButton("영화 검색") {{
		setFont(font);
	}};
	public NorthPanel(JFrame f) {
		this.setBackground(Color.white);
		this.setLayout(new BorderLayout());
		add(logo, BorderLayout.WEST);
		JPanel p = new JPanel(new GridLayout(0, 2, 3, 3));
		p.add(login);
		p.add(movieSerchBut);
		
		add(p, BorderLayout.EAST);
	}
}
