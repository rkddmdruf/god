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

import static  javax.swing.BorderFactory.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class 예매 extends JFrame{
	LocalDate nowDate = LocalDate.of(2025, 9, 10);
	LocalTime nowTime = LocalTime.of(0, 0, 0);
	JPanel days = new JPanel(new GridLayout(0, 1, 5, 5));
	String[] dayName = "월,화,수,목,금,토,일".split(",");
	List<JPanel> panels = new ArrayList<>();
	List<Data> ds;
	List<LocalDate> dates = new ArrayList<>();
	Data schedule = new Data();
	int selectTimes = -1;
	int selectPanels = -1;
	JPanel 예약 = new JPanel(new BorderLayout());
	JButton seatSelect = new CustumButton("좌석 조회") {{
		setFont(new Font("맑은 고딕", 0, 11));
	}};
	JPanel timesPanel = new JPanel(new GridLayout(0, 1, 5,5));
	예매(int u_no){
		JPanel westPanel = new JPanel(new BorderLayout()){{
			setBorder(createLineBorder(Color.black));
		}};
		JPanel nowDatePanel = new JPanel(new BorderLayout());
		nowDatePanel.setPreferredSize(new Dimension(125, 60));
		
		nowDatePanel.add(new JLabel(nowDate.getYear() + "년", JLabel.LEFT) {{
			setFont(new Font("맑은 고딕", 1, 24));
		}}, BorderLayout.NORTH);
		nowDatePanel.add(new JLabel(nowDate.getMonthValue() + "월", JLabel.RIGHT) {{
			setFont(new Font("맑은 고딕", 1, 24));
		}}, BorderLayout.SOUTH);
		
		setDays();
		set예약Panel();
		westPanel.add(new JScrollPane(days));
		westPanel.add(nowDatePanel, BorderLayout.NORTH);
		add(westPanel, BorderLayout.WEST);
		
		seatSelect.addActionListener(e->{
			if(schedule.isEmpty()) {
				JOptionPane.showMessageDialog(null, "스케줄을 선택해주세요.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(null, "좌석예매 폼으로 이동하겠습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			new lineBfs(schedule);
			dispose();
		});
		new A_setFrame(this, "예매", 500, 300);
	}
	
	private void setTimesPanel() {
		timesPanel.removeAll();
		ds = Connections.select("SELECT * FROM moviedb.schedule where m_no = 6 and sc_date = ?;", dates.get(selectPanels));
		for(int i = 0; i < 4; i++) {
			Data d;
			if(i < ds.size()) {
				d = ds.get(i);
				JTextArea ttt = new JTextArea(d.get(2) + "관 (총 81석)\n" + d.get(4) + " 81석 남음");
				timesPanel.add(ttt);
				ttt.setBorder(createLineBorder(Color.black));
				ttt.setFocusable(false);
				ttt.setFont(new Font("맑은 고딕", 1, 20));
				ttt.setCursor(getCursor().getDefaultCursor());
			}else
				timesPanel.add(new JLabel());
		}
		
		for(int i = 0; i < ds.size(); i++) {
			int index = i;
			timesPanel.getComponent(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(selectTimes != -1) {
						timesPanel.getComponent(selectTimes).setBackground(Color.white);
					}
					schedule = ds.get(index);
					selectTimes = index;
					timesPanel.getComponent(selectTimes).setBackground(Color.yellow);
				}
			});
		}
	}
	
	
	
	private void set예약Panel() {
		예약.add(new JScrollPane(timesPanel){{
			setPreferredSize(new Dimension(0, 300 - 50));
		}}, BorderLayout.NORTH);
		예약.add(new JPanel(new FlowLayout(0, 0,FlowLayout.RIGHT)) {{
			setBorder(createEmptyBorder(25, 0, 0, 0));
			seatSelect.setVerticalAlignment(JLabel.BOTTOM);
			seatSelect.setHorizontalAlignment(JLabel.RIGHT);
			add(seatSelect);
		}}, BorderLayout.EAST);
		add(예약);
	}
	
	
	
	private void setDays() {
		System.out.println(nowDate.getMonth().maxLength());
		for(int i = 1; i <= nowDate.getMonth().maxLength(); i++) {
			LocalDate d = LocalDate.of(2025, 9, i);
			JPanel gPanel = new JPanel(new GridLayout(0, 2));
			gPanel.setBackground(d.isAfter(nowDate) ? Color.white : Color.gray);
			gPanel.add(new JLabel(dayName[d.getDayOfWeek().getValue()-1]) {{
				setFont(new Font("맑은 고딕", 1 , 16));
			}});
			gPanel.add(new JLabel(d.getDayOfMonth() + "") {{
				setFont(new Font("맑은 고딕", 1 , 16));
			}});
			if(d.isAfter(nowDate)) { 
				panels.add(gPanel);
				dates.add(d);
				days.add(panels.get(panels.size() - 1));
			}else
				days.add(gPanel);
		}
		
		for(int i = 0; i < panels.size(); i++) {
			int index = i;
			panels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(selectPanels != -1)
						panels.get(selectPanels).setBackground(Color.white);
					selectPanels = index;
					panels.get(index).setBackground(Color.yellow);
					schedule = null;
					selectTimes = -1;
					setTimesPanel();
					revalidate();
					repaint();
				}
			});
		}
	}
	
	public static void main(String[] args) {
		new 예매(1);
	}
}
