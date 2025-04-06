package main;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*;
import java.sql.*;
//밑줄 추가
public class A_Login extends JFrame{
	public JFrame f = new JFrame();
	public Container cf = f.getContentPane();
	public JLabel Question = new JLabel("Question");
	public JPanel whitep = new JPanel();
	public JLabel ID = new JLabel("ID");
	public JTextField IDTF = new JTextField(20);
	public JLabel PW = new JLabel("PW");
	public JPasswordField PWTF = new JPasswordField(20);
	public JLabel Aarea = new JLabel("");
	public JButton login = new JButton("로그인");
	
	A_Login(){
		Image icon = f.getToolkit().getImage("imgs/icon/logo.png");
		setIconImage(icon);

		setTitle("로그인");
		setLayout(null);
		setResizable(false);
		//기본 패널
		whitep.setLayout(null);
		whitep.setBackground(Color.white);
		whitep.setBounds(0, 0, 500, 300);
		//로그인 버튼
		login.setBackground(new Color(30,120,20));
		login.setForeground(Color.white);
		login.setBounds(350, 85, 100, 115);
		login.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		login.addActionListener(new LoginAction());
		
		Question.setBounds(190,10, 120, 40);
		ID.setBounds(35, 80, 40, 40);
		IDTF.setBounds(70, 80, 270, 40);
		PW.setBounds(35, 150, 50, 50);
		PWTF.setBounds(70, 160, 270, 40);
		Aarea.setBounds(70, 200, 180, 40);
		
		Question.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		Aarea.setForeground(Color.red);
		
		whitep.add(login);
		whitep.add(Question);
		whitep.add(ID);
		whitep.add(IDTF);
		whitep.add(PW);
		whitep.add(PWTF);
		whitep.add(Aarea);
		add(whitep);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(710, 390, 500,300);
		setVisible(true);
	}
	
	public class LoginAction implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String pw = new String(PWTF.getPassword());
			int checks = 0;
			if(IDTF.getText().equals("") || pw.equals("")) {
				Aarea.setText("빈칸이 있습니다.");
			}else {
				try {
					Connection c = DriverManager.getConnection(Z_UUP.url(), Z_UUP.username(), Z_UUP.password());
					Statement s = c.createStatement();
					ResultSet reu = null;
					String selectList = "user";
					int number = 0;
					boolean user = false;
					String name = "";
					for(int i = 0; i <=1;i++) {
						if(i==0) {selectList = "user";}
						else if(i==1) {selectList = "teacher";}
						String query = "select * from question." + selectList;
						reu = s.executeQuery(query);
						
						while(reu.next()) {
							if(IDTF.getText().equals(reu.getString("id")) && !pw.equals(reu.getString("pw"))) {
								checks++;
								break;
							}else if(IDTF.getText().equals(reu.getString("id")) && pw.equals(reu.getString("pw"))) {
								
								Aarea.setText("");
								if(selectList.equals("user")) {
									JOptionPane.showMessageDialog(whitep, "정보 : \"" + reu.getString("name") + " 학생님 환영합니다.\"");
									number = reu.getInt("uno");
									name = reu.getString("name");
									user = true;
								}else if(selectList.equals("teacher")) {
									JOptionPane.showMessageDialog(whitep, "정보 : \"" + reu.getString("name") + " 선생님 환영합니다.\"");
									number = reu.getInt("tno");
									name = reu.getString("name");
									user = false;
								}
								checks = 2;
								setVisible(false);
								new B_Main(user, number, name);
								break;
							}
						}
					}
					if(checks == 0) {Aarea.setText("존재하지 않는 회원입니다. ");}
					else if(checks == 1) {Aarea.setText("비밀번호가 일치하지 않습니다.");}
					
					c.close();
					s.close();
					reu.close();
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
				}
			}
		}
	}

	public static void main(String[] args) {
		new A_Login();
	}
}
