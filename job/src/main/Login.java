package main;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import utils.*;

public class Login extends BaseFrame{
	JTextField[] tf = new JTextField[2];
	JPanel BorderPanel = new sp.cp(new BorderLayout(), sp.em(15,10,10,10), null);
	JButton but = new sp.cb("로그인").fontColor(Color.white).BackColor(sp.color);
	Login(){
		setFrame("로그인", 450, 175, ()->{});
	}
	@Override
	protected void desing() {
		for(int i = 0; i < 2; i++) {final int index = i;
			tf[i] = paceHold(i == 0 ? "ID" : "PW");
			tf[i].setPreferredSize(new Dimension(100, 30));
		}
		JLabel cat = new sp.cl("알바캣").font(sp.font(1, 22)).fontColor(sp.color).setHo(JLabel.CENTER);
		add(cat, sp.n);
		
		JPanel inputPanel = new sp.cp(new GridLayout(0, 2, 10, 10), sp.em(15, 0, 10, 0), null);
		for(final JTextField t : tf) { inputPanel.add(t); }
		BorderPanel.add(inputPanel ,sp.n);
		
		BorderPanel.add(but);
		add(BorderPanel);
	}
	@Override
	protected void action() {
		but.addActionListener(e->{
			String id = tf[0].getText();
			String pw = tf[1].getText();
			if(id.equals("") || pw.equals("")) {
				sp.ErrMes("빈칸이 있습니다");
				return;
			}
			if(id.equals("admin") && pw.equals("1234")) {
				sp.InforMes("관리자로 로그인하였습니다.");
				new AdminMain();
				dispose();
				return;
			}
			List<Row> list = Query.user.select(id, pw);
			if(list.isEmpty()) { sp.ErrMes("일치하는 회원 정보가 없습니다."); return; }
			
			sp.InforMes(list.get(0).getString(1)+ "회원님 환영합니다.");
			sp.user = list.get(0);
			new Main();
			
			dispose();
		});
	}
	
	private JTextField paceHold(String s) {
		return new JTextField() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				if(getText().isEmpty()) {
					g.setColor(Color.LIGHT_GRAY);
					g.setFont(sp.font(1, 15));
					g.drawString(s, 2, 20);
				}
			} 
		};
	}
	public static void main(String[] args) {
		new Login();
	}
}
