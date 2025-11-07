package main;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class Login extends BaseFrame{
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 15, 10), null);
	JButton login = new sp.cb("로그인").BackColor(sp.color).fontColor(Color.white).font(sp.font(0, 17));
	JTextField id = new JTextField() {
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if(this.getText().isEmpty()) {
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(sp.font(1, 17));
				g.drawString("ID", 3,23);
			}
		}
	};
	JPasswordField pw = new JPasswordField() {
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if(this.getText().isEmpty()) {
				g.setColor(Color.LIGHT_GRAY);
				g.setFont(sp.font(1, 17));
				g.drawString("PW", 3,23);
			}
		}
	};
	Login(){
		setFrame("로그인", 500, 200, ()->{});
	}
	@Override
	protected void desing() {
		borderPanel.add(new sp.cp(new GridLayout(0, 2, 20, 20), sp.em(30, 0, 10, 0), null) {{
			add(id);
			add(pw);
		}});
		borderPanel.add(new sp.cl("알바캣").font(sp.font(1, 20)).fontColor(sp.color).setHo(JLabel.CENTER), sp.n);
		borderPanel.add(login, sp.s);
		add(borderPanel);
		
	}

	@Override
	protected void action() {
		login.addActionListener(e->{
			String ID = id.getText();
			String PW = pw.getText();
			if(ID.isEmpty() || PW.isEmpty()) {
				sp.ErrMes("빈칸이 있습니다.");
				return;
			}
			if(ID.equals("admin") && PW.equals("1234")) {
				sp.InforMes("관리자로 로그인하였습니다");
				dispose();
				return;
			}
			List<Row> list = Query.selectText("SELECT * FROM roupang.user where uid = ? and upw = ?;", ID, PW);
			if(list.isEmpty()) {
				sp.ErrMes("일치하는 회원 정보가 없습니다");
				return;
			}
			sp.InforMes(list.get(0).getString(3) + " 회원님 환영합니다");
			new Main();
			dispose();
		});
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
