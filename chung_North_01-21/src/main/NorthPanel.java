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
	Font font = new Font("맑은 고딕", 0, 16);
	
	JLabel logo = new JLabel(getter.getImageIcon("datafiles/로고1.jpg", 150, 50));
	
	JButton login = new CustumButton(User.getUser() == null ? "로그인" : "내정보") {{
		setFont(font);
	}};
	JButton movieSerchBut = new CustumButton("영화 검색") {{
		setFont(font);
	}};
	
	public NorthPanel(JFrame f) {
		this.setBackground(Color.white);
		this.setLayout(new BorderLayout());
		
		add(logo, BorderLayout.WEST);
		add(new JPanel(new GridLayout(0,2,3,3)) {{
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
				return;
			}
		});
		login.addActionListener(e->{
			if(login.getText() == "로그인") {
				//new Login();
				f.dispose();
				return;
			}
		});
	}
}
