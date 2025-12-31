package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class seatChoice extends BaseFrame{

	JComboBox<String> peple = new JComboBox<String>() {{
		for(int i = 1; i <= 10; i++) addItem(i+"");
		setPreferredSize(new Dimension(70, 25));
	}};
	JPanel centerPanel = new sp.cp(new BorderLayout(45, 45), sp.em(0,80, 0, 80), null) {{
		add(new cl("스크린") {{
			setOpaque(true);
			setBackground(Color.gray);
		}}.setBorders(sp.line).setHo(JLabel.CENTER).font(sp.font(1, 14)), sp.n);
	}};
	JPanel southPanel = new cp(new BorderLayout(), BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black),null).size(0, 150);
	JPanel mainPanel = new sp.cp(new BorderLayout(50,50), sp.em(10, 10, 10, 10), null);
	List<List<JPanel>> seatList = new ArrayList<List<JPanel>>();
	Row row = new Row();
	int pepleCheck = 0;
	String seatString = "";
	JTextArea pay = new ca("좌석 결제").font(sp.font(1, 20)).setting();
	JLabel money = new cl("15000원").font(sp.font(1, 20)).setHo(JLabel.LEFT);
	seatChoice(int sc_no){
		row = Query.select("SELECT * FROM schedule where sc_no = ?;", sc_no).get(0);
		setFrame("좌석 예매", 725,680, ()->{});
	}
	@Override
	protected void desing() {
		System.out.println(sp.user);
		centerPanel.add(mainPanel);
		add(centerPanel);
		add(southPanel, sp.s);
		seatSetting();
		setSouthPanel();
	}

	private void setSouthPanel() {
		southPanel.add(new cl(sp.getImg("datafiles/movies/" + row.getInt(1) + ".jpg", 112, 142)).setBorders(sp.em(5, 5, 3, 5)), sp.w);
		JTextArea t = new ca("이름: " + Query.select("select m_name from movie where m_no = ?", row.get(1)).get(0).getString(0) +"\n"
				+ "일시: " + row.getString(3)).font(sp.font(1, 20)).setting();
		JPanel p = new cp(new FlowLayout(FlowLayout.LEFT), null,null);
		p.add(new cl("인원수: ").font(sp.font(1, 20)));
		p.add(peple);
		JPanel panel = new cp(new BorderLayout(), null, null);
		panel.add(t, sp.n);
		panel.add(p, sp.c);
		
		southPanel.add(panel);
		JPanel p1 = new cp(new BorderLayout(5,5), null, null);
		setMoney();
		p1.add(money, sp.n);
		p1.add(pay);
		pay.setPreferredSize(new Dimension(125, 0));
		pay.setBackground(sp.color);
		pay.setForeground(Color.white);
		pay.setBorder(sp.em(30,40,30,30));
		southPanel.add(p1, sp.e);
	}
	
	private void setMoney() {
		int discount = sp.getAge(LocalDate.parse(sp.user.getString(4))) < 19 ? 10 : (sp.getBirthday(LocalDate.parse(sp.user.getString(4))) ? 20 : 1);
		int i = (int) (((pepleCheck) * 15000) - (((pepleCheck) * 15000)  * (discount / 100.0)));
		money.setText(i + "원");
	}
	private void seatSetting() {
		JPanel west = new cp(new GridLayout(9, 2, 5, 5), null, null);
		JPanel center = new cp(new GridLayout(9, 2, 5, 5), sp.em(0, 18, 0, 18), null);
		JPanel east = new cp(new GridLayout(9, 2, 5, 5), null, null);
		mainPanel.add(west, sp.w);
		mainPanel.add(center);
		mainPanel.add(east, sp.e);
		for(int i = 65; i < 65+9; i++) {final int index = i;
			List<JPanel> lists = new ArrayList<JPanel>();
			for(int j = 1; j <= 9; j++) {final int jndex = j;
				JPanel p = new cp(null,null,null) {{
					setPreferredSize(new Dimension(40,40));
					char c = (char) index;
					System.out.println(c);
					JLabel seat = new cl(c +""+ jndex).fontColor(Color.white).setHo(JLabel.CENTER);
					seat.setBounds(0, 0, 40, 35);
					add(seat);
					JLabel l = new cl(sp.getImg("datafiles/icon/좌석.png", 40,40)).setBorders(sp.line);
					l.setBounds(0,0,40,40);
					add(l);
				}};
				lists.add(p);
				(j <= 2 ? west : (j >= 8 ? east : center))
					.add(lists.get(lists.size()-1));
			}
			seatList.add(lists);
		}
	}
	@Override
	protected void action() {
		pay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(sp.user.getInt(5) < Integer.parseInt(money.getText().substring(0, money.getText().length() -1))) {
					sp.err("잔액이 부족합니다.");
					return;
				}
				int result = JOptionPane.showConfirmDialog(null, "결제하시겠습니까?", "결제", JOptionPane.YES_NO_OPTION);
				if(result != JOptionPane.YES_OPTION) {
					return;
				}
				sp.Infor("결제가 완료되었습니다.");
				Query.update("update user set u_price = ? where u_no = ?;",
						sp.user.getInt(5) - Integer.parseInt(money.getText().substring(0, money.getText().length() -1)), sp.user.get(0));
				Query.update("insert into reservation values(0, ?, ?, ?, ?, ?, ?, ?);",
						sp.user.get(0), row.get(1), seatString, Integer.parseInt(money.getText().substring(0, money.getText().length() -1)),
						sp.user.get(0), pepleCheck, row.get(4), row.get(3));
				sp.user = Query.select("select * from user where u_no = ?", sp.user.get(0)).get(0);
				System.out.println(sp.user);
				new Main();
				dispose();
			}
		});
		peple.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				pepleCheck = 0;
				seatString = "";
				for(List<JPanel> p : seatList) {
					for(JPanel p2 : p) {
						p2.setBackground(Color.white);
					}
				}
				setMoney();
			}
		});
		for(int i = 65; i < 65 + 9; i++) { final int index = i;
			for(int j = 1; j <= 9; j++) { final int jndex = j;
				seatList.get(i - 65).get(j-1).addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						char c = (char) index;
						if(peple.getSelectedIndex() + 1 > pepleCheck && !seatString.contains(c + "" + jndex)) {
							System.out.println(c + "" + jndex);
							seatString = seatString + c + jndex+",";
							pepleCheck++;
							setMoney();
							seatList.get(index - 65).get(jndex - 1).setBackground(Color.LIGHT_GRAY);
						}
						
					}
				});
			}
		}
	}
	
	public static void main(String[] args) {
		new seatChoice(399);
	}
}
