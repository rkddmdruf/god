package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;

import utils.BaseFrame;
import utils.Query;
import utils.Row;
import utils.sp;

public class Login1 extends BaseFrame{

	int enterCheck = 0;
	JTextField[] tf = new JTextField[2];
	JPanel mainPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(20,10,50,80));
	}};
	JPanel northPanel = new JPanel(new GridBagLayout()) {{
		setBackground(Color.white);
	}};
	JPanel southPanel = new JPanel(new GridBagLayout()) {{
		setBackground(Color.white);
	}};
	Login1(){
		setFrame("로그인", 500, 225, ()->{});
	}
	@Override
	protected void desing() {
		for(int i = 0; i < 2; i++) {
			tf[i] = new JTextField() {{
				setPreferredSize(new Dimension(300, 40));
				setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(100,200, 255)));
			}};
		}
		add(new JLabel("login",JLabel.CENTER) {{
			setFont(sp.fontM(1, 20));
		}},sp.c);
		
		GridBagConstraints g = new GridBagConstraints();
		g.weightx = 0.2;
		g.gridx = 0;
		g.gridy = 0;
		northPanel.add(new JLabel("ID") {{setFont(sp.fontM(1, 14));}}, g);
		southPanel.add(new JLabel("PW") {{setFont(sp.fontM(1, 14));}}, g);
		g.weightx = 0.8;
		g.gridx = 1;
		g.gridy = 0;
		northPanel.add(tf[0]);
		southPanel.add(tf[1]);
		setEn(1);
		
		mainPanel.add(northPanel,sp.n);
		mainPanel.add(southPanel,sp.s);
		add(mainPanel, sp.s);
	}

	@Override
	protected void action() {
		
		for(JTextField t : tf) {
			t.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode() == 10) {
						if(e.getSource() == tf[0]) {
							setEn(0);
							tf[1].requestFocusInWindow();
							return;
						}
						
						if(tf[0].getText().equals("") || tf[1].getText().equals("")) {
							setEn(1);
							sp.ErrMes("빈칸이 있습니다.");
							setTexts();
							return;
						}
						List<Row> list = Query.login.select(tf[0].getText(), tf[1].getText());
						if(list.isEmpty()) {
							setEn(1);
							sp.ErrMes("아이디 또는 비밀번호를 확인해주세요.");
							setTexts();
							return;
						}
						sp.InforMes("정보: " + list.get(0).getString(1) + " 회원님 환영합니다");
						sp.user.add(list.get(0));
						dispose();
						//new Main();
					}
				}
			});
		}
	}
	
	private void setEn(int index) {
		tf[index].setFocusable(false);
		tf[index == 0 ? 1 : 0].setFocusable(true);
	}
	private void setTexts() {
		for(JTextField t : tf) {
			t.setText("");
		}
	}
	public static void main(String[] args) {
		new Login1();
	}

}
