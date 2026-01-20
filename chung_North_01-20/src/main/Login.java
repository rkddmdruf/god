package main;

import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;

public class Login extends JFrame{
	Font font = new Font("맑은 고딕", 1, 26);
	JTextField id = new JTextField();
	JTextField pw = new JPasswordField();
	JButton login = new CustumButton("로그인");
	
	public Login() {
		
		add(new JLabel("로그인", JLabel.LEFT) {{
			setBorder(createEmptyBorder(10, 5, 0, 0));
			setFont(font);
		}}, BorderLayout.NORTH);
		JPanel ipPanel = new JPanel(new BorderLayout(5,5));
		ipPanel.setBackground(Color.white);
		ipPanel.setBorder(createCompoundBorder(createMatteBorder(1, 0, 0, 0, Color.black), createEmptyBorder(5, 5, 5, 5)));
		
		JPanel gridPanel = new JPanel(new GridLayout(2, 1, 3, 3));
		gridPanel.setBackground(Color.white);
		ipPanel.add(gridPanel);
		
		setPanel(gridPanel, id, "아이디");
		setPanel(gridPanel, pw, "비밀번호");
		
		ipPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			setBorder(createEmptyBorder(15,0,15,0));
			add(login);
		}}, BorderLayout.SOUTH);
		
		setAction();
		add(ipPanel);
		setFrame.setframe(this, "로그인", 400, 225);
	}
	
	private void setAction() {
		login.addActionListener(e->{
			String i = id.getText();
			String p = pw.getText();
			if(i.isEmpty() || p.isEmpty()) {
				getter.mg("입력하지 않은 항목이 있습니다", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(i.equals("admin") && p.equals("1234")) {
				getter.mg("관리자님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
				User.admin = true;
				return;
			}
			Data user = Connections.select("select * from user where u_id = ? and u_pw = ?", i, p).get(0);
			if(user == null || user.isEmpty()) {
				getter.mg("존재하는 회원이 없습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getter.mg(user.get(1) + "회원님 환영합니다", JOptionPane.INFORMATION_MESSAGE);
			User.setUser(user);
			new Main();
			dispose();
			return;
		});
	}
	private void setPanel(JPanel panel, JTextField tf,String string) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		
		JLabel l = new JLabel(string);
		l.setFont(font.deriveFont(18f));
		l.setPreferredSize(new Dimension(100, 25));
		
		p.add(l, BorderLayout.WEST);
		p.add(tf);
		
		panel.add(p);
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
