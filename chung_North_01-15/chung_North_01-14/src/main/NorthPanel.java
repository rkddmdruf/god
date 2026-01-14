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
	JLabel logo = new JLabel(getter.getImageIcon("datafiles/로고1.jpg", 150, 50));
	JButton login = new CustumButton("로그인");
	JButton movieSerch = new CustumButton("영화 검색");
	JFrame f;
	
	public NorthPanel(JFrame f) {
		this.f = f;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		if(User.getUno() != null) {
			login.setText("내정보");
		}
		
		Font font = new Font("맑은 고딕", 0, 14);
		JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
		panel.setBackground(Color.white);
		login.setFont(font);
		panel.add(login);
		movieSerch.setFont(font);
		panel.add(movieSerch);
		add(panel, BorderLayout.EAST);
		add(logo, BorderLayout.WEST);
		setAction();
	}
	
	private void setAction() {
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(User.getUno() == 0) { return; }
				JOptionPane.showMessageDialog(null, "로그아웃 되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				User.logOut();
				return;
				
			}
		});
		
		login.addActionListener(e->{
			if(User.getUno() == null) {
				new Login();
			}
			f.dispose();
		});
		
		movieSerch.addActionListener(e->{
			new MovieSerch(true);
			f.dispose();
		});
	}
}
