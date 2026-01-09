package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class NorthPanel extends JPanel{
	private JLabel logo = new JLabel(new ImageIcon(new ImageIcon("datafiles/로고1.jpg").getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH)));
	private JButton login = new CustumButton("로그인");
	private JButton movieSerch = new CustumButton("영화 검색");
	JFrame f;
	int u_no = 0;
	NorthPanel(int u_no, JFrame f){
		this.u_no = u_no;
		this.f = f;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		this.add(logo, BorderLayout.WEST);
		JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
		login.setText(u_no == 0 ? "로그인" : "내정보");
		p.add(login);
		p.add(movieSerch);
		p.setBackground(Color.white);
		this.add(p, BorderLayout.EAST);
		setAction();
	}
	
	private void setAction() {
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(u_no == 0) return;
				JOptionPane.showMessageDialog(null, "로그아웃 되었습니다", "정보", JOptionPane.INFORMATION_MESSAGE);
				u_no = 0;
				login.setText("로그인");
			}
		});
		login.addActionListener(e->{
			if(u_no == 0) {
				new Login();
				f.dispose();
				return;
			}
			//new My...
			f.dispose();
		});
		movieSerch.addActionListener(e->{
			new MovieSerch(u_no);
			f.dispose();
		});
	}
}
