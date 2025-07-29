package main;

import java.awt.*;
import javax.swing.*;

import utils.BaseFrame;

public class B_Admin extends BaseFrame{
	String[] butName = {"고객 등록","고객 조회", "계약관리", "종료"};
	JButton[] buts = new JButton[4];
	B_Admin(){
		setFrame("보험계약 관리화면", 600, 500, ()->{});
	}
	@Override
	public void desgin() {
		add(new JPanel() {{
			for(int i = 0; i < 4; i++) {
				buts[i] = new JButton(butName[i]) {{setPreferredSize(new Dimension(90,25));}};
				add(buts[i]);
			}
			add(new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/main/img.jpg")).getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH))));
		}},BorderLayout.NORTH);
	}
	@Override
	public void action() {
		for(int i = 0; i < 4; i++) {
			buts[i].addActionListener(e->{
				if(e.getSource() == buts[0]) {
					new CA_UserUP();
					dispose();
				}
				if(e.getSource() == buts[1]) {
					new D_ShowUser();
					dispose();
				}
				if(e.getSource() == buts[3]) {
					dispose();
				}
			});
		}
	}
	public static void main(String[] args) {
		new B_Admin();
	}
}
