package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import static javax.swing.BorderFactory.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Login extends JFrame{
	
	JPanel textPanel = new JPanel(new BorderLayout(2,2)) {{
		setBorder(createEmptyBorder(5, 5, 5, 5));
		setBackground(Color.white);
	}};
	JTextField id = new JTextField() {{
		setPreferredSize(new Dimension(280, this.getSize().height));
	}};
	JTextField pw = new JPasswordField() {{
		setPreferredSize(new Dimension(280, this.getSize().height));
	}};
	JButton login = new JButton("로그인") {{
		setForeground(Color.white);
		setBackground(Color.blue);
	}};
	Login(){
		new A_setFrame(this, "로그인", 400, 200);
		JPanel LoginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		LoginPanel.setBackground(Color.white);
		LoginPanel.setBorder(createMatteBorder(0, 0, 1, 0, Color.black));
		JLabel loginl = new JLabel("로그인");
		loginl.setFont(new Font("맑은 고딕", 1, 30));
		LoginPanel.add(loginl);
		add(LoginPanel, BorderLayout.NORTH);
		
		setMainPanel();
		add(textPanel);
		
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
		southPanel.setBackground(Color.white);
		southPanel.setBorder(createEmptyBorder(10, 0, 30, 0));
		southPanel.add(login);
		add(southPanel, BorderLayout.SOUTH);
		
		setAction();
	}
	
	private void setMainPanel() {
		JPanel ID = new JPanel(new BorderLayout());
		ID.setBackground(Color.white);
		ID.add(new JLabel("아이디") {{
			setFont(new Font("맑은 고딕", 1, 22));
		}}, BorderLayout.WEST);
		ID.add(id, BorderLayout.EAST);
		textPanel.add(ID, BorderLayout.NORTH);
		JPanel PW = new JPanel(new BorderLayout());
		PW.setBackground(Color.white);
		PW.add(new JLabel("비밀번호") {{
			setFont(new Font("맑은 고딕", 1, 22));
		}}, BorderLayout.WEST);
		PW.add(pw, BorderLayout.EAST);
		textPanel.add(PW, BorderLayout.SOUTH);
	}
	
	private void setAction() {
		login.addActionListener(e->{
			String id = this.id.getText();
			String pw = this.pw.getText();
			if(id.isEmpty() || pw.isEmpty()) {
				JOptionPane.showMessageDialog(null, "입력하지 않은 항목이 있습니다", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(id.equals("admin") && pw.equals("1234")) {
				JOptionPane.showMessageDialog(null, "관리자님 환영합니다", "정보", JOptionPane.INFORMATION_MESSAGE);
				//new ...();
				dispose();
				return;
			}
			List<Data> user = new Connections().select("select * from user where u_id = ? and u_pw = ?", id, pw);
			if(user.isEmpty()) {
				JOptionPane.showMessageDialog(null, "존재하는 회원이 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			new Main(Integer.parseInt(user.get(0).toString()));
			dispose();
		});
	}
	public static void main(String[] args) {
		new Login();
	}
}
