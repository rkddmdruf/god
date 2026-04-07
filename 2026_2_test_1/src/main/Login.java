package main;

import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.getter;

public class Login extends CFrame{

	JTextField id = new JTextField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(id.getText().isBlank()) {
				g.setColor(Color.gray);
				g.drawString("아이디를 입력하세요!", 2, 20);
			}
		};
	};
	JTextField pw = new JTextField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(id.getText().isBlank()) {
				g.setColor(Color.gray);
				g.drawString("비밀번호를 입력하세요!", 2, 20);
			}
		};
	};
	JButton login = new JButton("로그인") {{
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	public Login() {
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {{
			setBackground(Color.white);
			add(new JLabel(getter.getImage("datafiles/icon/logo.png", 40, 40)) {{
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						new Main();
						dispose();
					};
				});
			}});
			add(new JLabel("Skills Qualification Association"));
		}}, BorderLayout.NORTH);
		
		JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
		panel.setBackground(Color.white);
		panel.setBorder(createEmptyBorder(0, 10, 10, 10));
		panel.add(id);
		panel.add(pw);
		panel.add(login);
		
		
		login.addActionListener(e -> {
			String i = id.getText();
			String p = pw.getText();
			if(i.isBlank() || p.isBlank()) {
				getter.err("빈칸이 있습니다");
				return;
			}
			
			if(i.equals("admin") && p.equals("1234")) {
				new Chart();
				dispose();
				return;
			}
			List<Data> list = Connections.select("select * from user where id = ? and pw = ?", i ,p);
			if(list.isEmpty()) {
				getter.err("아이디또는 비밀번호가 올바르지 않습니다.");
				id.setText("");
				pw.setText("");
				return;
			}
			User.setUser(list.get(0));
			getter.infor(User.getUser().get(1) + "님 환영합니다.");
			new Main();
			dispose();
			return;
		});
		borderPanel.add(panel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new Main();
				dispose();
			}
		});
		borderPanel.setBorder(createEmptyBorder(10, 10, 10, 10));
		setFrame("로그인", 300, 200);
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
