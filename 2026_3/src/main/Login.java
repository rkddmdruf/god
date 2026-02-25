package main;

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

	JButton login = new CustumButton("로그인");
	JTextField id = new JTextField();
	JTextField pw = new JTextField();
	
	public Login() {
		add(new JLabel("로그인") {{
			setFont(new Font("맑은 고딕", 1, 21));
			setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0), BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)));
		}}, BorderLayout.NORTH);
		
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel textPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		textPanel.setBackground(Color.white);
		setPanel(textPanel, id, "아이디");
		setPanel(textPanel, pw, "비밀번호");
		

		p.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			setBorder(BorderFactory.createEmptyBorder(25,0,0,0));
			setBackground(Color.white);
			add(login);
		}},BorderLayout.SOUTH);
		p.add(textPanel);
		add(p);
		
		setLoginAction();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				getter.pop().run();
				dispose();
			}
		});
		setFrame("로그인", 400, 180);
	}
	
	
	private void setLoginAction() {
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
				dispose();
				return;
			}
			List<Data> users = Connections.select("select * from user where u_id = ? and u_pw = ?", i, p);
			if(users.isEmpty()) {
				getter.mg("존재하는 회원이 없습니다.", JOptionPane.ERROR_MESSAGE);
				id.setText("");
				pw.setText("");
				return;
			}
			Data user = users.get(0);
			User.setUser(user);

			getter.mg(user.get(1) + "회원님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
			new Main();
			dispose();
			return;
			
		});
	}
	private void setPanel(JPanel p, JTextField tf, String string) {
		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.setBackground(Color.white);
		panel.add(new JLabel(string) {{
			setFont(new Font("맑은 고딕", 1, 15));
			setPreferredSize(new Dimension(75, 25));
		}});
		panel.add(tf);
		p.add(panel);
	}
	public static void main(String[] args) {
		new Login();
	}
}
