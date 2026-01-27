package main;

import utils.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class Reservation extends JFrame{
	LocalDate nows = LocalDate.of(2025, 9, 10);
	Font font = new Font("맑은 고딕", 1, 27);
	JPanel wPanel = new JPanel(new BorderLayout(10,10));
	
	JPanel timePanel = new JPanel(new GridLayout(4, 1, 3, 3));
	JPanel mainPanel = new JPanel(new BorderLayout(10,10));
	JButton selectSeat = new CustumButton("좌석조회");
	
	List<Data> list;
	List<LocalDate> dayList = new ArrayList<>();
	List<JPanel> days = new ArrayList<>();
	int m_no, selectDay, selectTime = - 1;
	public Reservation(int m_no){
		this.m_no = m_no;
		setwPanel();
		
		mainPanel.setBackground(Color.white);
		
		timePanel.setBackground(Color.white);
		timePanel.setBorder(createLineBorder(Color.lightGray));
		
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(selectSeat);
		}}, BorderLayout.SOUTH);
		mainPanel.add(timePanel);
		
		add(mainPanel);
		add(wPanel, BorderLayout.WEST);
		
		setAction();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new MovieInfor(m_no);
				dispose();
			}
		});
		setFrame.setframe(this, "예매", 600, 400);
	}
	
	private void setPanel() {
		timePanel.removeAll();
		List<JTextArea> textList = new ArrayList<>();
		list = Connections.select("SELECT * FROM moviedb.schedule where m_no = ? and sc_date = ?;", m_no, dayList.get(selectDay));
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			List<Data> seatSize = Connections.select("SELECT sum(r_people) FROM moviedb.reservation where m_no = ? and r_time = ? and r_date = ?;", m_no, data.get(4), data.get(3));
			int seat = 81;
			if(seatSize.get(0).get(0) != null) {
				seat -= seatSize.get(0).getInt(0);
			}
			JTextArea t = new JTextArea(data.get(2) + "관 (총 81석)\n" + data.get(4) + " " + seat + "석 남음");
			t.setFocusable(false);
			t.setCursor(getCursor().getDefaultCursor());
			t.setFont(font.deriveFont(18f));
			t.setBorder(createLineBorder(Color.black));
			
			textList.add(t);
			timePanel.add(t);
		}
		
		for(int i = 0; i < textList.size(); i++) {
			final int index = i;
			textList.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(textList.get(index).getText().contains(" 0석")) {
						getter.mg("남은 자리가 없습니다.", JOptionPane.ERROR_MESSAGE);
						return;
					}
					textList.get(selectTime != -1 ? selectTime : 0).setBackground(Color.white);
					textList.get(selectTime = index).setBackground(Color.yellow);
				}
			});
		}
		
		revalidate();
		repaint();
	}
	
	private void setAction() {
		selectSeat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(selectTime == -1) {
					getter.mg("스케줄을 선택해주세요", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				getter.mg("좌석예매 폼으로 이동 하겠습니다.", JOptionPane.INFORMATION_MESSAGE);
				new 상영관_배치도(list.get(selectTime));
				dispose();
				return;
			}
		});
		for(int i = 0; i < days.size(); i++) {
			final int index = i;
			days.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					days.get(selectDay).setBackground(Color.white);
					days.get(selectDay = index).setBackground(Color.yellow);
					selectTime = -1;
					setPanel();
				}
			});
		}
	}
	
	private void setwPanel() {
		wPanel.setBackground(Color.white);
		wPanel.setBorder(createLineBorder(Color.black));
		
		JPanel ymPanel = new JPanel(new GridLayout(2, 1, 10,10));
		ymPanel.setBackground(Color.white);
		ymPanel.setPreferredSize(new Dimension(150, 75));
		
		JLabel y = new JLabel(nows.getYear() + "년");
		y.setFont(font);
		
		JLabel m = new JLabel(nows.getMonthValue() + "월", JLabel.RIGHT);
		m.setFont(font);
		
		ymPanel.add(y);
		ymPanel.add(m);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 3, 3));
		gridPanel.setBackground(Color.white);
		
		String[] dayName = "월화수목금토일".split("");
		for(int i = 1; i <= nows.getMonth().maxLength(); i++) {
			LocalDate date = LocalDate.of(nows.getYear(), nows.getMonthValue(), i);
			
			JPanel p = new JPanel(new GridLayout(0, 2));
			p.setBackground(!nows.isAfter(date) ? Color.white : Color.gray);
			if(!nows.isAfter(date)) {
				days.add(p);
				List<Data> list = Connections.select("SELECT sc_date FROM moviedb.schedule where m_no = ? and sc_date = ? group by sc_date;", m_no, date);
				dayList.add(list.isEmpty() ? null : LocalDate.parse(list.get(0).get(0).toString()));
			}
			JLabel dayNameLabel = new JLabel(dayName[date.getDayOfWeek().getValue() - 1]);
			dayNameLabel.setForeground(Color.black);
			dayNameLabel.setFont(font.deriveFont(15f));
			
			JLabel day = new JLabel(date.getDayOfMonth() + "일");
			day.setForeground(Color.black);
			day.setFont(font.deriveFont(15f));
			
			p.add(dayNameLabel);
			p.add(day);
			
			gridPanel.add(p);
		}
		wPanel.add(new JScrollPane(gridPanel));
		wPanel.add(ymPanel, BorderLayout.NORTH);
	}
	
	public static void main(String[] args) {
		new Reservation(1);
	}
}
