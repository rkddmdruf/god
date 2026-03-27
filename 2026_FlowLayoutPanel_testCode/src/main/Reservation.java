package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import utils.*;

public class Reservation extends CFrame{
	LocalDate now = LocalDate.now();
	LocalTime time = LocalTime.now();
	
	
	List<JPanel> dayPanels = new ArrayList<>();
	List<LocalDate> dayList = new ArrayList<>();
	Data selectData;
	
	
	
	JButton selectBut = new CButton("좌석 조회");
	JPanel mainPanel = new JPanel(new GridLayout(0,1, 5, 5)) {{
		setBackground(Color.white);
	}};
	JPanel panel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
		add(new JScrollPane(mainPanel));
		add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(selectBut);
		}}, BorderLayout.SOUTH);
	}};
	
	int selectDay = 0;
	int selectTime = -1;
	int m_no;
	public Reservation(int m_no) {
		this.m_no = m_no;
		borderPanel.setLayout(new BorderLayout());
		setWPanel();
		borderPanel.add(panel);
		
		selectBut.addActionListener(e->{
			if(selectTime == -1) {
				getter.err("스케줄을 선택해주세요");
				return;
			}
			if(dayList.get(selectDay).isEqual(now))
				if(time.isAfter(LocalTime.parse(selectData.getString(4)))) {
					getter.err("잘못된 시간대");
					return;
				}

			getter.infor("좌석예매 폼으로 이동 하겠습니다.");
			new SeatSelect(selectData);
			dispose();
			return;
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//new MovieInfor(m_no);
				dispose();
				return;
			}
		});
		
		dayPanels.get(0).setBackground(Color.yellow);
		setMainPanel();
		setFrame("예매", 625, 400);
	}
	List<Data> list;
	private void setMainPanel() {
		mainPanel.removeAll();
		String query = "select * from schedule where m_no = ? and sc_date = ? ";
		List<JPanel> panels = new ArrayList<>();
		list = Connections.select(query, m_no, dayList.get(selectDay));
		
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			Data data = list.get(i);
			JPanel p = new JPanel(new BorderLayout(20, 20));
			p.setBackground(Color.white);
			p.setBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(5, 0, 0, 0)));
			p.add(new JLabel(data.getString(2) + "관 (총 81석)"), BorderLayout.NORTH);
			List<Data> seatList = Connections.select("SELECT r_setname FROM moviedb.reservation where m_no = ? and r_date = ? and r_time = ?", m_no, dayList.get(selectDay), data.get(4));
			System.out.println(seatList);
			int seat = 81 - 0;
			for(int j = 0; j < seatList.size(); j++) {
				seat -= seatList
						.get(j)
						.getString(0)
						.split(",")
						.length;
			}
			p.add(new JLabel(data.getString(4) + " 총 " + seat + "석 남음"));
			panels.add(p);
			mainPanel.add(p);
		}
		
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			panels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					panels.get(selectTime == -1 ? 0 : selectTime).setBackground(Color.white);
					panels.get(selectTime = index).setBackground(Color.yellow);
					selectData = list.get(index);
				}
			});
		}
		for(int i = 0; i < 4 - list.size(); i++) {
			mainPanel.add(new JLabel(""));
		}
		revalidate();
		repaint();
	}
	
	private void setWPanel() {

		JPanel wPanel = new JPanel(new BorderLayout());
		wPanel.setBackground(Color.white);
		wPanel.setBorder(createLineBorder(Color.black));
		wPanel.setPreferredSize(new Dimension(175, 0));
		
		JPanel ymPanel = new JPanel(new BorderLayout());
		ymPanel.setBackground(Color.white);
		UIManager.put("Label.font", new Font("맑은 고딕", 1, 22));
		ymPanel.add(new JLabel(now.getYear() + "년"), BorderLayout.NORTH);
		ymPanel.add(new JLabel(now.getMonthValue() + "월", JLabel.RIGHT), BorderLayout.SOUTH);
		
		wPanel.add(ymPanel, BorderLayout.NORTH);
		
		JPanel dayPanel = new JPanel(new GridLayout(0, 1, 3, 3));
		dayPanel.setBackground(Color.white);
		
		UIManager.put("Label.font", new Font("맑은 고딕", 1, 15));
		String[] dayName = "월화수목금토일".split("");
		for(int i = 1; i <= now.lengthOfMonth(); i++) {
			LocalDate date = LocalDate.of(now.getYear(), now.getMonthValue(), i);
			JPanel p = new JPanel(new GridLayout(0, 2));
			p.setBackground(now.isAfter(date) ? Color.gray : Color.white);
			
			p.add(new JLabel(dayName[date.getDayOfWeek().getValue() - 1]));
			p.add(new JLabel(i + "일"));
			
			if(!now.isAfter(date)) {
				dayList.add(date);
				dayPanels.add(p);
			}else {
				p.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						getter.err("현재시간 이후만 선택 가능");
					}
				});
			}
			dayPanel.add(p);
		}
		
		for(int i = 0; i < dayPanels.size(); i++) {
			final int index = i;
			dayPanels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					dayPanels.get(selectDay).setBackground(Color.white);
					dayPanels.get(selectDay = index).setBackground(Color.yellow);
					selectData = null;
					selectTime = -1;
					setMainPanel();
				}
			});
		}
		wPanel.add(new JScrollPane(dayPanel));
		borderPanel.add(wPanel, BorderLayout.WEST);
	}
	public static void main(String[] args) {
		new Reservation(1);
	}
}
