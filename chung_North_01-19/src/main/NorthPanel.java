package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class NorthPanel extends JPanel{

	Font font = new Font("맑은 고딕", 0, 14);
	JLabel logo = new JLabel(getter.getImageIcon("datafiles/로고1.jpg", 150, 50));
	JButton login = new CustumButton(User.getUser() == null ? "로그인" : "내정보") {{
		setFont(font);
	}};
	JButton movieSerchBut = new CustumButton("영화 검색") {{
		setFont(font);
	}};
	public NorthPanel(JFrame f) {
		setBackground(Color.white);
		this.setLayout(new BorderLayout());
		add(logo, BorderLayout.WEST);
		
		JPanel panel = new JPanel(new GridLayout(0, 2, 3, 3));
		panel.setBackground(Color.white);
		panel.add(login);
		panel.add(movieSerchBut);
		setAction(f);
		add(panel, BorderLayout.EAST);
	}
	
	private void setAction(JFrame f) {
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(User.getUser() != null) {
					getter.mg("로그아웃 되었습니다.", JOptionPane.INFORMATION_MESSAGE);
					User.setUser(null);
					login.setText("로그인");
				}
			}
		});
		
		login.addActionListener(e->{
			if(login.getText().equals("로그인")) {
				new Login();
			}
			f.dispose();
		});
		
		movieSerchBut.addActionListener(e->{
			new MovieSerch();
			f.dispose();
		});
	}
}
