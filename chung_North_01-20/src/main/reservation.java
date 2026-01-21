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

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class reservation extends JFrame {
	LocalDate nows = LocalDate.of(2025, 9, 10);
	Font font = new Font("맑은 고딕", 1, 24);
	
	JPanel wPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBorder(createLineBorder(Color.black));
		setBackground(Color.white);
	}};
	
	JPanel mainPanel = new JPanel(new GridLayout(4, 1, 3, 3)) {{
		setBorder(createLineBorder(Color.LIGHT_GRAY));
		setBackground(Color.white);
	}};
	JPanel centerPanel = new JPanel(new BorderLayout(15,15)) {{
		setBackground(Color.white);
	}};
	JButton seatSelected = new CustumButton("좌석조회");
	
	List<Data> times = new ArrayList<>();
	List<JPanel> panels = new ArrayList<>();
	List<Data> days = new ArrayList<>();
	List<JTextArea> textArea = new ArrayList<>();
	Data selectedTimeData;
	int m_no, selectedDay, selectedTime = -1;
	reservation(int m_no) {
		this.m_no = m_no;
		centerPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(seatSelected);
		}}, BorderLayout.SOUTH);
		centerPanel.add(mainPanel);
		setW_Panel();
		add(centerPanel);
		add(wPanel, BorderLayout.WEST);
		setAction();
		setFrame.setframe(this, "예매", 600, 400);
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		textArea.clear();
		if(!days.get(selectedDay).isEmpty())
			times = Connections.select("SELECT * FROM moviedb.schedule where m_no = ? and sc_date = ?", m_no, days.get(selectedDay).get(0));
		else times = new ArrayList<>();
		
		for(int i = 0; i < times.size(); i++) {
			Data d = times.get(i);
			List<Data> seatSize = Connections.select("SELECT sum(r_people) FROM moviedb.reservation where m_no = ? and r_date = ? and r_time = ?;", m_no, d.get(3), d.get(4));
			int seat = 81 - ((seatSize.get(0).get(0) == null) ? 0 : seatSize.get(0).getInt(0));
			JTextArea t = new JTextArea(d.getInt(2) + "관 (총 81석)\n" + d.get(4) + " " + seat + "석 남음");
			t.setFont(font.deriveFont(17f));
			t.setBorder(createLineBorder(Color.black));
			t.setFocusable(false);
			t.setCursor(getCursor().getDefaultCursor());
			
			textArea.add(t);
			mainPanel.add(t);
		}
		
		for(int i = 0; i < textArea.size(); i++) {
			final int index = i;
			textArea.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(textArea.get(index).getText().contains(" 0석")) {
						getter.mg("남은 자리가 없습니다.", JOptionPane.ERROR_MESSAGE);
						return;
					}
					textArea.get(selectedTime == -1 ? 0 : selectedTime).setBackground(Color.white);
					textArea.get(selectedTime = index).setBackground(Color.yellow);
					selectedTimeData = times.get(index);
				}
			});
		}
		revalidate();
		repaint();
	}
	
	private void setW_Panel() {
		JPanel ymPanel = new JPanel(new GridLayout(2, 1, 10, 10));
		ymPanel.setBackground(Color.white);
		ymPanel.setPreferredSize(new Dimension(150, 75));
		JLabel y = new JLabel(nows.getYear() + "년", JLabel.LEFT);
		y.setFont(font);
		
		JLabel m = new JLabel(nows.getMonthValue() + "월", JLabel.RIGHT);
		m.setFont(font);
		
		ymPanel.add(y);
		ymPanel.add(m);
		
		wPanel.add(ymPanel, BorderLayout.NORTH);
		JPanel panel = new JPanel(new GridLayout(0, 1, 3, 3)) {{
			setBackground(Color.white);
		}};
		String[] daysName = "월화수목금토일".split("");
		for(int i = 1; i <= nows.getMonth().maxLength(); i++) {
			LocalDate date = LocalDate.of(nows.getYear(), nows.getMonthValue(), i);
			
			JPanel p = new JPanel(new GridLayout(0, 2));
			p.setBackground(!date.isBefore(nows) ? Color.white : Color.gray);
			if(!date.isBefore(nows)) {
				panels.add(p);
				List<Data> d = Connections.select("SELECT sc_date FROM moviedb.schedule \r\n"
						+ "where m_no = ? and sc_date = ?\r\n"
						+ "group by sc_date;", m_no, date);
				Data datas = ((d == null || d.isEmpty()) ? new Data() : d.get(0));
				days.add(datas);
			}
			JLabel dayName = new JLabel(daysName[date.getDayOfWeek().getValue() - 1]);
			dayName.setForeground(Color.black);
			dayName.setFont(font.deriveFont(17f));
			
			JLabel day = new JLabel(i + "일");
			day.setForeground(Color.black);
			day.setFont(font.deriveFont(17f));
			
			p.add(dayName);
			p.add(day);
			panel.add(p);
		}
		wPanel.add(new JScrollPane(panel));
	}
	
	private void setAction() {
		for(int i = 0; i < panels.size(); i++) {
			final int index = i;
			panels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					
					panels.get(selectedDay).setBackground(Color.white);
					panels.get(selectedDay = index).setBackground(Color.yellow);
					selectedTime = -1;
					selectedTimeData = null;
					setMainPanel();
				}
			});
		}
		seatSelected.addActionListener(e->{
			if(selectedTimeData == null) {
				getter.mg("스케줄을 선택해주세요", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getter.mg("좌석예매 폼으로 이동하겠습니다.", JOptionPane.INFORMATION_MESSAGE);
			new 상영관_배치도(selectedTimeData);
			dispose();
		});
	}
	public static void main(String[] args) {
		new reservation(1);
	}
}
