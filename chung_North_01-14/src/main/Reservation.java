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

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import com.sun.jdi.connect.spi.Connection;

public class Reservation extends JFrame{
	LocalDate nows = LocalDate.of(2025, 9, 10);
	LocalDate selectedDate;
	LocalTime selectedTime;
	
	JPanel mainPanel = new JPanel(new BorderLayout());
	JPanel timePanel = new JPanel(new BorderLayout());
	JButton seatSelect = new CustumButton("좌석조회");
	Font font = new Font("맑은 고딕", 1, 26);
	
	
	List<Data> times;
	List<LocalDate> dates = new ArrayList<>();
	List<JTextArea> ts = new ArrayList<>();
	List<JPanel> panels = new ArrayList<>();
	int m_no, selected, selectedTimeColor;
	boolean isFromMain;
	
	public Reservation(int m_no, boolean isFromMain) {
		
		this.m_no = m_no;
		this.isFromMain = isFromMain;
		setWestPanel();
		setMainPanel();
		
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		butPanel.setBorder(createEmptyBorder(10,0,0,0));
		butPanel.setBackground(Color.white);
		butPanel.add(seatSelect);
		
		mainPanel.add(butPanel, BorderLayout.SOUTH);
		mainPanel.add(new JScrollPane(timePanel));
		add(mainPanel);
		
		setAction();
		A_setFrame.setting(this, "예매", 600, 400);
	}
	
	private void setWestPanel() {
		JPanel westPanel = new JPanel(new BorderLayout(7,7));
		westPanel.setBackground(Color.white);
		
		JPanel nowsPanel = new JPanel(new GridLayout(0, 1, 5,5));
		nowsPanel.setBackground(Color.white);
		nowsPanel.setPreferredSize(new Dimension(150, 75));
		nowsPanel.add(new JLabel(nows.getYear() + "년", JLabel.LEFT) {{
			setFont(font);
		}});
		nowsPanel.add(new JLabel(nows.getMonthValue() + "월", JLabel.RIGHT) {{
			setFont(font);
		}});
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 3,3));
		gridPanel.setBackground(Color.white);
		
		String[] dayName = "월화수목금토일".split("");
		
		for (int i = 1; i <= nows.getMonth().maxLength(); i++) {
			LocalDate date = LocalDate.of(nows.getYear(), nows.getMonthValue(), i);
			JPanel p = new JPanel(new GridLayout(0, 2));
			
			p.add(new JLabel(dayName[date.getDayOfWeek().getValue()-1], JLabel.LEFT) {{
				setForeground(Color.black);
				setFont(font.deriveFont(15f));
			}});
			p.add(new JLabel(i + "일", JLabel.LEFT) {{
				setForeground(Color.black);
				setFont(font.deriveFont(15f));
			}});
			if(i < nows.getDayOfMonth()) {
				p.setBackground(Color.gray);
			}else {
				p.setBackground(Color.white);
				panels.add(p);
				dates.add(date);
			}
			gridPanel.add(p);
		}
		westPanel.add(new JScrollPane(gridPanel));
		westPanel.add(nowsPanel, BorderLayout.NORTH);
		add(westPanel, BorderLayout.WEST);
		
	}
	
	private void setMainPanel() {
		timePanel.removeAll();
		ts.clear();
		times = Connections.select("SELECT * FROM moviedb.schedule where m_no = ? and sc_date = ?", m_no, selectedDate);
		JPanel gridPanel = new JPanel(new GridLayout(5, 1, 2, 2));
		gridPanel.setBackground(Color.white);
		for(int i = 0; i < times.size(); i++) {
			Data time = times.get(i);
			Object obj = Connections.select("SELECT sum(r_people) FROM moviedb.reservation where m_no = ? and r_date = ? and r_time = ?;", m_no, selectedDate, time.get(4)).get(0).get(0);
			int seat = 81 - (obj != null ? Integer.parseInt(obj.toString()) : 0);
			JTextArea t = new JTextArea(time.get(2) + "관 (총 81석) \n" + time.get(4) + " " + seat + "석 남음") {{
				setFocusable(false);
				setCursor(getCursor().getDefaultCursor());
				setFont(font.deriveFont(14f));
				setBorder(createLineBorder(Color.black));
				setBackground(Color.white);
			}};
			ts.add(t);
			gridPanel.add(t);
		}
		
		for(int i = 0; i < ts.size(); i++) {
			final int index = i;
			ts.get(index).addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					ts.get(selectedTimeColor).setBackground(Color.white);
					ts.get(index).setBackground(Color.yellow);
					selectedTimeColor = index;
					selectedTime = LocalTime.parse(times.get(index).get(4).toString());
				}
			});
		}
		timePanel.add(gridPanel);
		revalidate();
		repaint();
	}
	
	private void setAction() {
		seatSelect.addActionListener(e->{
			if(selectedDate == null || selectedTime == null) {
				JOptionPane.showMessageDialog(null, "스케줄을 선택해주세요", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(null, "좌석예매 폼으로 이동하겠습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			new 상영관_배치도(Connections.select("SELECT * FROM moviedb.schedule where m_no = ? and sc_date = ? and sc_time = ?",m_no, selectedDate, selectedTime).get(0), isFromMain);
			dispose();
		});
		for(int i = 0; i < panels.size(); i++) {
			final int index = i;
			MouseAdapter m = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(selected != -1) panels.get(selected).setBackground(Color.white);
					panels.get(index).setBackground(Color.yellow);
					selected = index;
					selectedDate = dates.get(index);
					selectedTime = null;
					setMainPanel();
				}
			};
			panels.get(i).addMouseListener(m);
		}
	}
	public static void main(String[] args) {
		new Reservation(1, true);
	}
}
