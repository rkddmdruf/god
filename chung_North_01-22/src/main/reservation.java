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
import javax.swing.border.LineBorder;

public class reservation extends JFrame{
	Font font = new Font("맑은 고딕", 1, 26);
	LocalDate nows = LocalDate.of(2025, 9, 10);
	JPanel mainPanel = new JPanel(new GridLayout(4, 1, 3, 3)) {{
		setBackground(Color.white);
		setBorder(createLineBorder(Color.lightGray));
	}};
	Data selectDate = null;
	JButton seatSelectBut = new CustumButton("좌석조회");
	List<Data> timesList = new ArrayList<>();
	List<LocalDate> daysList = new ArrayList<>();
	List<JPanel> panels = new ArrayList<>();
	int m_no, selectDay, selectTime = -1;
	public reservation(int m_no) {
		this.m_no = m_no;
		JPanel panel = new JPanel(new BorderLayout(10,10));
		panel.add(mainPanel);
		panel.setBackground(Color.white);
		panel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(seatSelectBut);
		}}, BorderLayout.SOUTH);
		add(panel);
		setWPanel();
		setAction();
		seatSelectBut.addActionListener(e->{
			if(selectDate == null) {
				getter.mg("스케줄을 선택해주세요", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getter.mg("좌석예매 폼으로 이동 하겠습니다.", JOptionPane.INFORMATION_MESSAGE);
		});
		setFrame.setframe(this, "예매", 600, 400);
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		List<JTextArea> tList = new ArrayList<>();
		if(daysList.get(selectDay) != null)
			timesList = Connections.select("select * from schedule where m_no = ? and sc_date = ?", m_no, daysList.get(selectDay));
		else timesList = new ArrayList<>();
		for(int i = 0; i < timesList.size(); i++) {
			Data d = timesList.get(i);
			List<Data> lists = Connections.select("SELECT sum(r_people) FROM moviedb.reservation where r_date = ? and r_time = ? and m_no = ?;", d.get(3), d.get(4), d.get(1));
			int seat = 81 - (lists.get(0).get(0) != null ? lists.get(0).getInt(0) : 0);
			JTextArea t = new JTextArea(d.getInt(2) + "관 (총 81석)\n" + d.get(4) + " " + seat + "석 남음");
			t.setBorder(createLineBorder(Color.black));
			t.setFocusable(false);
			t.setFont(font.deriveFont(17f));
			t.setCursor(getCursor().getDefaultCursor());
			
			tList.add(t);
			mainPanel.add(t);
		}
		
		for(int i = 0; i < tList.size(); i++) {
			final int index = i;
			tList.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(tList.get(index).getText().contains(" 0석")) {
						getter.mg("남은 자리가 없습니다.", JOptionPane.ERROR_MESSAGE);
						return;
					}
					tList.get(selectTime != -1 ? selectTime : 0).setBackground(Color.white);
					tList.get(selectTime = index).setBackground(Color.yellow);
					selectDate = timesList.get(index);
				}
			});
		}
		revalidate();
		repaint();
	}
	
	private void setAction() {
		for(int i = 0; i < panels.size(); i++) {
			final int index = i;
			panels.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					panels.get(selectDay).setBackground(Color.white);
					panels.get(selectDay = index).setBackground(Color.yellow);
					selectDate = null;
					selectTime = -1;
					setMainPanel();
				}
			});
		}
	}
	private void setWPanel() {
		JPanel wPanel = new JPanel(new BorderLayout(5,5));
		wPanel.setBorder(createLineBorder(Color.black));
		wPanel.setBackground(Color.white);
		wPanel.setPreferredSize(new Dimension(160, 55));
		
		JPanel ymPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		ymPanel.setBackground(Color.white);
		
		ymPanel.add(new JLabel(nows.getYear() + "년", JLabel.LEFT) {{
			setFont(font);
		}});
		ymPanel.add(new JLabel(nows.getMonthValue() + "월", JLabel.RIGHT) {{
			setFont(font);
		}});
		
		wPanel.add(ymPanel, BorderLayout.NORTH);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 3, 3));
		gridPanel.setBackground(Color.white);
		String[] dayname = "월화수목금토일".split("");
		for(int i = 1; i <= nows.getMonth().maxLength(); i++) {
			LocalDate date = LocalDate.of(nows.getYear(), nows.getMonthValue(), i);
			JPanel p = new JPanel(new GridLayout(0, 2));
			if(!nows.isAfter(date)) {
				List<Data> list = Connections.select("select sc_date from schedule where sc_date = ? and m_no = ?", date, m_no);
				daysList.add((list == null || list.isEmpty()) ? null : LocalDate.parse(list.get(0).get(0).toString()));
				panels.add(p);
			}
			p.setBackground(nows.isAfter(date) ? Color.gray : Color.white);
			
			p.add(new JLabel(dayname[date.getDayOfWeek().getValue() - 1]) {{
				setForeground(Color.black);
				setFont(font.deriveFont(17f));
			}});
			p.add(new JLabel(i + "일") {{
				setForeground(Color.black);
				setFont(font.deriveFont(17f));
			}});
			
			gridPanel.add(p);
		}
		wPanel.add(new JScrollPane(gridPanel));
		add(wPanel, BorderLayout.WEST);
	}
	public static void main(String[] args) {
		new reservation(1);
	}
}
