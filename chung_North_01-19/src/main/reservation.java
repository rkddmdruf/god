package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class reservation extends JFrame{
	LocalDate nows = LocalDate.of(2025, 9, 10);
	Font font = new Font("맑은 고딕", 1, 26);
	JPanel wPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(createLineBorder(Color.black));
	}};
	List<JPanel> panels = new ArrayList<>();
	
	public reservation(int m_no) {
		setWPanel();
		add(wPanel, BorderLayout.WEST);
		setFrame.setframe(this, "예매", 600, 450);
	}
	
	private void setWPanel() {
		JPanel ymPanel = new JPanel(new GridLayout(2, 1, 3, 3));
		ymPanel.setPreferredSize(new Dimension(150, 75));
		ymPanel.setBackground(Color.white);
		
		JLabel y = new JLabel(nows.getYear() + "년", JLabel.LEFT);
		y.setFont(font);
		
		JLabel m = new JLabel(nows.getMonthValue() + "년", JLabel.RIGHT);
		m.setFont(font);
		
		ymPanel.add(y);
		ymPanel.add(m);
		
		String[] dayName = "월화수목금토일".split("");
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 3, 3));
		gridPanel.setBackground(Color.white);
		
		for(int i = 1; i <= nows.getMonth().maxLength(); i++) {
			LocalDate d = LocalDate.of(nows.getYear(), nows.getMonthValue(), i);
			JPanel p = new JPanel(new GridLayout(0, 2));
			p.setBackground(nows.isAfter(d) ? Color.gray : Color.white);
			
			JLabel dayNameLabel = new JLabel(dayName[d.getDayOfWeek().getValue() - 1]);
			dayNameLabel.setForeground(Color.black);
			dayNameLabel.setFont(font.deriveFont(15f));
			
			JLabel day = new JLabel(i + "일");
			day.setFont(font.deriveFont(15f));
			day.setForeground(Color.black);
			
			p.add(dayNameLabel);
			p.add(day);
			gridPanel.add(p);
		}
		wPanel.add(new JScrollPane(gridPanel));
		wPanel.add(ymPanel, BorderLayout.NORTH);
	}
	public static void main(String[] args) {
		new reservation(1);
	}
}
