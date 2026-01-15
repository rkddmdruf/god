package main;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class Login extends JFrame{

	Font font = new Font("맑은 고딕", 1, 24);
	
	JTextField id = new JTextField();
	JTextField pw = new JPasswordField();
	JButton login = new CustumButton("로그인");
	JPanel idPassPanel = new JPanel(new GridLayout(2, 1, 5, 5));
	
	
	
	public Login() {
		JLabel label = new JLabel("로그인");
		label.setBorder(createCompoundBorder(createMatteBorder(0, 0, 1, 0, Color.black), createEmptyBorder(10, 5, 0, 5)));
		label.setFont(font);
		add(label, BorderLayout.NORTH);
		
		idPassPanel.setBorder(createEmptyBorder(5,5,0,5));
		idPassPanel.setBackground(Color.white);
		setidPassPanel(idPassPanel, "아이디", id);
		setidPassPanel(idPassPanel, "비밀번호", pw);
		add(idPassPanel);
		
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 20));
		butPanel.setBackground(Color.white);
		butPanel.add(login);
		add(butPanel, BorderLayout.SOUTH);
		setAction();
		A_setFrame.setting(this, "로그인", 400, 225);
	}
	
	private void setidPassPanel(JPanel panel, String string, JTextField tf) {
		JLabel l = new JLabel(string);
		l.setPreferredSize(new Dimension(75, 0));
		l.setFont(font.deriveFont(1,15));
		
		JPanel p = new JPanel(new BorderLayout(5, 5));
		p.setBackground(Color.white);
		p.add(l, BorderLayout.WEST);
		p.add(tf);
		
		panel.add(p);
	}
	
	private void setAction() {
		login.addActionListener(e->{
			String id = this.id.getText();
			String pw = this.pw.getText();
			if(id.isEmpty() || pw.isEmpty()) {
				mg("입력하지 않은 항목이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(id.equals("admin") && pw.equals("1234")) {
				mg("관리자님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
				User.admin = true;
				new MovieSerch(false);
				return;
			}
			Data user = Connections.select("select * from user where u_id = ? and u_pw = ?", id, pw).get(0);
			if(user.isEmpty()) {
				mg("존재하는 회원이 없습니다.",JOptionPane.ERROR_MESSAGE);
				this.id.setText("");
				this.pw.setText("");
				return;
			}
			mg(user.get(1) + "회원님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
			User.setUser(user);
			new Main();
			dispose();
		});
	}
	
	private void mg(String string, int type) {
		JOptionPane.showMessageDialog(null, string, type == JOptionPane.ERROR_MESSAGE ? "경고" : "정보", type);
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
