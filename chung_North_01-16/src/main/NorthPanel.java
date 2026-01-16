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
	Font font = new Font("맑은 고딕", 0, 15);
	JButton login = new CustumButton(User.getData() == null ? "로그인" : "내정보") {{
		setFont(font);
	}};
	JButton movieSerch = new CustumButton("영화 검색") {{
		setFont(font);
	}};
	JLabel logo = new JLabel(getter.getImageIcon("datafiles/로고1.jpg", 150, 50));
	JFrame f;
	
	NorthPanel(JFrame f){
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		
		JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
		p.setBackground(Color.white);
		p.add(login);
		p.add(movieSerch);
		
		add(logo, BorderLayout.WEST);
		add(p, BorderLayout.EAST);
		setAction();
	}
	
	private void setAction() {
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(User.getData() == null) return;
				JOptionPane.showMessageDialog(null, "로그아웃 되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				User.setUser(null);
				login.setText("로그인");
			}
		});
		
		login.addActionListener(e->{
			if(login.getText().equals("로그인")) {
				new Login();
			}else {
				//내정보
				System.out.println("내정보");
			}
			f.dispose();
		});
		
		movieSerch.addActionListener(e->{
			//new MovieSerch();
			f.dispose();
		});
	}
}
