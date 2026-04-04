package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import static  javax.swing.BorderFactory.*;

import utils.*;

public class 강의 extends CFrame{

	LocalDate now = LocalDate.now();
	JPanel mainPanel = new JPanel(new GridLayout(16, 1, 10, 10)) {{
		setBackground(Color.white);
	}};
	List<Data> list = new ArrayList<>();
	Data certi = new Data();
	int cno;
	
	boolean percent = false;
	public 강의(int cno) {
		this.cno = cno;
		certi = Connections.select("SELECT cname, ratring FROM lecture.certi where cno = ?;", cno).get(0);
		list = Connections.select("SELECT * FROM lecture.lecture where cno = ?;", cno);
		borderPanel.setLayout(new BorderLayout(10, 10));
		borderPanel.setBorder(createEmptyBorder(10,10,10,10));
		borderPanel.add(new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			MouseAdapter m = new MouseAdapter() {
				private int y;
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
		setNorthPanel();
		setMainPanel();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				new Main();
			}
		});
		setFrame("강의", 400, 600);
	}
	
	int n = 0;
	private void setMainPanel() {
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(10, 10));
			p.setPreferredSize(new Dimension(0, 150));
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			JLabel l = new JLabel("<html><font color='blue'>" + list.get(i).getString(1) + "</font color><br>" + "문제갯수: 20개<br>" + list.get(i).getString(2) + "</html");
			l.setFont(new Font("맑은 고딕", 1, 12));
			JLabel img = new JLabel(getter.getImageIcon("datafiles/icon/pdf.png", 120, 150));
			String uno = User.getUser().getString(0);
			List<String> unos = Arrays.asList(list.get(i).getString(5).split(","));
			if(unos.contains(uno)) {
				l.setEnabled(false);
				img.setEnabled(false);
				n++;
			}else {
				img.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						Connections.update("update lecture set student = ? where lno = ?;", list.get(index).get(5) + "," + User.getUser().getInt(0), list.get(index).get(0));
						img.setEnabled(false);
						l.setEnabled(false);
						revalidate();
						repaint();
						img.removeMouseListener(this);
						n++;
						if((n / 16.0) * 100.0 > 60.0 && !percent) {
							getter.mg("60% 이상입니다", JOptionPane.INFORMATION_MESSAGE);
							percent = true;
						}
						try {
							Desktop.getDesktop().open(new File("datafiles/question/" + certi.getString(0) + "/" + index +".pdf"));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
			p.add(l);
			p.add(img, BorderLayout.WEST);
			mainPanel.add(p);
		}
	}
	
	private void setNorthPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(Color.white);
		
		JPanel w = new JPanel(new GridLayout(2, 1));
		w.setBackground(Color.white);
		w.add(new JLabel(certi.getString(0) + " " + certi.getString(1) + "급", JLabel.CENTER));
		LocalDate date = LocalDate.parse(Connections.select("SELECT start_date FROM lecture.course_registration where cno = ? and uno = ?;", cno, User.getUser().get(0)).get(0).getString(0));
		System.out.println(date);
		w.add(new JLabel("<html><font color='blue'>" + ChronoUnit.DAYS.between(date, now) + "일차</font color><html>", JLabel.CENTER));
		p.add(w, BorderLayout.WEST);
		
		JPanel e = new JPanel(new GridLayout(2, 1));
		e.setBackground(Color.white);
		e.add(new JLabel(User.getUser().getString(1) + " 회원님", JLabel.RIGHT));
		e.add(new JLabel("<html><font color='gray'>문제 갯수는 총 320개</font color><html>") );
		p.add(e, BorderLayout.EAST);
		
		borderPanel.add(p, BorderLayout.NORTH);
	}

	public static void main(String[] args) {
		new 강의(3);
	}
}
