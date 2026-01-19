package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class Login extends JFrame{
	Font font = new Font("맑은 고딕", 1,26);
	JTextField id = new JTextField();
	JTextField pw= new JPasswordField();
	
	JButton login = new CustumButton("로그인");
	
	public Login() {
		add(new JLabel("로그인", JLabel.LEFT) {{
			setBorder(createEmptyBorder(10,5,0,0));
			setFont(font);
		}}, BorderLayout.NORTH);
		
		JPanel ipPanel = new JPanel(new GridLayout(2, 1, 3, 3));
		ipPanel.setBackground(Color.white);
		ipPanel.setBorder(createCompoundBorder(createMatteBorder(1, 0, 0, 0, Color.black), createEmptyBorder(5, 5, 5, 5)));
		
		setipPanel(ipPanel, id, "아이디");
		setipPanel(ipPanel, pw, "비밀번호");
		
		add(ipPanel);
		
		add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(login);
			setBorder(createEmptyBorder(15, 0, 15, 5));
		}}, BorderLayout.SOUTH);
		setAction();
		setFrame.setframe(this, "로그인", 400, 225);
	}
	
	private void setipPanel(JPanel p, JTextField tf, String string) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		
		JLabel l = new JLabel(string);
		l.setFont(font.deriveFont(18f));
		l.setPreferredSize(new Dimension(100, 25));
		
		panel.add(l, BorderLayout.WEST);
		panel.add(tf);
		
		p.add(panel);
	}
	
	private void setAction() {
		login.addActionListener(e->{
			String id = this.id.getText();
			String pw = this.pw.getText();
			if(id.isEmpty() || pw.isEmpty()) {
				getter.mg("입력하지 않은 항목이 있습니다", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(id.equals("admin") && pw.equals("1234")) {
				getter.mg("관리자님 환영합니다", JOptionPane.INFORMATION_MESSAGE);
				User.admin = true;
				return;
			}
			Data user = Connections.select("select * from user where u_id = ? and u_pw = ?", id, pw).get(0);
			if(user.isEmpty() || user == null) {
				getter.mg("존재하는 회원이 없습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getter.mg(user.get(1) + "회원님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
			User.setUser(user);
			new Main();
			return;
		});
	}
	
	public static void main(String[] args) {
		new Login();
		
	}
}
