package main;

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
import java.time.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class Reservation extends JFrame{
	Font font = new Font("맑은 고딕", 1, 24);
	LocalDate nowDate = LocalDate.of(2025, 9, 10);
	LocalTime nowTime = LocalTime.of(0, 0, 0);
	
	LocalDate selectDate = null;
	LocalTime selectTime = null;
	
	JPanel westPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
		setPreferredSize(new Dimension(150, 0));
		setBorder(createLineBorder(Color.black));
	}};
	
	JButton seatSelectBut = new CustumButton("좌석조회");
	JPanel timePanel = new JPanel(new GridLayout(0, 1, 5, 5)) {{
		setBackground(Color.white);
		setBorder(createLineBorder(Color.lightGray));
	}};
	JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
		add(timePanel);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		p.setBackground(Color.white);
		p.setBorder(createEmptyBorder(10,0,0,0));
		p.add(seatSelectBut);
		add(p, BorderLayout.SOUTH);
	}};
	
	List<JPanel> panels = new ArrayList<>();
	List<JTextArea> textAreaList = new ArrayList<>();
	List<LocalDate> dates = new ArrayList<>();
	int u_no, m_no, selectDay = -1;
	boolean isFromMain = false;
	public Reservation(int u_no, int m_no, boolean isFromMain) {
		this.isFromMain = isFromMain;
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) { new MovieInfor(u_no, m_no, isFromMain); }
		});
		this.u_no = u_no;
		this.m_no = m_no;
		setWestPanel();
		add(mainPanel);
		add(westPanel, BorderLayout.WEST);
		setAction();
		new A_setFrame(this, "예매",600,450);
	}
	
	private void setMainPanel() {
		textAreaList.clear();
		timePanel.removeAll();
		List<Data> list = new ArrayList<>();
		if(selectDate != null)
			list = Connections.select("SELECT * FROM moviedb.schedule where m_no = ? and sc_date = ?;", m_no, selectDate);
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);
			int seatSize = 81;
			List<Data> d = Connections.select("SELECT sum(r_people) FROM moviedb.reservation where m_no = ? and r_date = ? and r_time = ?;",m_no, list.get(i).get(3), list.get(i).get(4));
			if(d.get(0).get(0) != null) {
				seatSize -= Integer.parseInt(d.get(0).get(0).toString());
			}
			JTextArea ta = new JTextArea(data.get(2) + "관 (총 81석)\n" + data.get(4) + " " + seatSize + "석 남음");
			ta.setFont(font.deriveFont(1, 16));
			ta.setBorder(createLineBorder(Color.black));
			ta.setCursor(getCursor().getDefaultCursor());
			ta.setFocusable(false);
			textAreaList.add(ta);
			timePanel.add(ta);
		}
		for(int i = 0; i < 5 - list.size(); i++) {
			timePanel.add(new JLabel(" "));
		}
		
		textAreaAction(list);
		revalidate();
		repaint();
	}
	
	private void textAreaAction(List<Data> list) {
		for(int i = 0; i < textAreaList.size(); i++) {
			int index = i;
			textAreaList.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for (JTextArea t : textAreaList)
						t.setBackground(Color.white);
					if(textAreaList.get(index).getText().contains(" 0석")) {
						JOptionPane.showMessageDialog(null, "남은 자리가 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
						return;
					}
					selectTime = LocalTime.parse(list.get(index).get(4).toString());
					textAreaList.get(index).setBackground(Color.yellow);
				}
			});
		}
	}
	private void setWestPanel() {
		JPanel westNorthPanel = new JPanel(new GridLayout(0, 1));
		westNorthPanel.setBackground(Color.white);
		
		westNorthPanel.add(new JLabel(nowDate.getYear() + "년", JLabel.LEFT) {{
			setFont(font);
		}});
		westNorthPanel.add(new JLabel(nowDate.getMonthValue() + "월", JLabel.RIGHT) {{
			setFont(font);
		}});
		
		JPanel daysPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		daysPanel.setBackground(Color.white);
		
		String[] dayName = "월 화 수 목 금 토 일".split(" ");
		
		for (int i = 1; i <= nowDate.getMonth().maxLength(); i++) {
			JPanel p = new JPanel(new GridLayout(0, 2));
			p.setBackground(i >= nowDate.getDayOfMonth() ? Color.white : Color.gray);
			LocalDate day = LocalDate.of(nowDate.getYear(), nowDate.getMonthValue(), i);
			if(i >= nowDate.getDayOfMonth()) {
				panels.add(p);
				dates.add(day);
			}
			
			p.add(new JLabel(dayName[day.getDayOfWeek().getValue() - 1]) {{
				setFont(font.deriveFont(1, 16));
				setForeground(Color.black);
			}});
			p.add(new JLabel(i + "일") {{
				setFont(font.deriveFont(1, 16));
				setForeground(Color.black);
			}});
			daysPanel.add(p);
		}
		
		westPanel.add(new JScrollPane(daysPanel) {{
			setBackground(Color.white);
		}});
		westPanel.add(westNorthPanel, BorderLayout.NORTH);
		
	}
	
	private void setAction() {
		seatSelectBut.addActionListener(e->{
			if(selectTime == null) {
				JOptionPane.showMessageDialog(null, "스케줄을 선택해주세요.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(null, "좌석예매 폼으로 이동하겠습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			new 상영관_배치도(u_no, m_no, isFromMain, selectDate, selectTime);
			dispose();
		});
		for (int i = 0; i < panels.size(); i++) {
			int index = i;
			panels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (selectDay == index) return;
					if (selectDay != -1)
						panels.get(selectDay).setBackground(Color.white);
					panels.get(index).setBackground(Color.yellow);
					selectDay = index;
					selectDate = dates.get(index);
					selectTime = null;
					setMainPanel();
				}
			});
		}
	}
	
	public static void main(String[] args) {
		new Reservation(1, 1, true);
	}
}
