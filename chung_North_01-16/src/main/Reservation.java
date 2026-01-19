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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class Reservation extends JFrame{
	Font font = new Font("맑은 고딕", 1, 24);
	LocalDate nows = LocalDate.of(2025, 9, 10);
	JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
	JPanel timesPanel = new JPanel(new GridLayout(5, 1, 3, 3)) {{
		setBackground(Color.white);
		setBorder(createLineBorder(Color.LIGHT_GRAY));
	}};
	JButton seatSelect = new CustumButton("좌석조회");
	List<JPanel> panels = new ArrayList<>();
	List<JTextArea> timesText = new ArrayList<>();
	List<Data> timeList;
	
	LocalDate selectDay;
	LocalTime selectLocalTime;
	
	int m_no = 0, selectTime = 0;
	Reservation(int m_no){
		this.m_no = m_no;
		setWest();
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(seatSelect);
		}}, BorderLayout.SOUTH);
		mainPanel.add(timesPanel);
		mainPanel.setBackground(Color.white);
		add(mainPanel);
		setAction();
		seatSelect.addActionListener(e->{
			if(selectLocalTime == null) {
				JOptionPane.showMessageDialog(null, "스케줄을 선택해주세요", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(null, "좌석예매 폼으로 이동하겠습니다.","정보", JOptionPane.INFORMATION_MESSAGE);
			new 상영관_배치도(timeList.get(selectTime));
			dispose();
			return;
		});
		SetFrames.setframe(this, "예매", 600, 450);
	}
	
	private void setMainPanel() {
		timesPanel.removeAll();
		timesText.clear();
		JLabel getLabel = ((JLabel) panels.get(panelsColor).getComponent(1));
		int day = Integer.parseInt(getLabel.getText().substring(0, getLabel.getText().toCharArray().length - 1));
		selectDay = LocalDate.of(nows.getYear(), nows.getMonthValue(), day);
		timeList = Connections.select("SELECT * FROM moviedb.schedule where m_no = ? and sc_date = ?;", m_no, selectDay);
		for(int i = 0; i < timeList.size(); i++) {
			Data time = timeList.get(i);
			Data re_seat = Connections.select("select sum(r_people) from reservation where m_no = ? and r_date = ? and r_time = ?", m_no, selectDay, time.get(4)).get(0);
			
			int seat = 81 - (re_seat.get(0) != null ? re_seat.getInt(0) : 0);
			
			JTextArea t = new JTextArea(time.get(2) + "관 (총 81석)\n" + time.get(4) + " " + seat + "석 남음");
			t.setFont(font.deriveFont(1, 16));
			t.setFocusable(false);
			t.setCursor(getCursor().getDefaultCursor());
			t.setBorder(createLineBorder(Color.black));
			
			timesText.add(t);
			timesPanel.add(t);
		}
		
		for(int i = 0; i < timeList.size(); i++) {
			final int index = i;
			timesText.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(timesText.get(index).getText().contains(" 0석")) {
						JOptionPane.showMessageDialog(null, "남은 자리가 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
						return;
					}
					timesText.get(selectTime).setBackground(Color.white);
					timesText.get(selectTime = index).setBackground(Color.yellow);
					selectLocalTime = LocalTime.parse(timeList.get(index).get(4).toString());
				}
			});
		}
		revalidate();
		repaint();
	}
	
	private void setWest() {
		JPanel wPanel = new JPanel(new BorderLayout(5,5));
		wPanel.setBorder(createLineBorder(Color.black));
		wPanel.setPreferredSize(new Dimension(150, 0));
		wPanel.setBackground(Color.white);
		
		JPanel ymPanel = new JPanel(new GridLayout(2, 1, 3, 3));
		ymPanel.setBackground(Color.white);
		JLabel y = new JLabel(nows.getYear() + "년");
		y.setFont(font);
		
		JLabel m = new JLabel(nows.getMonthValue() + "월", JLabel.RIGHT);
		m.setFont(font);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 3, 3));
		gridPanel.setBackground(Color.white);
		
		String[] dayName = "월화수목금토일".split("");
		for(int i = 1; i <= nows.getMonth().maxLength(); i++) {
			LocalDate date = LocalDate.of(nows.getYear(), nows.getMonthValue(), i);
			JPanel p = new JPanel(new GridLayout(0, 2));
			if(nows.getDayOfMonth() <= date.getDayOfMonth()) { panels.add(p); }
			p.setBackground(nows.getDayOfMonth() <= date.getDayOfMonth() ? Color.white : Color.gray);
			
			JLabel nameLabel = new JLabel(dayName[date.getDayOfWeek().getValue()-1]);
			nameLabel.setFont(font.deriveFont(1,17));
			nameLabel.setForeground(Color.black);
			
			JLabel dayLabel = new JLabel(date.getDayOfMonth() + "일");
			dayLabel.setFont(font.deriveFont(1, 17));
			dayLabel.setForeground(Color.black);
			
			p.add(nameLabel);
			p.add(dayLabel);
			
			gridPanel.add(p);
		}
		
		ymPanel.add(y);
		ymPanel.add(m);
		wPanel.add(new JScrollPane(gridPanel) {{
			getVerticalScrollBar().setUnitIncrement(20);
		}});
		wPanel.add(ymPanel, BorderLayout.NORTH);
		add(wPanel, BorderLayout.WEST);
		
	}
	
	int panelsColor = 0;
	private void setAction() {
		for(int i = 0; i < panels.size(); i++){
			final int index = i;
			panels.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					panels.get(panelsColor).setBackground(Color.white);
					panels.get(panelsColor = index).setBackground(Color.yellow);
					selectLocalTime = null;
					setMainPanel();
				}
			});
		}
	}
	public static void main(String[] args) {
		new Reservation(1);
	}
}
