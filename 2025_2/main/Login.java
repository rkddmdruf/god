package main;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import utils.BaseFrame;
import utils.ButtonMaker;
import utils.Query;
import utils.Row;

public class Login extends BaseFrame{

	JTextField id = new JTextField() {{setPreferredSize(new Dimension(11111,35));}};
	JPasswordField pw = new JPasswordField() {{setPreferredSize(new Dimension(2211115,35));}};
	JButton login = new ButtonMaker("로그인",Color.blue, Color.white, 115,130) {{setFont(setBoldFont(16));}};
	Login(){
		setFrame("로그인", 500, 250, ()->{});
	}
	@Override
	public void desgin() {
		getContentPane().setBackground(Color.white);
		add(new JLabel("로그인") {{
			setFont(setBoldFont(27));setHorizontalAlignment(JLabel.CENTER);
		}},BorderLayout.NORTH);
		add(new JPanel(new BorderLayout()) {{
			setBackground(Color.white);
			add(new JPanel(new BorderLayout()) {{
				setBackground(Color.white);
				add(new JPanel(new BorderLayout()) {{
					setBackground(Color.white);
					setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
					add(new JLabel("아이디") {{setFont(setBoldFont(16));setPreferredSize(new Dimension(100, 30));}}, BorderLayout.WEST);
					add(id, BorderLayout.CENTER);
				}}, BorderLayout.NORTH);
				add(new JPanel(new BorderLayout()) {{
					setBackground(Color.white);
					setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 20));
					add(new JLabel("비밀번호") {{setFont(setBoldFont(16));setPreferredSize(new Dimension(100, 30));}}, BorderLayout.WEST);
					add(pw, BorderLayout.CENTER);
				}}, BorderLayout.SOUTH);
			}}, BorderLayout.CENTER);
			add(new JPanel() {{setBackground(Color.white);add(login);setBorder(BorderFactory.createEmptyBorder(15, 0, 30, 10));}},BorderLayout.EAST);
		}}, BorderLayout.CENTER);
	}

	@Override
	public void action() {
		login.addActionListener(e->{
			String PW = pw.getText();
			String ID = id.getText();
			if(ID.equals("") || PW.equals("")) {
				JOptionPane.showMessageDialog(this, "빈칸이 있습니다", "경고", JOptionPane.ERROR_MESSAGE);
			}else {
				List<Row> list = Query.Login.select(ID,PW);
				if(!list.isEmpty()) {
					JOptionPane.showMessageDialog(this, list.get(0).getString(3) + "님 환영합니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호를 확인해주세요.", "경고", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
