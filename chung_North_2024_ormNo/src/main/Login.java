package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;


import static javax.swing.BorderFactory.*;
import utils.*;

public class Login extends CFrame{
	Font font = new Font("맑은 고딕", 0, 9);
	JPanel borderPanel = new JPanel(new BorderLayout(2,2)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(3,7,3,10));
	}};
	
	JButton login = new JButton("로그인") {{
		setForeground(Color.white);
		setFont(font);
		setBackground(getter.COLOR);
	}};
	JTextField id = new JTextField();
	JTextField pw = new JTextField();
	public Login(){
		borderPanel.add(new JLabel("게임 스토어") {{
			setFont(font.deriveFont(1, 20));
		}}, BorderLayout.NORTH);
		
		JPanel tPanel = new JPanel(new GridLayout(2, 1, 3, 3));
		tPanel.setBackground(Color.white);
		
		tPanel.add(id);
		tPanel.add(pw);
		
		borderPanel.add(tPanel);
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(login);
		}}, BorderLayout.SOUTH);
		add(borderPanel);
		
		login.addActionListener(e->{
			String i = id.getText();
			String p = pw.getText();
			if(i.isEmpty() || p.isEmpty()) {
				getter.mg("입력하지 않은 항목이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(i.equals("admin") && p.equals("1234")) {
				getter.mg("관리자님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
				dispose();
				return;
			}
			
			List<Data> user = Connections.select("select * from user where u_id = ? and u_pw = ?", i, p);
			if(user.isEmpty()) {
				getter.mg("존재하는 회원이 없습니다.", JOptionPane.ERROR_MESSAGE);
				id.setText("");
				pw.setText("");
				return;
			}
			
			getter.mg(user.get(0).get(1) + "회원님 환영합니다.", JOptionPane.INFORMATION_MESSAGE);
			UserU.setUser(user.get(0));
			new Main();
			dispose();
			return;
		});
		setFrame("로그인", 250, 125);
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
