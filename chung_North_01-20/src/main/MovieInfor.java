package main;

import java.awt.BorderLayout;
import java.awt.Color;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class MovieInfor extends JFrame{

	JPanel borderPanel = new JPanel(new BorderLayout(7,7)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5,5,5,5));
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel);
	int m_no;
	MovieInfor(int m_no){
		this.m_no = m_no;
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		setNorthPanel();
		borderPanel.add(sc);
		add(borderPanel);
		setFrame.setframe(this, "영화 정보", 700, 450);
	}
	
	private void setNorthPanel() {
		JPanel p = new JPanel(new BorderLayout(10,10));
		p.setBackground(Color.white);
		
		JLabel img = new JLabel(getter.getImageIcon("datafiles/movies/" + m_no + ".jpg", 175, 250)) {
			
		};
	}
	
	public static void main(String[] args) {
		new MovieInfor(1);
	}
}
