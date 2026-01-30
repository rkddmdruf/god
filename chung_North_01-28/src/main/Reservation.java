package main;

import javax.swing.*;

import utils.*;
import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reservation extends CFrame{
	Font font = new Font("맑은 고딕", 1, 25);
	LocalDate nows = LocalDate.of(2025, 9, 10);
	
	JPanel mainPanel = new JPanel(new GridLayout(4, 1, 3, 3)) {{
		setBackground(Color.white);
		setBorder(createLineBorder(Color.LIGHT_GRAY));
	}};
	
	JButton but = new CustumButton("좌석 조회");
	
	List<JTextArea> textList = new ArrayList<>();
	List<JPanel> panels = new ArrayList<>();
	List<LocalDate> daysList = new ArrayList<>();
	
	Data selectData;
	int m_no, selectDay, selectTime;
	public Reservation(int m_no) {
		this.m_no = m_no;
		setWPanel();
		
		JPanel p = new JPanel(new BorderLayout(10,10));
		p.setBackground(Color.white);
		
		p.add(mainPanel);
		p.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(but);
		}}, BorderLayout.SOUTH);
		
		add(p);
		setAction();
		setFrame("예매", 600, 400);
	}

	private void setAction() {
		for(int i = 0; i < panels.size(); i++) {
			final int index = i;
			panels.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					panels.get(selectDay).setBackground(Color.white);
					panels.get(selectDay = index).setBackground(Color.yellow);
					selectData = null;
					setMainPanel();
				}
			});
		}
		but.addActionListener(e->{
			if(selectData == null) {
				getter.mg("스케줄을 선택해주세요.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getter.mg("좌석예매 폼으로 이동하겠습니다.", JOptionPane.INFORMATION_MESSAGE);
			new 상영관_배치도(selectData);
		});
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		textList.clear();
		List<Data> list = Connections.select("select * from schedule where sc_date = ? and m_no = ?", daysList.get(selectDay), m_no);
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			
			List<Data> seatSzie = Connections.select("SELECT sum(r_people) FROM moviedb.reservation where m_no = ? and r_date = ? and r_time = ?;", m_no, data.get(3), data.get(4));
			int seat = 81 - (seatSzie.get(0).get(0) == null ? 0 : seatSzie.get(0).getInt(0));
			
			JTextArea t = new JTextArea(data.getInt(2) + "관 (총 81석)\n" + data.get(4) + " " + seat + "석 남음");
			t.setBorder(createLineBorder(Color.black));
			t.setFocusable(false);
			t.setCursor(Cursor.getDefaultCursor());
			t.setFont(font.deriveFont(20f));
			
			textList.add(t);
			mainPanel.add(t);
		}
		
		for (int i = 0; i < textList.size(); i++) {
			final int index = i;
			textList.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(textList.get(index).getText().contains(" 0석")) {
						getter.mg("남은 자리가 없습니다.", JOptionPane.ERROR_MESSAGE);
						return;
					}
					textList.get(selectTime).setBackground(Color.white);
					textList.get(selectTime = index).setBackground(Color.yellow);
					selectData = list.get(index);
				}
			});
		}
		revalidate();
		repaint();
	}
	
	private void setWPanel() {
		JPanel wPanel = new JPanel(new BorderLayout(10,10));
		wPanel.setBackground(Color.white);
		wPanel.setBorder(createLineBorder(Color.black));
		wPanel.setPreferredSize(new Dimension(160, 0));
		JPanel ymPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		ymPanel.setBackground(Color.white);
		
		JLabel y = new JLabel(nows.getYear() + "년");
		y.setFont(font);
		
		JLabel m = new JLabel(nows.getMonthValue() + "월", JLabel.RIGHT);
		m.setFont(font);
		
		ymPanel.add(y);
		ymPanel.add(m);
		
		wPanel.add(ymPanel, BorderLayout.NORTH);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 3, 3));
		gridPanel.setBackground(Color.white);
		
		String[] dayName = "월화수목금토일".split("");
		for(int i = 1; i <= nows.getMonth().maxLength(); i++) {
			LocalDate date = LocalDate.of(nows.getYear(), nows.getMonthValue(), i);
			
			JPanel p = new JPanel(new GridLayout(0, 2));
			p.setBackground(!nows.isAfter(date) ? Color.white : Color.gray);
			
			JLabel dayNameLabel = new JLabel(dayName[date.getDayOfWeek().getValue() - 1]);
			dayNameLabel.setFont(font.deriveFont(1, 15));
			
			JLabel dayLabel = new JLabel(date.getDayOfMonth() + "일");
			dayLabel.setFont(font.deriveFont(1, 15));
			
			p.add(dayNameLabel);
			p.add(dayLabel);
			
			
			if(!nows.isAfter(date)) {
				List<Data> list = Connections.select("SELECT * FROM moviedb.schedule where sc_date = ? and m_no = ?;", date, m_no);
				daysList.add(list.isEmpty() ? null : date);
				panels.add(p);
			}
			gridPanel.add(p);
		}
		
		wPanel.add(new JScrollPane(gridPanel));
		add(wPanel, BorderLayout.WEST);
	}
	
	public static void main(String[] args) {
		new Reservation(1);
	}
}
