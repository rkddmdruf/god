package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class seatChoice extends BaseFrame{

	JPanel centerPanel = new sp.cp(new BorderLayout(45, 45), sp.em(0,80, 0, 80), null) {{
		add(new cl("스크린") {{
			setOpaque(true);
			setBackground(Color.gray);
		}}.setBorders(sp.line).setHo(JLabel.CENTER).font(sp.font(1, 14)), sp.n);
	}};
	JPanel southPanel = new cp(new BorderLayout(), BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black),null).size(0, 150);
	JPanel mainPanel = new sp.cp(new BorderLayout(50,50), sp.em(10, 10, 10, 10), null);
	List<List<JPanel>> seatPanel = new ArrayList<List<JPanel>>();
	Row row = new Row();
	char c = 65;
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
		setMovie();
	}

	private void setMovie() {
		southPanel.add(new cl(sp.getImg("datafiles/movies/" + row.getInt(1) + ".jpg", 112, 142)).setBorders(sp.em(5, 5, 3, 5)), sp.w);
		JTextArea t = new ca("이름: " + Query.select("select m_name from movie where m_no = ?", row.get(1)).get(0).getString(0) +"\n"
				+ "일시: " + row.getString(3)).font(sp.font(1, 20)).setting();
		southPanel.add(t);
	}
	private void seatSetting() {
		JPanel west = new cp(new GridLayout(9, 2, 5, 5), null, null);
		JPanel center = new cp(new GridLayout(9, 2, 5, 5), null, null);
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
			seatPanel.add(lists);
			System.out.println("");
		}
	}
	@Override
	protected void action() {
		for(int i = 65; i < 65 + 9; i++) { final int index = i;
			for(int j = 1; j <= 9; j++) { final int jndex = j;
				seatPanel.get(i - 65).get(j-1).addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						char c = (char) index;
						System.out.println(c + "" + jndex);
					}
				});
			}
		}
	}
	
	public static void main(String[] args) {
		new seatChoice(399);
	}
}
