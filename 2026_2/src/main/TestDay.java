package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

import static  javax.swing.BorderFactory.*;
import javax.swing.*;

import utils.*;

public class TestDay extends CFrame{
	JPanel northPanel = new JPanel(new BorderLayout(5, 5));
	JComboBox<String> cb = new JComboBox<String>() {{
		setPreferredSize(new Dimension(100, 25));
		addItem("전체");
		for(Data d : Connections.select("select * from category")) {
			addItem(d.getString(1));
		}
	}};
	JTextField tf = new JTextField();
	JButton select = new JButton("조회하기") {{
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	
	JPanel scPanel = new JPanel(new GridLayout(0, 1, 5, 5)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(scPanel) {{
		setBackground(Color.white);
	}};
	List<Data> list = Connections.select("SELECT schedule.*, certi.* FROM lecture.schedule\r\n"
			+ "join certi on schedule.cno = certi.cno\r\n"
			+ "where cname like ?"
			+ "order by schedule.exam_date, schedule.start_date;", "%%");
	List<Data> 수강신청한_자격증 = Connections.select("SELECT cno FROM lecture.course_registration\r\n"
			+ "where uno = ?;", User.getUser().get(0));
	TestDay() {
		borderPanel.setBorder(createEmptyBorder(5, 12, 6, 12));
		setNorthPanel();
		setMainPanel();
		borderPanel.setLayout(new BorderLayout(10, 10));
		borderPanel.add(northPanel, BorderLayout.NORTH);
		borderPanel.add(sc);
		setFrame("시럼일정", 600, 350);
	}
	
	private void setMainPanel() {
		scPanel.removeAll();
		list = Connections.select("SELECT schedule.*, certi.* FROM lecture.schedule\r\n"
					+ "join certi on schedule.cno = certi.cno\r\n"
					+ "where cname like ?"
					+ "order by schedule.exam_date, schedule.start_date;", "%%");
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			p.setBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(10,10,10,10)));
			
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					g.drawImage(new ImageIcon("datafiles/certification/" + list.get(index).get(1) + ".png").getImage(), 0, 0, getWidth(), getHeight(), null);
					super.paintComponent(g);
				}
			};
			img.setPreferredSize(new Dimension(200,150));
			img.setBorder(createLineBorder(Color.black));
			boolean bools = false;
			for(Data data : 수강신청한_자격증) {
				if(list.get(index).getInt(1) == data.getInt(0)) {
					bools = true;
				}
			}
			boolean bool = bools;
			JPanel inforPanel = new JPanel(new BorderLayout());
			String cgname = Connections.select("select * from category where cgno = ?", list.get(index).getString(17).split(",")[0]).get(0).getString(1);
			inforPanel.add(new JTextArea(list.get(index).getString(6) + " " + list.get(index).getString(11) + "급 " + list.get(index).getString(3) + "~" + LocalDate.parse(list.get(index).getString(3)).plusDays(7)
					+ "\n신청일자: " + list.get(index).getString(2) + "~" + LocalDate.parse(list.get(index).getString(2)).plusDays(5)
					+ "\n분류:" + cgname) {{
						setFocusable(false);
						setCursor(getCursor().getDefaultCursor());
						setBorder(createEmptyBorder(30, 0, 30, 0));
					}});//17
			inforPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
				setBackground(Color.white);
				
				add(new JButton("신청하러 가기") {{
					setEnabled(bool);
					setForeground(Color.white);
					setBackground(Color.blue);
				}});
			}}, BorderLayout.SOUTH);
			p.add(inforPanel);
			p.add(new JPanel(new BorderLayout()) {{
				setBackground(Color.white);
				setBorder(createEmptyBorder(5,10,5,10));
				add(img);
			}}, BorderLayout.WEST);
			
			scPanel.add(p);
		}
	}

	private void setNorthPanel() {
		northPanel.setBackground(Color.white);
		northPanel.add(cb, BorderLayout.WEST);
		northPanel.add(tf);
		northPanel.add(select, BorderLayout.EAST);
	}
	
	public static void main(String[] args) {
		new TestDay();
	}
}
