package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.*;

import utils.*;

public class 강의 extends CFrame{

	int cno;
	List<Data> list;
	Data certi;
	Data cr;
	boolean infor = false;
	public 강의(int cno) {
		this.cno = cno;
		
		cr = Connections.select("SELECT * FROM lecture.course_registration where cno = ? and uno = ?;", cno, User.getUser().get(0)).get(0);
		certi = Connections.select("select * from certi where cno = ?", cno).get(0);
		borderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel nPanel = new JPanel(new BorderLayout());
		nPanel.setBackground(Color.white);
		
		JPanel wPanel = new JPanel(new GridLayout(2, 1, 2, 2));
		wPanel.setBackground(Color.white);
		wPanel.add(new JLabel(certi.getString(1) + " " + certi.getString(6) + "급",JLabel.CENTER));
		wPanel.add(new JLabel(ChronoUnit.DAYS.between(LocalDate.parse(cr.getString(3)), LocalDate.now()) + "일차", JLabel.CENTER) {{
			setForeground(Color.blue);
		}});
		nPanel.add(wPanel, BorderLayout.WEST);
		
		JPanel ePanel = new JPanel(new GridLayout(2, 1, 2, 2));
		ePanel.setBackground(Color.white);
		ePanel.add(new JLabel(User.getUser().getString(1) + " 회원님", JLabel.RIGHT));
		ePanel.add(new JLabel("문제 갯수는 총 320개", JLabel.LEFT) {{
			setForeground(Color.LIGHT_GRAY);
		}});
		nPanel.add(ePanel, BorderLayout.EAST);
		setMainPanel();
		borderPanel.add(nPanel, BorderLayout.NORTH);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new Main();
				dispose();
			}
		});
		setFrame("강의", 400, 550);
	}
	
	private void setMainPanel() {
		JPanel mainPanel = new JPanel(new GridLayout(16, 1, 10, 10));
		mainPanel.setBackground(Color.white);
		list = Connections.select("SELECT * FROM lecture.lecture where cno = ?;", cno);
		
		for(int i = 0; i < 16; i++) {
			Data data = list.get(i);
			boolean user = data.getString(5).contains(User.getUser().getString(0));
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			p.setBorder(BorderFactory.createLineBorder(Color.black));
			
			JLabel pdf = new JLabel(getter.getImage("datafiles/icon/pdf.png", 100, 111));
			pdf.setEnabled(!user);
			
			String s = "<html><font color='blue'>" + data.getString(1) + "</font color><br>문제갯수: 20개<br>" + data.getString(2).substring(0, data.getString(2).length() > 20 ? 20 : data.getString(2).length()) + "</html>";
			JLabel l = new JLabel(s);
			l.setEnabled(!user);
			l.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));
			p.add(l);
			p.add(pdf, BorderLayout.WEST);
			if(pdf.isEnabled()) {
				pdf.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							System.out.println("datafiles/question/" + certi.getString(1) + "/" + (data.getInt(3)) + ".pdf");
							Connections.update("update lecture set student = ? where lno = ?", data.get(5) + "," + User.getUser().getInt(0), data.get(0));
							List<Data> test = Connections.select("select * from lecture where cno = ? and student like ?", certi.get(0), User.getUser().get(0));
							if((test.size() / 60.0) * 100 > 60.0 && !infor) {
								getter.infor("60% 이상입니다");
								infor = true;
							}
							pdf.setEnabled(false);
							l.setEnabled(false);
							Desktop.getDesktop().open(new File("datafiles/question/" + certi.getString(1) + "/" + (data.getInt(3)) + ".pdf"));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
			mainPanel.add(p);
		}
		borderPanel.add(new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBorder(null);
			MouseAdapter m = new MouseAdapter() {
				int y;
				public void mousePressed(java.awt.event.MouseEvent e) {
					y = e.getY();
				};
				public void mouseDragged(java.awt.event.MouseEvent e) {
					JScrollBar sb = getVerticalScrollBar();
					sb.setValue(sb.getValue() + (y - e.getY()));
					y = e.getY();
				};
			};
			addMouseListener(m);
			addMouseMotionListener(m);
		}});
	}

	public static void main(String[] args) {
		new 강의(3);
	}
}
