package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class 예매 extends JFrame{

	LocalDate noW = LocalDate.of(2025, 9, 10);
	List<JPanel> dayPanelList = new ArrayList<>();
	List<List<Object>> list = new ArrayList<>();
	JButton seatSelect = new JButton("좌석 조회") {{
		setForeground(Color.white);setBackground(Color.blue);
	}};
	JPanel mainPanel;
	JPanel scrollPanel = new JPanel(new GridLayout(0, 1, 10, 10)) {{
		setBorder(BorderFactory.createLineBorder(Color.black));
	}};
	int selectDay = -1;
	int u_no = 0, m_no = 0;;
	int selectTime = -1;
	예매(int u_no, int m_no){
		this.u_no = u_no; this.m_no = m_no;
		setdesing();
		mainPanel = new JPanel(new BorderLayout(15, 15));
		mainPanel.add(scrollPanel);
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
			add(seatSelect);
		}}, BorderLayout.SOUTH);
		add(mainPanel);
		setFrame();
		revalidate();
		repaint();
	}
	private void setdesing() {
		
		JPanel wPanel = new JPanel(new BorderLayout());
		wPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel datePanel = new JPanel(new BorderLayout());
		datePanel.setPreferredSize(new Dimension(130, 60));
		Font font = new Font("맑은 고딕", 1, 22);
		datePanel.add(new JLabel(noW.getYear() + "년", JLabel.LEFT) {{
			setFont(font);
		}}, BorderLayout.NORTH);
		datePanel.add(new JLabel(noW.getMonthValue() + "월", JLabel.RIGHT) {{
			setFont(font);			
		}}, BorderLayout.SOUTH);
		
		JPanel dayPanel = new JPanel(new GridLayout(0, 1, 10, 10));
		for(int i = 1; i <= noW.getMonth().maxLength(); i++) {
			LocalDate date = LocalDate.of(2025, 9, i);
			String week = "";
			switch (date.getDayOfWeek().getValue()) {
				case 1: {week = "월"; break;}
				case 2: {week = "화"; break;}
				case 3: {week = "수"; break;}
				case 4: {week = "목"; break;}
				case 5: {week = "금"; break;}
				case 6: {week = "토"; break;}
				case 7: {week = "일"; break;}
			}
			JPanel panel = new JPanel(new GridLayout(0, 2)) {{
				setBackground(noW.getDayOfMonth() > date.getDayOfMonth() ? Color.GRAY : Color.white);
			}};
			panel.add(new JLabel(week) {{
				setFont(new Font("맑은 고딕", 1, 16));
			}});
			panel.add(new JLabel(date.getDayOfMonth() + "일") {{
				setFont(new Font("맑은 고딕", 1, 16));
			}});
			dayPanelList.add(panel);
			dayPanel.add(dayPanelList.get(dayPanelList.size() - 1));
		}
		wPanel.add(new JScrollPane(dayPanel));
		wPanel.add(datePanel, BorderLayout.NORTH);
		add(wPanel, BorderLayout.WEST);
	}
	
	private void setMainPanel() {
		scrollPanel.removeAll();
		list = setList("SELECT * FROM moviedb.schedule where m_no = ? and sc_date = ?;", m_no, LocalDate.of(2025, 9, selectDay));
		
		for(int i = 0; i < list.size(); i++) {final int index = i;
			
			scrollPanel.add(new JTextArea(list.get(i).get(2) + "관 (총 81석)\n"
					+ list.get(i).get(4) + " 81석 남음") {{
						setFont(new Font("맑은 고딕", 1, 17));
						setBorder(BorderFactory.createLineBorder(Color.black));
						setFocusable(false);
						MouseAdapter m  = new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								for(int j = 0; j < scrollPanel.getComponentCount(); j++) {
									scrollPanel.getComponent(j).setBackground(Color.white);
								}
								setBackground(Color.yellow);
								selectTime = index + 1;
								System.out.println(selectTime);
							}
						};
						addMouseListener(m);
			}});
		}
		for(int i = 0; i < 5 - list.size(); i++) {
			scrollPanel.add(new JLabel(""));
		}
		revalidate();
		repaint();
	}
	
	private void setAction() {
		for(int i = noW.getDayOfMonth(); i <= noW.getMonth().maxLength(); i++) {final int index = i;
			dayPanelList.get(i- 1).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for(int i = noW.getDayOfMonth(); i <= noW.getMonth().maxLength(); i++) 
						dayPanelList.get(i- 1).setBackground(Color.white);
					dayPanelList.get(index - 1).setBackground(Color.yellow);
					selectTime = 0;
					selectDay = index;
					System.out.println(selectTime);
					setMainPanel();
				}
			});
		}
		seatSelect.addActionListener(e->{
			if(selectTime == 0) {
				JOptionPane.showMessageDialog(null, "스케줄을 선택해주세요", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(null, "좌석예매 폼으로 이동하겠습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
			new 상영관_배치도(Integer.parseInt(list.get(selectTime).get(2).toString()));
			dispose();
		});
	}
	
	private List<List<Object>> setList(String string, Object...val) {
		List<List<Object>> list = new ArrayList<>();
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=Asia/Seoul&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement st = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				st.setObject(i+1, val[i]);
			}
			ResultSet re = st.executeQuery();
			while(re.next()) {
				List<Object> l = new ArrayList<>();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++)
					l.add(re.getObject(i+1));
				list.add(l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private void setFrame() {
		setAction();
		setTitle("예매");
		setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		int w = 500 + 16; int h = 350 + 39;
		setBounds(960 - (w / 2), 540 - (h / 2), w, h);
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args) {
		new 예매(1, 1);
	}
}
