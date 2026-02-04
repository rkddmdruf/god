package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import utils.*;

public class InforChange extends CFrame{
	JButton change = new JButtonC("정보 수정");
	JTextField nick = new JTextField();
	JTextField pw = new JTextField();
	JTextField name = new JTextField();
	JTextField id = new JTextField();
	JTextField birth = new JTextField() {{
		setEnabled(false);
	}};
	
	JCheckBox publicCheck = new JCheckBox("공개");
	JCheckBox privateCheck = new JCheckBox("비공개");
	
	JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 10, 10, 10));
	}};
	
	Data user = UserU.getUser();
	public InforChange() {
		setting();
		UIManager.put("Label.font", new Font("맑은 고딕", 1, 15));
		setNorthPanel();
		setMainPanel();
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(change);
		}}, BorderLayout.SOUTH);
		add(borderPanel);
		
		change.addActionListener(e->{
			String nick = this.nick.getText();
			String p = this.pw.getText();
			String n = this.name.getText();
			String b = this.birth.getText();
			String i = this.id.getText();
			
			if(nick.isEmpty() || p.isEmpty() || n.isEmpty() || b.isEmpty() || i.isEmpty()) {
				getter.mg("입력하지 않은 항목이 있습니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			List<Data> list1 = Connections.select("SELECT u_nick FROM game_site.user where u_no != ?;", user.get(0));
			for(int s = 0; s < list1.size(); s++) {
				if(list1.get(s).get(0).toString().equals(nick)) {
					getter.mg("이미 존재하는 닉네임입니다.", JOptionPane.ERROR_MESSAGE);
					this.nick.setText("");
					return;
				}
			}
			
			List<Data> list2 = Connections.select("SELECT u_id FROM game_site.user where u_no != ?;", user.get(0));
			for(int s = 0; s < list2.size(); s++) {
				if(list2.get(s).get(0).toString().equals(i)) {
					getter.mg("이미 있는 아이디입니다..", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			if(i.length() > 10) {
				getter.mg("아이디란 글자는 최대 10글자까지 가능합니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getter.mg("등록이 완료 되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			
			Connections.update("update user set u_name = ?, u_id = ?, u_pw = ?, u_nick = ?, u_bd = ?, u_Disclosure = ? where u_no = ?", n,i, p, nick, b, 1, user.get(0));
			dispose();
			new MyHome(user.getInt(0));
		});
		setFrame("정보 수정", 600, 400);
	}

	private void setting() {
		nick.setText(user.get(4).toString());
		pw.setText(user.get(3).toString());
		name.setText(user.get(1).toString());
		birth.setText(user.get(5).toString());
		id.setText(user.get(2).toString());
		if(user.getInt(user.size() - 2) == 0) {
			publicCheck.setSelected(true);
		}else {
			privateCheck.setSelected(true);
		}
	}

	private void setMainPanel() {
		JPanel p = new JPanel(new GridLayout(2, 2, 15, 15));
		p.setBackground(Color.white);
		p.setBorder(createLineBorder(Color.black));
		
		JPanel namePanel = new JPanel(new GridLayout(2, 1));
		namePanel.setBackground(Color.white);
		
		namePanel.add(new JLabel("이름 :"));
		namePanel.add(name);
		
		JPanel birthPanel = new JPanel(new GridLayout(2, 1));
		birthPanel.setBackground(Color.white);
		
		birthPanel.add(new JLabel("생년월일 :"));
		birthPanel.add(birth);
		
		JPanel idPanel = new JPanel(new GridLayout(2, 1));
		idPanel.setBackground(Color.white);
		
		idPanel.add(new JLabel("아이디 :"));
		idPanel.add(id);
		
		JPanel PP = new JPanel(new GridLayout(2, 1));
		PP.setBackground(Color.white);
		
		PP.add(new JLabel("공개 여부 :"));
		PP.add(new JPanel(new GridLayout(0, 2)) {{
			setBackground(Color.white);
			add(publicCheck);
			add(privateCheck);
		}});
		
		p.add(namePanel);
		p.add(birthPanel);
		p.add(idPanel);
		p.add(PP);
		
		borderPanel.add(p);
	}

	private void setNorthPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(Color.white);
		
		panel.add(new JLabel(getter.getImage("userimage/" + user.getInt(0) + ".jpg", 200, 200)) {{
			setPreferredSize(new Dimension(200, 200));
			setBorder(createLineBorder(Color.black));
			setBackground(Color.white);
		}}, BorderLayout.WEST);
		
		JPanel gridPanel = new JPanel(new GridLayout(4, 1, 10, 10));
		gridPanel.setBackground(Color.white);
		gridPanel.setBorder(createEmptyBorder(10, 0, 0, 0));
		
		gridPanel.add(new JLabel("닉네임 :"));
		gridPanel.add(nick);
		gridPanel.add(new JLabel("PW :"));
		gridPanel.add(pw);
		
		panel.add(gridPanel);
		borderPanel.add(panel, BorderLayout.NORTH);
	}
	
	public static void main(String[] args) {
		new InforChange();
	}
}
