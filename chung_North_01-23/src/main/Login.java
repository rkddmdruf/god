package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.JTextArea;

public class Login extends JFrame {
	Font font = new Font("맑은 고딕", 1, 28);
	JTextField id = new JTextField();
	JTextField pw = new JTextField();
	
	JButton but = new CustumButton("로그인");
	Login(){
		JLabel l = new JLabel(" 로그인", JLabel.LEFT);
		l.setFont(font);
		
		add(l, BorderLayout.NORTH);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.white);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black), BorderFactory.createEmptyBorder(5,5,5,5)));
		
		JPanel ipPanel = new JPanel(new GridLayout(2, 1, 3, 3));
		ipPanel.setBackground(Color.white);
		setTextField(id, ipPanel, "아이디");
		setTextField(pw, ipPanel, "비밀번호");
		
		mainPanel.add(ipPanel);
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
			add(but);
		}}, BorderLayout.SOUTH);
		add(mainPanel);
		
		but.addActionListener(e->{
			String i = id.getText();
			String p = pw.getText();
			if(i.isEmpty() || p.isEmpty()) {
				getter.mg("입력하지 않은 항목이 있습니다", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(i.equals("admin") && p.equals("1234")) {
				User.admin = true;
				getter.mg("관리자님 환영합니다", JOptionPane.INFORMATION_MESSAGE);
				new MovieSerch();
				return;
			}
			List<Data> user = Connections.select("select * from user where u_id = ? and u_pw = ?", i, p);
			if(user == null || user.isEmpty()) {
				getter.mg("존재하는 회원이 없습니다", JOptionPane.ERROR_MESSAGE);
				id.setText("");
				pw.setText("");
				return;
			}
			getter.mg(user.get(0).get(1).toString() + "회원님 환영합니다", JOptionPane.INFORMATION_MESSAGE);
			User.setUser(user.get(0));
			new Main();
			dispose();
			return;
		});
		setFrame.setframe(this, "로그인", 400, 190);
	}
	
	private void setTextField(JTextField tf, JPanel p, String s) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		
		JLabel l = new JLabel(s);
		l.setFont(font.deriveFont(18f));
		l.setPreferredSize(new Dimension(90, 25));
		
		panel.add(l, BorderLayout.WEST);
		panel.add(tf);
		
		p.add(panel);
	}
	public static void main(String[] args) {
		new Login();
	}
}
