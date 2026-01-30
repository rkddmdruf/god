package main;

import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.*;

import utils.*;

public class Login extends CFrame{
	Font font = new Font("맑은 고딕", 1, 26);
	
	JTextField id = new JTextField();
	JTextField pw = new JPasswordField();
	
	JButton login = new CustumButton("로그인");
	JFrame f = this;
	public Login()	{
		JLabel l = new JLabel("로그인");
		l.setFont(font);
		l.setBorder(createEmptyBorder(10, 10, 0, 0));
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.white);
		mainPanel.setBorder(createCompoundBorder(createMatteBorder(1, 0, 0, 0, Color.black), createEmptyBorder(5,5,5,5)));
		
		JPanel ipPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		ipPanel.setBackground(Color.white);
		ipPanel.add(setPanel(id, "아이디"));
		ipPanel.add(setPanel(pw, "비밀번호"));
		
		mainPanel.add(ipPanel);
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(login);
			setBorder(createEmptyBorder(20,0,20,0));
		}}, BorderLayout.SOUTH);
		
		add(l, BorderLayout.NORTH);
		add(mainPanel);
		
		setAction();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				getter.fromMove(f);
			}
		});
		setFrame("로그인", 400, 200);
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
				User.admin = true;
				new MovieSerch();
				return;
			}
			
			List<Data> user = Connections.select("select * from user where u_id = ? and u_pw = ?", i, p);
			if(user.isEmpty()) {
				getter.mg("존재하는 회원이 없습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			User.setUser(user.get(0));
			getter.mg(user.get(0).get(1) + "회원님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);	
			getter.fromMove(this);
			return;
		});
	}
	
	private JPanel setPanel(JTextField t, String s) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		
		JLabel l = new JLabel(s);
		l.setFont(font.deriveFont(16f));
		l.setPreferredSize(new Dimension(90, 25));
		
		p.add(l, BorderLayout.WEST);
		p.add(t);
		
		return p;
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
