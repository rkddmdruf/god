package main;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;

public class Login {
	JFrame f =new JFrame("로그인");
	JLabel l = new JLabel("로그인", JLabel.CENTER);
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JPanel p3 = new JPanel();
	
	JButton login = new JButton("로그인");
	
	JLabel lid = new JLabel("아이디  ");
	JTextField id = new JTextField(20);
	JLabel lpass = new JLabel("비밀번호 ");
	JPasswordField pass = new JPasswordField(20);
	Login(){
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(null);
		
		p1.setLayout(null);
		p2.setLayout(null);
		p3.setLayout(null);
		
		p1.setBounds(0,0,480, 50);
		p2.setBounds(0, 50, 325, 200);
		p3.setBounds(325, 50, 155, 200);
		
		p1.setBackground(Color.white);
		p2.setBackground(Color.white);
		p3.setBackground(Color.white);
		
		l.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		l.setBounds(214, 0, 72, 50);

		lid.setFont(new Font("함초롬바탕", Font.BOLD, 17));
		lid.setBounds(25,5, 60, 60);
		
		lpass.setFont(new Font("함초롬바탕", Font.BOLD, 17));
		lpass.setBounds(25,80, 70, 60);

		id.setBounds(110, 24, 200,35);
		pass.setBounds(110, 99, 200,35);
		
		login.setBackground(Color.blue);
		login.setForeground(Color.white);
		login.setFont(new Font("함초롬바탕", Font.BOLD, 20));
		login.setBounds(0, 15, 120, 135);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pw = new String(pass.getPassword());
				int checks = 0;
				if(id.getText().equals("") || pw.equals("")) {
					JOptionPane.showMessageDialog(f, "경고 : 빈칸이 있습니다.");
				}else {
					try {
						Connection c = DriverManager.getConnection(UUP.url(), UUP.username(), UUP.password());
						Statement s = c.createStatement();
						ResultSet re = s.executeQuery("select * from clothingstore.user");
						while(re.next()) {
							if(id.getText().equals(re.getString("u_id")) && pw.equals(re.getString("u_pw"))){
								JOptionPane.showMessageDialog(f, "정보 : " + re.getString("u_name") + "님 환영합니다.");
								f.setVisible(false);
								checks = 0;
								break;
							}else {
								checks = 1;
							}
						}
						if(checks == 1) {
							JOptionPane.showMessageDialog(f, "경고 : 아이디 또는 비밀번호가 일치하지 않습니다. ");
						}
						c.close();
						s.close();
						re.close();
					} catch (Exception e2) {
						System.out.println(e2.getMessage());
					}
				}
			}
		});
		
		p1.add(l);
		p2.add(id);
		p2.add(pass);
		p2.add(lid);
		p2.add(lpass);
		p3.add(login);
		f.add(p1);
		f.add(p2);
		f.add(p3);
		
		f.setBounds(720, 410, 480,260);
		f.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
