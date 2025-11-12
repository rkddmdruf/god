package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utils.*;
import utils.sp.*;

public class Login extends BaseFrame{
	JPanel borderPanel = new cp(new BorderLayout(), sp.em(10, 10, 10, 10), null);
	JButton login = new cb("로그인").font(sp.font(0, 12)).fontColor(Color.white).BackColor(sp.color);
	JTextField id = new JTextField() {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(!this.getText().isEmpty()) return;
			g.setFont(sp.font(1, 14));
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("ID", 5, 15);
		}
	};
	JPasswordField pw = new JPasswordField() {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(!this.getText().isEmpty()) return;
			g.setFont(sp.font(1, 14));
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("PW", 5, 15);
		}
	{}};
	Login(){
		setFrame("로그인", 400, 160, ()->{});
	}
	@Override
	protected void desing() {
		borderPanel.add(new sp.cl("알바캣").setHo(JLabel.CENTER).font(sp.font(1, 18)).fontColor(sp.color), sp.n);
		borderPanel.add(new sp.cp(new GridLayout(0,2,10,10), sp.em(20, 0, 10, 0), null) {{
			add(id); add(pw);
		}});
		borderPanel.add(login, sp.s);
		add(borderPanel);
	}

	@Override
	protected void action() {
		login.addActionListener(e->{
			if(id.getText().isEmpty() || pw.getText().isEmpty()) {sp.ErrMes("빈칸이 있습니다");return;}
			if(id.getText().equals("admin") && pw.getText().equals("1234")) {//관리자
				sp.InforMes("관리자로 로그인하였습니다.");
				return;
			}
			List<Row> row = Query.selectText("select * from user where uid = ? and upw = ?", id.getText(), pw.getText().toString());
			System.out.println(row);
			System.out.println(row);
			if(row.isEmpty()) {sp.InforMes("일치하는 회원 정보가 없습니다."); return;}
			sp.InforMes(row.get(0).getString(1) + "회원님 환영합니다.");
			sp.user = row.get(0);
			new Main();
			dispose();
		});
	}
	public static void main(String[] args) {
		new Login();
	}
}
