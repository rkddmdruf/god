package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class Reservation extends JFrame{
	Font font = new Font("맑은 고딕", 1, 24);
	LocalDate nows = LocalDate.of(2025, 9, 10);
	
	JPanel mainPanel  = new JPanel(new GridLayout(4, 1, 3, 3)) {{
		setBackground(Color.white);
		setBorder(createLineBorder(Color.lightGray));
	}};
	List<JPanel> dayPanels = new ArrayList<>();
	List<LocalDate> dayDateList = new ArrayList<>();
	List<Data> timesList = new ArrayList<>();
	List<JTextArea> taList = new ArrayList<>();
	JButton seatSelectBut = new CustumButton("좌석조회");
	int m_no, selectDay, selectTime = -1;
	
	Reservation(int m_no) {
		this.m_no = m_no;
		setW_Panel();
		
		JPanel panel = new JPanel(new BorderLayout(10,10));
		panel.setBackground(Color.white);
		
		panel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(seatSelectBut);
		}}, BorderLayout.SOUTH);
		panel.add(mainPanel);
		add(panel);
		setAction();
		SetFrame.setFrame(this, "예매", 600, 350);
	}

	private void setMainPanel() {
		mainPanel.removeAll();
		taList.clear();
		if(dayDateList.get(selectDay) == null) {
			revalidate();
			repaint();
			return;
		}
		timesList = Connections.select("select * from schedule where sc_date = ? and m_no = ?", dayDateList.get(selectDay), m_no);
		for(int i = 0; i < timesList.size(); i++) {
			List<Data> list = Connections.select("SELECT sum(r_people) FROM moviedb.reservation \r\n"
					+ "where m_no = ? and r_date = ? and r_time = ?;", m_no, timesList.get(i).get(3), timesList.get(i).get(4));
			int seat = 81 - (list.get(0).get(0) == null ? 0 : list.get(0).getInt(0));
			System.out.println(seat);
			JTextArea t = new JTextArea(timesList.get(i).getInt(2) + "관 (총 81석)\n" + timesList.get(i).get(4) + " " + seat + "석 남음");
			t.setFont(font.deriveFont(16f));
			t.setBorder(createLineBorder(Color.black));
			t.setFocusable(false);
			t.setCursor(getCursor().getDefaultCursor());
			
			taList.add(t);
			mainPanel.add(t);
		}
		
		for(int i = 0; i < taList.size(); i++) {
			final int index = i;
			taList.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(taList.get(index).getText().contains(" 0석")) return;
					taList.get(selectTime == -1 ? 0 : selectTime).setBackground(Color.white);
					taList.get(selectTime = index).setBackground(Color.yellow);
				}
			});
		}
		
		revalidate();
		repaint();
	}
	private void setAction() {
		for(int i = 0; i < dayPanels.size(); i++) {
			final int index = i;
			dayPanels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					dayPanels.get(selectDay).setBackground(Color.white);
					dayPanels.get(selectDay = index).setBackground(Color.yellow);
					selectTime = -1;
					setMainPanel();
				}
			});
		}
		seatSelectBut.addActionListener(e->{
			if(selectTime == -1) {
				getter.mg("스케줄을 선택해주세요.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getter.mg("좌석예매 폼으로 이동하겠습니다.", JOptionPane.INFORMATION_MESSAGE);
			new 상영관_배치도(timesList.get(selectTime));
			dispose();
			return;
		});
	}
	private void setW_Panel() {
		JPanel wPanel = new JPanel(new BorderLayout(7,7));
		wPanel.setBackground(Color.white);
		wPanel.setBorder(createLineBorder(Color.black));
		wPanel.setPreferredSize(new Dimension(150, 0));
		
		JPanel ymPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		ymPanel.setBackground(Color.white);
		JLabel y = new JLabel(nows.getYear() + "년", JLabel.LEFT);
		y.setFont(font);
		
		JLabel m = new JLabel(nows.getMonthValue() + "월", JLabel.RIGHT);
		m.setFont(font);
		
		ymPanel.add(y);
		ymPanel.add(m);
		
		JPanel daysGridPanel = new JPanel(new GridLayout(0, 1, 3, 3));
		daysGridPanel.setBackground(Color.white);
		String[] dayName = "월화수목금토일".split("");
		for(int i = 1; i <= nows.getMonth().maxLength(); i++) {
			LocalDate date = LocalDate.of(nows.getYear(), nows.getMonthValue(), i);
			
			JPanel p = new JPanel(new GridLayout(0, 2));
			p.setBackground(!nows.isAfter(date) ? Color.white : Color.gray);
			
			p.add(new JLabel(dayName[date.getDayOfWeek().getValue() - 1]) {{
				setFont(font.deriveFont(1, 16));
				setForeground(Color.black);
			}});
			p.add(new JLabel(i + "일") {{
				setFont(font.deriveFont(1, 16));
				setForeground(Color.black);
			}});
			
			if(!nows.isAfter(date)) {
				dayPanels.add(p);
				List<Data> list = Connections.select("select sc_date from schedule where sc_date = ? and m_no = ?", date, m_no);
				dayDateList.add(list == null || list.isEmpty() ? null : LocalDate.parse(list.get(0).get(0).toString()));
			}
			daysGridPanel.add(p);
		}
		wPanel.add(ymPanel, BorderLayout.NORTH);
		wPanel.add(new JScrollPane(daysGridPanel));
		add(wPanel, BorderLayout.WEST);
	}
	
	public static void main(String[] args) {
		new Reservation(1);
	}
}
