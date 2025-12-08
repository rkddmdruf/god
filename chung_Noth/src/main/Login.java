package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class Login extends BaseFrame{
	JButton but = new cb("로그인").BackColor(Color.blue).fontColor(Color.white);
	JTextField id = new JTextField();
	JPasswordField pw = new JPasswordField();
	public Login(){
		setFrame("로그인", 400, 240, ()->{});
	}
	@Override
	protected void desing() {
		add(new cp(new FlowLayout(FlowLayout.LEFT), sp.em(10, 5, 5, 5), null) {{
			add(new cl("로그인").font(sp.font(1, 26)));
		}}, sp.n);
		JPanel mainPanel = new cp(new BorderLayout(), sp.com(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black), sp.em(5, 5, 10, 5)), null);
		
		mainPanel.add(new cp(new BorderLayout(), sp.em(0, 0, 10, 0), null) {{
			for(int i = 0; i < 2; i++) {final int index = i;
				add(new cp(new BorderLayout(), null, null) {{
					add(new cl(index == 0 ? "아이디" : "비밀번호").font(sp.font(1, 16)).size(70, 35),sp.w);
					add(index == 0 ? id : pw);
				}}, index == 0 ? sp.n : sp.s);
			}
		}});
		mainPanel.add(new cp(new FlowLayout(FlowLayout.RIGHT), null, null) {{
			add(but);
		}}, sp.s);
		add(mainPanel);
	}

	@Override
	protected void action() {
		but.addActionListener(e->{
			String i = id.getText();
			String p = pw.getText();
			if(i.isEmpty() || p.isEmpty()) {
				sp.err("입력하지 않은 항목이 있습니다.");
				return;
			}
			if(i.equals("admin") && p.equals("1234")) {
				sp.Infor("관리자님 환영합니다");
				return;
			}
			List<Row> list = Query.select("SELECT * FROM moviedb.user where u_id = ? and u_pw = ?;", i, p);
			if(list.isEmpty()) {
				sp.err("존재하는 회원이 없습니다.");
				id.setText("");
				pw.setText("");
				return;
			}
			sp.Infor(list.get(0).get(1) + "회원님 환영합니다.");
			sp.user = list.get(0);
			new Main();
			dispose();
			return;
		});
	}
	public static void main(String[] args) {
		new Login();
	}
}
