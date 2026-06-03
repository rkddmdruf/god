package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.CFrame;
import utils.Connections;
import utils.Data;
import utils.User;
import utils.getter;

public class Login extends CFrame{

	JTextField id = new JTextField() {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(id.getText().isBlank()) {
				FontMetrics fm = getFontMetrics(getFont());
				int y = (getHeight() / 2) + (fm.getAscent() / 2);
				g.drawString("아이디 입력", 5, y);
			}
		};
	};
	JTextField pw = new JTextField() {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(pw.getText().isBlank()) {
				FontMetrics fm = getFontMetrics(getFont());
				int y = (getHeight() / 2) + (fm.getAscent() / 2);
				g.drawString("비밀번호 입력", 5, y);
			}
		};
	};
	
	JPanel panel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
	}};
	JButton login = new JButton("로그인") {{
		setBackground(Color.white);
		setForeground(Color.black);
	}};
	public Login() {
		borderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		borderPanel.add(panel);
		panel.add(new JLabel("로그인", JLabel.CENTER) {{
			setForeground(Color.black);
			setFont(new Font("맑은 고딕", 1, 20));
		}}, BorderLayout.NORTH);
		
		JPanel gridPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		gridPanel.setBackground(Color.white);
		gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		gridPanel.add(new JPanel(new BorderLayout(2, 2)) {{
			setBackground(Color.white);
			add(new JLabel("ID"), BorderLayout.NORTH);
			add(id);
		}});
		gridPanel.add(new JPanel(new BorderLayout(2, 2)) {{
			setBackground(Color.white);
			add(new JLabel("PW"), BorderLayout.NORTH);
			add(pw);
		}});
		
		panel.add(gridPanel);
		panel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(login);
		}}, BorderLayout.SOUTH);
		
		login.addActionListener(e -> {
			if(id.getText().isBlank() || pw.getText().isBlank()) {
				getter.err("빈칸이 있습니다.");
				return;
			}
			
			if(id.getText().equals("admin") && pw.getText().equals("1234")) {
				getter.infor("관리자로 로그인 되었습니다.");
				User.admin  = true;
				return;
			}
			List<Data> list = Connections.select("select * from user where id = ? and pw = ?", id.getText(), pw.getText());
			if(list.isEmpty()) {
				getter.err("아이디 또는 비밀번호가 다릅니다.");
				return;
			}

			User.setUser(list.get(0));
			User.setLoginDate();
			getter.infor("로그인 되었습니다");
			new EmpList();
			dispose();
			return;
		});
		
		setFrameCd("관리 시스템", 400, 200, () -> {});
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
