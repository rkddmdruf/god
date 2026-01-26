package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class Login extends JFrame{
	Font font = new Font("맑은 고딕", 1, 30);
	
	JTextField id = new JTextField();
	JTextField pw = new JPasswordField();
	
	JFrame f = this;
	JButton login = new CustumButton("로그인");
	Login(){
		JLabel l = new JLabel("로그인");
		l.setBorder(createEmptyBorder(7,10,2,0));
		l.setFont(font);
		
		JPanel mainPanel = new JPanel(new BorderLayout(5,5));
		mainPanel.setBackground(Color.white);
		mainPanel.setBorder(createCompoundBorder(createMatteBorder(1, 0, 0, 0, Color.black), createEmptyBorder(5,5,5,5)));
		
		JPanel ipPanel = new JPanel(new GridLayout(2, 1, 4, 4));
		ipPanel.setBackground(Color.white);
		addIpPanel(ipPanel, id, "아이디");
		addIpPanel(ipPanel, pw, "비밀번호");
		
		mainPanel.add(ipPanel);
		
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		butPanel.setBorder(createEmptyBorder(15, 0, 10, 0));
		butPanel.setBackground(Color.white);
		butPanel.add(login);
		
		mainPanel.add(butPanel, BorderLayout.SOUTH);
		int i = 11;
		do {
			i++;
			System.out.println(i);
		} while (i < 10);
		add(mainPanel);
		add(l, BorderLayout.NORTH);
		setAction();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				getter.run_Dispose(f);
				getter.setRun(()->{
					new Login();
				});
			}
		});
		setFrame.setframe(this, "로그인", 400, 200);
	}
	
	private void setAction() {
		login.addActionListener(e->{
			String i = id.getText();
			String p = pw.getText();
			if(i.isEmpty() || p.isEmpty()) {
				getter.mg("입력하지 않은 항목이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(i.equals("admin") && p.equals("1234")) {
				getter.mg("관리자님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			List<Data> user = Connections.select("select * from user where u_id = ? and u_pw = ?", i, p);
			if(user.isEmpty()) {
				getter.mg("존재하는 회원이 없습니다.", JOptionPane.ERROR_MESSAGE);
				id.setText("");
				pw.setText("");
				return;
			}
			getter.mg(user.get(0).get(1) + "회원님 환영합니다", JOptionPane.INFORMATION_MESSAGE);
			User.setUser(user.get(0));
			getter.run_Dispose(this);
		});
	}
	
	private void addIpPanel(JPanel panel, JTextField tf, String s) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		
		JLabel l = new JLabel(s);
		l.setFont(font.deriveFont(20f));
		l.setPreferredSize(new Dimension(90, 25));
		
		p.add(l, BorderLayout.WEST);
		p.add(tf);
		
		panel.add(p);
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
