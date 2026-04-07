package main;

import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;

public class MyInfor extends CFrame{

	JPanel mainPanel = new JPanel(new BorderLayout(20, 20)) {{
		setBackground(Color.white);
	}};
	JLabel colorLabel = new JLabel("나의 강의실") {{
		setBackground(Color.blue);
		setForeground(Color.white);
		setOpaque(true);
		setPreferredSize(new Dimension(0, 50));
	}};
	JLabel left = new JLabel("<") {{
		setFont(new Font("맑은 고딕", 1, 26));
	}};
	JLabel right = new JLabel(">") {{
		setFont(new Font("맑은 고딕", 1, 26));
	}};
	List<Data> list = new ArrayList<>();
	int selectIndex = 0;
	JLabel img;
	JLabel infor = new JLabel();
	
	JLabel topLabel = new JLabel("", getter.getImage("datafiles/icon/logo.png", 40, 40), JLabel.LEFT);
	MouseAdapter mc = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				new 강의(list.get(selectIndex).getInt(2));
				dispose();
			}
		};
	};;
	public MyInfor() {
		left.setEnabled(false);
		borderPanel.setBorder(createEmptyBorder(10, 10, 10, 10));
		borderPanel.add(topLabel, BorderLayout.NORTH);
		for(Data d : Connections.select("SELECT 0, start_date, certi.* FROM lecture.course_registration\r\n"
				+ "join certi on certi.cno = course_registration.cno\r\n"
				+ "where uno = ? order by start_date;", User.getUser().get(0)))
			list.add(d);
		for(Data d : Connections.select("SELECT 1, exam_date, certi.* FROM lecture.test\r\n"
				+ "join certi on certi.cno = test.cno\r\n"
				+ "where uno = ?  order by exam_date;", User.getUser().get(0)))
			list.add(d);
		list.sort((a, b) -> LocalDate.parse(a.getString(1)).compareTo(LocalDate.parse(a.getString(1))));
		img = new JLabel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				if(!list.isEmpty()) {
					g.drawImage(new ImageIcon("datafiles/certification/" + list.get(selectIndex).getString(2) + ".png").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			}
		};
		setting();
		MouseAdapter lr = new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if(e.getSource() == left && left.isEnabled()) {
					right.setEnabled(true);
					selectIndex--;
				}
				if(e.getSource() == right && right.isEnabled()) {
					left.setEnabled(true);
					selectIndex++;
				}
				if(selectIndex == 0) left.setEnabled(false);
				if(selectIndex == list.size() - 1) right.setEnabled(false);
				setting();
			};
		};
		setMainPanel();
		left.addMouseListener(lr);
		right.addMouseListener(lr);
		borderPanel.add(mainPanel);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				new Main();
				dispose();
			};
		});
		setFrame("나의 과정", 400, 550);
	}
	
	private void setMainPanel() {
		mainPanel.add(colorLabel, BorderLayout.NORTH);
		mainPanel.add(left, BorderLayout.WEST);
		mainPanel.add(right, BorderLayout.EAST);
		JPanel p = new JPanel(new BorderLayout(20, 20));
		p.setBackground(Color.white);
		p.add(img);
		infor.setPreferredSize(new Dimension(0, 200));
		p.add(infor, BorderLayout.SOUTH);
		mainPanel.add(p);
	}
	private void setting() {
		img.removeMouseListener(mc);
		Data data = list.get(selectIndex);
		String s = "<html>" + data.getString(3) + " " + data.getString(8) + "급<br><br>" + data.getString(1);
		if(data.getInt(0) == 0) {
			img.addMouseListener(mc);
			s += "~" + LocalDate.parse(data.getString(1)).plusDays(29) + "<br><br>";
			s += "시청률은 60%이상이여야 합니다.";
			colorLabel.setBackground(Color.blue);
		}else {
			s += "<br><br>점수는 60점이상이여야 합니다.";
			colorLabel.setBackground(Color.red);
		}
		s += "</html>";
		img.revalidate();
		img.repaint();
		infor.setText(s);
		revalidate();
		repaint();
	}
	
	public static void main(String[] args) {
		new MyInfor();
	}
}
