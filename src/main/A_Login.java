package main;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import utils.BaseFrame;
import utils.ButtonMaker;
import utils.Query;
import utils.Query;
import utils.Row;

public class A_Login extends BaseFrame{

	JTextField name = new JTextField() {{setPreferredSize(new Dimension(125,25));}};
	JPasswordField pw = new JPasswordField() {{setPreferredSize(new Dimension(125,25));}};
	JButton check = new ButtonMaker("확인", 60, 30) {{setFont(setBoldFont(13));}};
	JButton end = new ButtonMaker("종료", 60, 30) {{setFont(setBoldFont(13));}};
	JPanel downP = new JPanel() {{
		add(check);
		add(end);
	}};
	public A_Login() {
		setFrame("로그인", 300, 200, ()->{});
	}
	@Override
	public void desgin() {
		
		add(new JLabel("관리자 로그인", JLabel.CENTER) {{
			this.setFont(setBoldFont(22));
		}}, BorderLayout.NORTH);
		add(new JPanel(new BorderLayout()) {{
			add(new JPanel(new BorderLayout()) {{
				add(new JLabel("이름") {{setFont(setBoldFont(14));}}, BorderLayout.WEST);
				add(name, BorderLayout.EAST);
				setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50));
			}}, BorderLayout.NORTH);
			add(new JPanel(new BorderLayout()) {{
				add(new JLabel("비밀번호") {{setFont(setBoldFont(14));}}, BorderLayout.WEST);
				add(pw, BorderLayout.EAST);
				setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 50));
			}}, BorderLayout.SOUTH);
		}}, BorderLayout.CENTER);
		add(downP, BorderLayout.SOUTH);
	}

	@Override
	public void action() {
		check.addActionListener(e->{
			List<Row> list = Query.Login.select();
			for(int i = 0; i < list.size(); i++) {
				if(list.get(i).get(0).equals(name.getText()) && list.get(i).get(1).equals(new String(pw.getText()))) {
					new B_Admin();
					dispose();
				}
			}
		});
		end.addActionListener(e->{
			dispose();
		});
	}
	public static void main(String[] args) {
		new A_Login();
	}
}
