package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class NorthPanel extends JPanel{
	Font font = new Font("맑은 고딕", 0, 14);
	JLabel logo = new JLabel(getter.getImageIcon("datafiles/로고1.jpg", 150, 50));
	JButton login = new CustumButton((User.getUser() == null || User.getUser().isEmpty()) ? "로그인" : "내정보"){{
		setFont(font);
	}};
	JButton movieSerchBut = new CustumButton("영화 검색"){{
		setFont(font);
	}};
	public NorthPanel(JFrame f) {
		this.setLayout(new BorderLayout());
		setBackground(Color.white);
		
		add(logo, BorderLayout.WEST);
		add(new JPanel(new GridLayout(0, 2, 2, 2)) {{
			setBackground(Color.white);
			add(login);
			add(movieSerchBut);
		}}, BorderLayout.EAST);
		
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(User.getUser() == null) return;
				getter.mg("로그아웃 되었습니다.", JOptionPane.INFORMATION_MESSAGE);
				login.setText("로그인");
				User.setUser(null);
			}
		});
	}
}
