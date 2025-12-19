package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class 예약 extends BaseFrame{
	LocalDateTime nows = LocalDateTime.of(2025, 9, 10,0,0,0);
	JPanel borderPanel = new cp(new BorderLayout(), null, null);
	JPanel datePanel = new cp(new BorderLayout(), sp.line, null).size(150, 0);
	JPanel dayPanel = new cp(new GridLayout(0, 1,5,5), null, null);
	JScrollPane sc = new JScrollPane(dayPanel);
	List<LocalDate> dateList = new ArrayList<LocalDate>();
	List<JPanel> dayPanelList = new ArrayList<>();
	List<JTextArea> movieInforPanelList = new ArrayList<>() {{add(new JTextArea());}};
	List<Row> movie = new ArrayList<>();
	JPanel mainPanel = new cp(new BorderLayout(), sp.line, null);
	JButton seatSerch = new cb("좌석조회").fontColor(Color.white).BackColor(sp.color);
	int day = -1, time = -1,mno = 0;
	예약(int mno){
		this.mno = mno;
		setFrame("예매", 600, 400, ()->{});
	}
	@Override
	protected void desing() {
		datePanel.add(new cp(new BorderLayout(), sp.em(10, 0, 10, 0), null) {{
			add(new cl(nows.getYear() + "년").setHo(JLabel.LEFT).font(sp.font(1, 25)), sp.n);
			add(new cl(nows.getMonthValue() + "월").setHo(JLabel.RIGHT).font(sp.font(1, 25)), sp.s);
		}}, sp.n);
		setPanel();
		datePanel.add(sc);
		borderPanel.add(mainPanel);
		JPanel butPanel = new cp(new BorderLayout(), sp.em(10, 0, 0, 0), null);
		butPanel.add(seatSerch, sp.e);
		borderPanel.add(butPanel, sp.s);
		add(datePanel, sp.w);
		add(borderPanel);
	}

	private void setPanel() {
		for(int i = 1; i <= nows.getMonth().maxLength(); i++) {
			LocalDate date = LocalDate.of(2025, 9, i);
			String week = "";
			switch (date.getDayOfWeek().getValue()) {
				case 1: {week = "월"; break;}
				case 2: {week = "화"; break;}
				case 3: {week = "수"; break;}
				case 4: {week = "목"; break;}
				case 5: {week = "금"; break;}
				case 6: {week = "토"; break;}
				case 7: {week = "일"; break;}
			}
			JPanel panel = new cp(new GridLayout(0, 2), null, nows.getDayOfMonth() > date.getDayOfMonth() ? Color.GRAY : null);
			panel.add(new cl(week).font(sp.font(1, 16)));
			panel.add(new cl(date.getDayOfMonth() + "일").font(sp.font(1, 16)));
			if(!(nows.getDayOfMonth() > date.getDayOfMonth())) {
				dateList.add(date);
				dayPanelList.add(panel);
			}
			dayPanel.add(panel);
		}
	}
	
	private void setMainPanel() {
		movieInforPanelList.clear();
		mainPanel.removeAll();
		JPanel northPanel = new cp(new GridLayout(0,1,5,5), null, null);
		movie = Query.select("SELECT * FROM moviedb.schedule "
				+ "where m_no = ? and date(sc_date) = ?"
				+ "order by sc_date, sc_time;", mno, dateList.get(day));//영화 예매 시간 몇관인지
		for(int i = 0; i < movie.size(); i++) {final int index = i;
			List<Row> list = Query.select("SELECT * FROM moviedb.reservation \r\n"
					+ "where m_no = ? and r_date = ? and r_time = ?;", mno, movie.get(i).get(3), movie.get(i).get(4));// 예약있는지
			int peple = 0;
			if(!list.isEmpty()) 
				for(int j = 0; j < 1; j++) { peple = peple + list.get(j).getInt(5); }
			JTextArea t = new ca(movie.get(i).getString(2) + "관 (총 81석)\n" + movie.get(i).getString(4) + " " + (81 - peple) + "석 남음\n").font(sp.font(1, 17)).setting();
			t.setBorder(sp.line);
			northPanel.add(t);
		}
		mainPanel.add(northPanel, sp.n);
		RePaint();
	}
	
	private void setAction() {
		JPanel p = (JPanel) mainPanel.getComponent(0);
		for(int i = 0; i < p.getComponentCount(); i++) {final int index = i;
			p.getComponent(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JTextArea t = (JTextArea) p.getComponent(index);
					if(t.getText().contains(" 0석")) {
						sp.err("남은 자리가 없습니다.");
						return;
					}
					time = index;
					for(int i = 0; i < p.getComponentCount(); i++) {
						p.getComponent(i).setBackground(e.getComponent() == p.getComponent(i) ? Color.yellow : Color.white);
					}
				}
			});
		}
	}
	@Override
	protected void action() {
		seatSerch.addActionListener(e->{
			if(time == -1) {
				sp.err("스케줄을 선택해주세요.");
				return;
			}
			new seatChoice(movie.get(time).getInt(0));
			sp.Infor("좌석예매 폼으로 이동하겠습니다.");
		});
		for(int i = 0; i < dayPanelList.size(); i++) {final int index = i;
			dayPanelList.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					day = index;
					time = -1;
					for(JPanel p : dayPanelList) {
						p.setBackground(Color.white);
						if(p == dayPanelList.get(index)) {
							dayPanelList.get(index).setBackground(Color.yellow);
						}
					}
					setMainPanel();
					setAction();
				}
			});
		}
		
	}
	public static void main(String[] args) {
		new 예약(1);
	}

}
