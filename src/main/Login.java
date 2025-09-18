package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;

public class Login extends BaseFrame{
	List<Row> list = new ArrayList<>();
	JTextField[] tf = new JTextField[2];
	JPanel BorderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(15,10,10,10));
	}};
	JButton but = new JButton("로그인") {{
		setForeground(Color.white);
		setBackground(sp.color);
	}};
	Login(){
		setFrame("로그인", 450, 175, ()->{});
	}
	@Override
	protected void desing() {
		for(int i = 0; i < 2; i++) {
			tf[i] = new JTextField();
			tf[i].setPreferredSize(new Dimension(100, 30));
		}
		add(new JLabel("알바캣") {{
			setFont(sp.font(1, 22));
			setForeground(sp.color);
			setHorizontalAlignment(JLabel.CENTER);
		}}, sp.n);
		BorderPanel.add(new JPanel(new GridLayout(0,2, 10, 10)) {{
			setBorder(BorderFactory.createEmptyBorder(15,0,10,0));
			setBackground(Color.white);
			for(JTextField t : tf) add(t);
		}},sp.n);
		BorderPanel.add(but);
		add(BorderPanel);
	}
	@Override
	protected void action() {
		but.addActionListener(e->{
			if(tf[0].getText().equals("") || tf[1].getText().equals("")) {
				sp.ErrMes("빈칸이 있습니다");
				return;
			}
			list = Query.user.select(tf[0].getText(), tf[1].getText());
			if(list.isEmpty()) 
			{	
				if(tf[0].getText().equals("admin") && tf[1].getText().equals("1234")) {
					sp.InforMes("관리자로 로그인하였습니다.");
					return;
				}
				sp.ErrMes("일치하는 회원 정보가 없습니다.");
				return;
			}
			sp.InforMes(list.get(0).getString(1)+ "회원님 환영합니다.");
			new Main();
			dispose();
		});
	}
	public static void main(String[] args) {
		new Login();
	}
}
