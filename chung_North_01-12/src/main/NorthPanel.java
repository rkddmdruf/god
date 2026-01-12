package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class NorthPanel extends JPanel{
	JLabel logo = new JLabel(getImage("datafiles/로고1.jpg", 150, 50));
	Font font = new Font("맑은 고딕", 0, 14);
	JButton login = new CustumButton("로그인");
	JButton movieSerch = new CustumButton("영화 검색");
	int u_no = 0;
	JFrame f;
	public NorthPanel(JFrame f,int u_no) {
		this.f = f;
		this.u_no = u_no;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		add(logo, BorderLayout.WEST);
		JPanel eastPanel = new JPanel(new GridLayout(0, 2,5,5));
		eastPanel.setBackground(Color.white);
		login.setFont(font);
		movieSerch.setFont(font);
		eastPanel.add(login);
		eastPanel.add(movieSerch);		
		add(eastPanel, BorderLayout.EAST);
		if(u_no !=0) login.setText("내정보");
		setAction();
	}
	
	private void setAction() {
		logo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(u_no != 0) {
					JOptionPane.showMessageDialog(null, "로그아웃 되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
					u_no = 0;
					login.setText("로그인");
				}
			}
		});
		login.addActionListener(e->{
			if(login.getText().equals("로그인")) {
				new Login();
				f.dispose();
				return;
			}
			System.out.println("내정보");
		});
		movieSerch.addActionListener(e->{
			new MovieSerch(u_no);
			f.dispose();
		});
	}
	private ImageIcon getImage(String file, int w, int h) {
		return new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(w, h, 4) );
		
	}
}
