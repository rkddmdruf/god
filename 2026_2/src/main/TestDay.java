package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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
		scPanel.setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(scPanel) {{
		setBackground(Color.white);
	}};
	TestDay() {
		List<Data> list = Connections.select("SELECT schedule.*, certi.* FROM lecture.schedule\r\n"
				+ "join certi on schedule.cno = certi.cno\r\n"
				+ "order by schedule.start_date, schedule.cno;");
		borderPanel.setBorder(createEmptyBorder(5, 12, 6, 12));
		setNorthPanel();
		borderPanel.add(northPanel, BorderLayout.NORTH);
		setFrame("시럼일정", 500, 350);
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
