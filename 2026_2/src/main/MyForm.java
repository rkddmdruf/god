package main;

import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import utils.*;

public class MyForm extends CFrame{
	JLabel title = new JLabel("수강신청이 완료되었습니다.", getter.getImageIcon("datafiles/icon/logo.png", 35, 35), JLabel.LEFT);
	JPanel mainPanel = new JPanel(new BorderLayout(25, 25)) {{
		setBackground(Color.white);
	}};
	JLabel mainPanelTitle = new JLabel("나의 강의실") {{
		setForeground(Color.white);
		setPreferredSize(new Dimension(50, 50));
		setOpaque(true);
	}};
	
	JLabel left = new JLabel("<") {{
		setEnabled(false);
		setFont(new Font("맑은 고딕", 1, 25));
	}};
	JLabel right = new JLabel(">") {{
		setFont(new Font("맑은 고딕", 1, 25));
	}};
	
	List<Data> 강의 = Connections.select("SELECT course_registration.*, certi.cname, certi.ratring FROM lecture.course_registration \r\n"
			+ "join certi on certi.cno = course_registration.cno\r\n"
			+ "where uno = ?;", User.getUser().get(0));
	List<Data> test = Connections.select("SELECT test.ano, test.cno, test.uno, test.exam_date, test.exam, certi.cname, certi.ratring FROM lecture.test \r\n"
			+ "join certi on certi.cno = test.cno\r\n"
			+ "where uno = ?;", User.getUser().get(0));
	int selectIndex = 0;
	public MyForm() {
		test.addAll(강의);
		Collections.sort(test, Comparator.comparing(e -> LocalDate.parse(e.getString(3))));
		
		borderPanel.setBorder(createEmptyBorder(10,10,10,10));
		borderPanel.setLayout(new BorderLayout(10, 10));
		borderPanel.add(title, BorderLayout.NORTH);
		borderPanel.add(mainPanel);
		setMainPanel();
		setAction();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				new Main();
			};
		});
		setFrame("나의 과정", 325, 450);
	}
	
	private void setAction() {
		MouseAdapter m = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getSource() == left) {
					left.setEnabled(false);
					right.setEnabled(true);
					selectIndex--;
					if(selectIndex <= 0)
						selectIndex = 0;
					else
						left.setEnabled(true);
				}
				if(e.getSource() == right) {
					left.setEnabled(true);
					right.setEnabled(false);
					selectIndex++;
					if(selectIndex >= test.size() - 1)
						selectIndex = test.size() - 1;
					else
						right.setEnabled(true);
				}
				setMainPanel();
			}
		};
		left.addMouseListener(m);
		right.addMouseListener(m);
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		try {
			LocalTime.parse(test.get(selectIndex).getString(4));
			mainPanelTitle.setBackground(Color.red);
		} catch (Exception e) {
			mainPanelTitle.setBackground(Color.blue);
		}
		boolean blue = mainPanelTitle.getBackground().getRGB() == Color.blue.getRGB();
		
		JLabel image = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(new ImageIcon("datafiles/certification/" + test.get(selectIndex).getInt(1) + ".png").getImage(), 0, 0, getWidth(), getHeight(), null);
				super.paintComponent(g);
			}
		};
		if(blue) {
			image.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						new 강의(test.get(selectIndex).getInt(1));
						dispose();
					}
				}
			});
		}
		JPanel infor = new JPanel(new GridLayout(3, 1, 20, 20));
		infor.setBackground(Color.white);
		infor.add(new JLabel(test.get(selectIndex).getString(5) + " " + test.get(selectIndex).getString(6) + "급"));
		infor.add(new JLabel(test.get(selectIndex).getString(3) + (blue ? "~" + LocalDate.parse(test.get(selectIndex).getString(3)).plusDays(29) : "") ));
		infor.add(new JLabel((blue ? "시청률은 60% 이상이어야 합니다." : "점수는 60점이상이여야 합니다.")));
		
		mainPanel.add(new JPanel(new BorderLayout(10, 10)) {{
			setBorder(createEmptyBorder(0, 0, 20, 0));
			setBackground(Color.white);
			add(image);
			add(infor, BorderLayout.SOUTH);
		}});
		mainPanel.add(mainPanelTitle, BorderLayout.NORTH);
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 75)) {{
			setBackground(Color.white);
			add(left);
		}}, BorderLayout.WEST);
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 75)) {{
			setBackground(Color.white);
			add(right);
		}}, BorderLayout.EAST);
		
		
		revalidate();
		repaint();
	}

	public static void main(String[] args) {
		new MyForm();
	}
	
}
