package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class Login extends JFrame{
	Font font = new Font("맑은 고딕", 1, 25);
	
	JTextField id = new JTextField();
	JTextField pw = new JPasswordField();
	
	JButton login = new CustumButton("로그인");
	Login(){
		JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		flowPanel.setBorder(createMatteBorder(0, 0, 1, 0, Color.black));
		flowPanel.setBackground(Color.white);
		
		JLabel l = new JLabel("로그인");
		l.setFont(font);
		flowPanel.add(l);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(createEmptyBorder(5,5,5,5));
		mainPanel.setBackground(Color.white);
		
		JPanel idPw = new JPanel(new GridLayout(2, 1, 3, 3));
		idPw.setBackground(Color.white);
		idPw.add(setJTF(id, "아이디"));
		idPw.add(setJTF(pw, "비밀번호"));
		
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		butPanel.setBackground(Color.white);
		butPanel.add(login);
		butPanel.setBorder(createEmptyBorder(20, 0, 10, 0));
		
		mainPanel.add(butPanel, BorderLayout.SOUTH);
		mainPanel.add(idPw);
		add(mainPanel);
		add(flowPanel, BorderLayout.NORTH);
		SetFrames.setframe(this, "로그인", 400, 225);
	}
	
	private JPanel setJTF(JTextField tf, String string) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		
		JLabel l = new JLabel(string);
		l.setFont(font.deriveFont(18f));
		l.setPreferredSize(new Dimension(88, 25));
		
		p.add(l, BorderLayout.WEST);
		p.add(tf);
		return p;
	}
	
	private void login() {
		ActionListener a = e ->{
			String id = this.id.getText();
			String pw = this.pw.getText();
			if(id.isEmpty() || pw.isEmpty()) {
				mg("입력하지 않은 항목이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(id.equals("admin") && pw.equals("1234")){
				mg("관리자님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
				User.admin = true;
				return;
			}
			Data user = Connections.select("select * from user where u_id = ? and u_pw = ?", id, pw).get(0);
			if(user.isEmpty()) {
				mg("존재하는 회원이 없습니다.", JOptionPane.ERROR_MESSAGE);
				this.id.setText("");
				this.pw.setText("");
				return;
			}
			mg(user.get(1) + "회원님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
			User.setUser(user);
			new Main();
			dispose();
			return;
		};
		login.addActionListener(a);
	}
	
	private void mg(String string, int type) {
		JOptionPane.showMessageDialog(null, string, type == JOptionPane.ERROR_MESSAGE ? "경고" : "정보", type);
	}
	public static void main(String[] args) {
		new Login();
	}
}
