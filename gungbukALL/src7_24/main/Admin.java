package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import utils.BaseFrame;
import utils.ButtonMake;

public class Admin extends BaseFrame{
	
	
	Font font = new Font("맑은 고딕", Font.BOLD, 13);
	ButtonMake[] butn = {new ButtonMake("고객 등록", 100,  30, font), new ButtonMake("고객 조회", 100,  30, font),
			new ButtonMake("계약관리", 100,  30, font), new ButtonMake("종료", 100,  30, font)};
	JPanel topbt = new JPanel() {{
		for(int i = 0; i < butn.length; i ++) {this.add(butn[i]);}
	}};
	
	Admin(){
		setFrame("보험계약 관리화면", 700, 600, () -> {new Main();});
	}

	@Override
	public void design() {
		add(topbt,BorderLayout.NORTH);
		add(new JLabel() {{
			this.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/main/img.jpg")).getImage().
					getScaledInstance(600, 450, Image.SCALE_SMOOTH)));
			this.setBorder(BorderFactory.createEmptyBorder(20, 40, 60, 40));
		}}, BorderLayout.CENTER);
	}

	@Override
	public void action() {
		for(int i = 0; i < butn.length; i++) {
			butn[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == butn[0]) {
						new UserM();
						dispose();
					}else if(e.getSource() == butn[1]) {
						new UserS();
						dispose();
					}else if(e.getSource() == butn[2]) {
						new ContractM();
						dispose();
					}else if(e.getSource() == butn[3]) {
						dispose();
					}
				}
			});
		}
		
	}
	
	public static void main(String[] args) {
		new Admin();
	}
}
