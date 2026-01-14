package main;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

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
public class Reservation2 extends JFrame{
	Font font = new Font("맑은 고딕", 1, 25);
	LocalDate nowDate = LocalDate.of(2025, 9, 10);
	LocalTime nowTime = LocalTime.of(0,0,0);
	
	JPanel timesPanel = new JPanel(new GridLayout(0, 1, 5, 5));
	JButton seatSelect = new CustumButton("좌석조회");
	JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
	List<JPanel> panels = new ArrayList<>();
	List<Data> datas;
	List<JTextArea> textAreaList = new ArrayList<>();
	
	int u_no, m_no, selectedDay = -1, selectedTime = -1;;
	boolean isFromMain;
	public Reservation2(int u_no, int m_no, boolean isFromMain) {
		this.u_no = u_no;
		this.m_no = m_no;
		this.isFromMain = isFromMain;
		
		setWestPanel();
		
		butPanel.add(seatSelect);
		butPanel.setBackground(Color.white);
		
		timesPanel.setBorder(createLineBorder(Color.black));
		timesPanel.setBackground(Color.white);
		
		JPanel panel = new JPanel(new BorderLayout(15,15));
		panel.setBackground(Color.white);
		panel.add(butPanel, BorderLayout.SOUTH);
		panel.add(timesPanel);
		
		add(panel);
		setAction();
		new A_setFrame(this, "예매", 600, 400);
	}
	
	private void setWestPanel() {
		JPanel westPanel = new JPanel(new BorderLayout(10,10));
		westPanel.setBorder(createLineBorder(Color.black));
		westPanel.setBackground(Color.white);
		
		JPanel northPanel = new JPanel(new GridLayout(0, 1, 10, 10));
		northPanel.setBackground(Color.white);
		northPanel.setPreferredSize(new Dimension(150, 70));
		northPanel.add(new JLabel(nowDate.getYear() + "년", JLabel.LEFT) {{
			setFont(font);
		}});
		northPanel.add(new JLabel(nowDate.getMonthValue() + "월", JLabel.RIGHT) {{
			setFont(font);
		}});
		
		JPanel daysPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		daysPanel.setBackground(Color.white);
		
		String[] dayName = "월화수목금토일".split("");
		for(int i = 1; i <= nowDate.getMonth().maxLength(); i++) {
			LocalDate d = LocalDate.of(2025, 9, i);
			JPanel p = new JPanel(new GridLayout(0, 2));
			p.setBackground(d.getDayOfMonth() >= nowDate.getDayOfMonth() ? Color.white : Color.gray);
			if(d.getDayOfMonth() >= nowDate.getDayOfMonth()) {
				panels.add(p);
			}
			p.add(new JLabel(dayName[d.getDayOfWeek().getValue()-1], JLabel.LEFT) {{
				setForeground(Color.black);
				setFont(font.deriveFont(1, 15f));
			}});
			p.add(new JLabel(i + "일", JLabel.LEFT) {{
				setForeground(Color.black);
				setFont(font.deriveFont(1, 15f));
			}});
			
			daysPanel.add(p);
		}
		JScrollPane sc = new JScrollPane(daysPanel);
		sc.setBackground(Color.white);
		
		westPanel.add(sc);
		westPanel.add(northPanel, BorderLayout.NORTH);
		add(westPanel, BorderLayout.WEST);
	}
	
	private void setMainPanel() {
		timesPanel.removeAll();
		int day = Integer.parseInt(((JLabel) panels.get(selectedDay).getComponent(1)).getText().substring(0, 2));
		datas = Connections.select("SELECT * FROM moviedb.schedule where m_no = ? and sc_date = ?;", m_no, LocalDate.of(nowDate.getYear(), nowDate.getMonthValue(), day));
		for(int i = 0; i < datas.size(); i++) {
			final int index = i;
			Data data = datas.get(i);
			Object ds = Connections.select("SELECT sum(r_people) FROM moviedb.reservation where m_no = ? and r_date = ? and r_time = ?;"
					, m_no, data.get(3), data.get(4)).get(0).get(0);
			int seat = 81;
			if(ds != null) {
				seat -= Integer.parseInt(ds.toString());
			}
			JTextArea p = new JTextArea(data.get(2) + "관 (총 81석)\n" + data.get(4) + " " + seat + "석 남음");
			p.setFont(font.deriveFont(1, 17));
			p.setCursor(getCursor().getDefaultCursor());
			p.setFocusable(false);
			p.setBorder(createLineBorder(Color.black));
			textAreaList.add(p);
			timesPanel.add(p);
		}
		for(int i = 0; i < 4 - datas.size(); i++) 
			timesPanel.add(new JLabel(""));
		setTextAreaAction();
		revalidate();
		repaint();
	}
	
	private void setTextAreaAction() {
		for(int i = 0; i < textAreaList.size(); i++) {
			final int index = i;
			textAreaList.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(textAreaList.get(index).getText().contains(" 0석")) {
						JOptionPane.showMessageDialog(null, "남은 자리가 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(selectedTime != -1) textAreaList.get(selectedTime).setBackground(Color.white);
					textAreaList.get(index).setBackground(Color.yellow);
					selectedTime = index;
				}
			});
		}
	}
	private void setAction() {
		seatSelect.addActionListener(e->{
			if(selectedTime == -1) {
				JOptionPane.showMessageDialog(null, "스케줄을 선택해주세요.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(null, "좌석예매 폼으로 이동하겠습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			new 상영관_배치도2(u_no, datas.get(selectedTime), true);
			dispose();
		});
		for(int i = 0; i < panels.size(); i++) {
			final int index = i;
			panels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(selectedDay != -1)
						panels.get(selectedDay).setBackground(Color.white);
					panels.get(index).setBackground(Color.yellow);
					selectedDay = index;
					selectedTime = -1;
					setMainPanel();
				}
			});
		}
	}
	public static void main(String[] args) {
		new Reservation2(1, 1, true);
	}
}
