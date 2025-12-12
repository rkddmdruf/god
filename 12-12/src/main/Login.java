package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class Login extends BaseFrame{
	
	JPanel borderPanel = new cp(new BorderLayout(), sp.em(10, 10, 20, 10), null);
	JTextField id = new JTextField() {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(getText().isEmpty()) {
				g.setFont(sp.font(1, 16));
				g.setColor(Color.LIGHT_GRAY);
				g.drawString("ID", 3,22);
			}
			
		}
	};
	JPasswordField pw = new JPasswordField() {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(getText().isEmpty()) {
				g.setFont(sp.font(1, 16));
				g.setColor(Color.LIGHT_GRAY);
				g.drawString("PW", 3,22);
			}
			
		}
	};
	JButton login = new cb("로그인").backColor(sp.color).fontColor(Color.white);
	Login(){
		setFrame("로그인", 500,  200, ()->{});
	}
	@Override
	protected void desing() {
		borderPanel.add(new cl("알바캣").font(sp.font(1, 25)).fontColor(sp.color).setHo(JLabel.CENTER), sp.n);
		borderPanel.add(new cp(new GridLayout(0,2, 10, 10), sp.em(25, 0, 15, 0), null) {{
			add(id);
			add(pw);
		}});
		borderPanel.add(login, sp.s);
		add(borderPanel);
	}

	@Override
	protected void action() {
		login.addActionListener(e->{
			String i = id.getText();
			String p = pw.getText();
			if(i.isEmpty() || p.isEmpty()) {
				sp.errMes("빈간이 있습니다.");
				return;
			}
			if(i.equals("admin") && p.equals("1234")) {
				sp.InforMes("관리자로 로그인하였습니다.");
				
				return;
			}
			List<Row> list = Query.selectText("select * from user where uid = ? and upw = ?;", i, p);
			if(list.isEmpty()) {
				sp.errMes("일치하는 회원 정보가 없습니다.");
				return;
			}
			sp.InforMes(list.get(0).get(1) + " 회원님 환영합니다." );
			new Main();
			dispose();
		});
	}
	public static void main(String[] args) {
		new Login();
	}
}
