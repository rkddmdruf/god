package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class Login extends JFrame{
	Font font = new Font("맑은 고딕", 1, 18);
	JTextField id = new JTextField();
	JTextField pw = new JPasswordField();
	
	JButton login = new CustumButton("로그인");
	Login(){
		JLabel l = new JLabel("로그인", JLabel.LEFT);
		l.setBorder(createEmptyBorder(5,3,1,1));
		l.setFont(font.deriveFont(30f));
		add(l, BorderLayout.NORTH);

		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.white);
		mainPanel.setBorder(createCompoundBorder(createMatteBorder(1, 0, 0, 0, Color.black), createEmptyBorder(4, 4, 0, 4)));
		
		JPanel ipPanel = new JPanel(new GridLayout(2, 1, 3, 3));
		ipPanel.setBackground(Color.white);
		addTextField(ipPanel, id, "아이디");
		addTextField(ipPanel, pw, "비밀번호");
		
		mainPanel.add(ipPanel);
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBorder(createEmptyBorder(30, 0, 20, 0));
			setBackground(Color.white);
			add(login);
		}}, BorderLayout.SOUTH);
		add(mainPanel);
		
		login.addActionListener(e->{
			String i = id.getText();
			String p = pw.getText();
			if(i.isEmpty() || p.isEmpty()) {
				getter.mg("입력하지 않은항목이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(i.equals("admin") && p.equals("1234")) {
				getter.mg("관리자님 환영합니다", JOptionPane.INFORMATION_MESSAGE);
				User.admin = true;
				return;
			}
			List<Data> user = Connections.select("select * from user where u_id = ? and u_pw = ?", i, p);
			if(user == null || user.isEmpty()) {
				getter.mg("존재하는 회원이 없습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			getter.mg(user.get(0).get(0).toString() + "회원님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
			new Main();
			User.setUser(user.get(0));
		});
		
		SetFrame.setFrame(this, "로그인", 400, 200);
	}
	
	private void addTextField(JPanel panel, JTextField tf, String string) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		
		JLabel l = new JLabel(string);
		l.setPreferredSize(new Dimension(100, 25));
		l.setFont(font);
		
		p.add(l, BorderLayout.WEST);
		p.add(tf);
		
		panel.add(p);
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
