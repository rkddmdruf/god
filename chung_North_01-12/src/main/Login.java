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
	JTextField id = new JTextField();
	JTextField pw = new JPasswordField();
	JPanel mainPanel = new JPanel(new BorderLayout(25, 25)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10, 10, 10, 10));
	}};
	JButton login = new CustumButton("로그인");
	Login(){
		add(new JLabel("로그인") {{
			setBorder(createCompoundBorder(createMatteBorder(0, 0, 1, 0, Color.black), createEmptyBorder(10, 10, 10, 10)));
			setFont(new Font("맑은 고딕", 1, 24));
		}}, BorderLayout.NORTH);
		
		
		JPanel IPP = new JPanel(new GridLayout(2, 0, 5, 5));
		JPanel idp = new JPanel(new BorderLayout());
		JPanel pwp= new JPanel(new BorderLayout());
		Font font = new Font("맑은 고딕", 1, 18);
		Dimension sizz = new Dimension(100, 20);
		
		idp.setBackground(Color.white);
		idp.add(new JLabel("아이디") {{
			setFont(font);
			setPreferredSize(sizz);
		}}, BorderLayout.WEST);
		idp.add(id);
		
		pwp.setBackground(Color.white);
		pwp.add(new JLabel("비밀번호") {{
			setFont(font);
			setPreferredSize(sizz);
		}}, BorderLayout.WEST);
		pwp.add(pw);
		
		IPP.setBackground(Color.white);
		IPP.add(idp);
		IPP.add(pwp);
		mainPanel.add(IPP);
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			setBackground(Color.white);
			add(login);
		}}, BorderLayout.SOUTH);
		
		add(mainPanel);
		
		login.addActionListener(e->{
			String id = this.id.getText();
			String pw = this.pw.getText();
			if(id.isEmpty() || pw.isEmpty()) {
				JOptionPane.showMessageDialog(null, "입력하지 않은 항목이 있습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(id.equals("admin") && pw.equals("1234")) {				
				JOptionPane.showMessageDialog(null, "관리자님 환영합니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				dispose();
				return;
			}
			Data user = Connections.select("select * from user where u_id = ? and u_pw = ?", id, pw).get(0);
			if(user.isEmpty()) {
				JOptionPane.showMessageDialog(null, "존재하는 회원이 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				this.id.setText("");
				this.pw.setText("");
				return;
			}
			JOptionPane.showMessageDialog(null,user.get(1) + "회원님 환영합니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			new Main(Integer.parseInt(user.get(0).toString()));
			dispose();
		});
		
		new A_setFrame(this, "로그인", 400, 250);
	}
	
	
	public static void main(String[] args) {
		new Login();
	}
}
