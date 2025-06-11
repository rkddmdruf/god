package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;

import javax.swing.*;

import utils.BaseFrame;
import utils.ButtonMaker;

public class C_UserUPandUPDATE extends BaseFrame{

	String[] str = "고객코드,고객명,생년월일,연락처,주소,회사".split(",");
	JLabel[] Label = new JLabel[6];
	JTextField[] TxtF = new JTextField[6];
	JButton up = new ButtonMaker("추가", 60,30);
	JButton end = new ButtonMaker("닫기", 60,30);
	public C_UserUPandUPDATE(String title, int pom) {
		setFrame(title, 500, 300, ()->{
			if(pom == 0) {new B_Admin();}
		});
	}
	@Override
	public void desgin() {
		add(new JPanel(new GridLayout(6,2)) {{
			for(int i = 0; i < 6; i++) {
				add(Label[i] = new JLabel(str[i] + " :"));
				add(TxtF[i] = new JTextField());
				TxtF[i].setEnabled(i > 1);
			}
		}}, BorderLayout.CENTER);
		add(new JPanel() {{
			add(up);add(end);
		}}, BorderLayout.SOUTH);
	}

	@Override
	public void action() {
		TxtF[2].addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					String ld = LocalDate.now().getYear()+"";
					String[] str = TxtF[2].getText().split("-");
					int sub = Integer.parseInt(str[0])+Integer.parseInt(str[1])+Integer.parseInt(str[2]);
					TxtF[0].setText("S"+ld.substring(2,4)+sub);
				}
			}
		});
	}
}
