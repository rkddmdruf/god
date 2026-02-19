package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import realOrm.*;
import realOrm.Db.*;
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
	
	JRadioButton publicCheck = new JRadioButton("공개");
	JRadioButton privateCheck = new JRadioButton("비공개");
	
	JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 10, 10, 10));
	}};
	
	User user = UserU.getUser();
	

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
		setAction();
		setFrame("정보 수정", 600, 400);
	}
	
	private void setAction() {
		privateCheck.addActionListener(e->{
			privateCheck.setSelected(true);
			publicCheck.setSelected(false);
		});
		publicCheck.addActionListener(e->{
			publicCheck.setSelected(true);
			privateCheck.setSelected(false);
		});
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
			/*List<Data> list1 = Connections.select("SELECT u_nick FROM game_site.user where u_no != ?;", user.getU_no());
			List<Data> list2 = Connections.select("SELECT u_id FROM game_site.user where u_no != ?;", user.getU_no());
			for(int s = 0; s < list2.size(); s++) {
				if(list2.get(s).get(0).toString().equals(i)) {
					getter.mg("이미 있는 아이디입니다..", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			*/
			
			if(!Entity2.select(User.U_NICK.getNev()) .from(User.class)
					.where(User.U_NO.notEq(user.getU_no()), User.U_NICK.eq(nick))
					.toList() .isEmpty()) {
				this.nick.setText("");
				getter.mg("이미 존재하는 닉네임입니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			///////////////밑에 두개는 똑같은데 쿼리문 쓴거 안쓴거 차이 내생각에는 비슷한거 같다?
			if(!Entity2.select(User.U_ID.getNev()).from(User.class)
					.where(User.U_NO.notEq(user.getU_no()), User.U_ID.eq(i))
					.toList().isEmpty()) {
				getter.mg("이미 있는 아이디입니다..", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(!Connections.select("SELECT u_id FROM game_site.user where u_no != ? and u_id = ?;"
					, user.getU_no(), i)
					.isEmpty()) {
				getter.mg("이미 있는 아이디입니다..", JOptionPane.ERROR_MESSAGE);
				return;
			}
			////////////////
			if(i.length() > 10) {
				getter.mg("아이디란 글자는 최대 10글자까지 가능합니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getter.mg("등록이 완료 되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			
			int selectCheck = publicCheck.isSelected() ? 0 : 1;
			
			User users = new User(user.getU_no(), user.getU_name(), user.getU_id(), user.getU_pw(), user.getU_name(), user.getU_bd(), user.getU_price(), user.getU_Disclosure());
			users.update();
			//Connections.update("update user set u_name = ?, u_id = ?, u_pw = ?, u_nick = ?, u_bd = ?, u_Disclosure = ? where u_no = ?", n,i, p, nick, b, selectCheck, user.getU_no());
			UserU.setUser(users);
			dispose();
			new MyHome(user.getU_no());
		});
	}
	
	private void setting() {
		nick.setText(user.getU_nick().toString());
		pw.setText(user.getU_pw().toString());
		name.setText(user.getU_name().toString());
		birth.setText(user.getU_bd().toString());
		id.setText(user.getU_id().toString());
		if(user.getU_Disclosure() == 0) {
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
			add(publicCheck);
			add(privateCheck);
			setBackground(Color.white);
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
		
		panel.add(new JLabel(getter.getImage("userimage/" + user.getU_no() + ".jpg", 200, 200)) {{
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
