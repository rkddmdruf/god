package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;

import static javax.swing.BorderFactory.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.CFrame;
import utils.Connections;
import utils.Data;

public class Schedule extends CFrame{

	LocalDate date = LocalDate.of(2026, 5, 1);
	int cno;
	Data	 data;
	JSlider js = new JSlider(1, 31) {{
		setBackground(Color.white);
	}};
	public Schedule(int scno, int cno) {
		data = Connections.select("select * from certi where cno = ?", cno).get(0);
		this.cno = cno;
		borderPanel.setBorder(createEmptyBorder(10,10,10,10));
		borderPanel.add(new JLabel(data.getString(1) + " " +  data.getString(6) + "급") {{
			setFont(new Font("맑은 고딕", 1, 16));
		}}, BorderLayout.NORTH);
		setDayPanel();
		setFrame("시험스케줄", 400, 200);
	}
	
	private void setDayPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(createEmptyBorder(10,0, 10, 0));
		p.setBackground(Color.white);
		p.add(js, BorderLayout.SOUTH);
		p.add(new JLabel(date.getYear() + "-" + date.getMonthValue(), JLabel.CENTER), BorderLayout.NORTH);
		p.add(new JLabel("1"), BorderLayout.WEST);
		p.add(new JLabel("31"), BorderLayout.EAST);
		borderPanel.add(p);
	}

	public static void main(String[] args) {
		new Schedule(1, 1);
	}
}
