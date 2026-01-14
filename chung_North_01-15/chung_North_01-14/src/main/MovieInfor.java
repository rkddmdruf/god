package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.ScrollPane;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class MovieInfor extends JFrame{
	Font font = new Font("맑은 고딕", 1, 20);
	
	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBorder(createEmptyBorder(0,5,5,5));
		setBackground(Color.white);
	}};
	NorthPanel northPanel = new NorthPanel(this);
	
	JPanel mainPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10, 5,5,5));
	}};
	JButton reservation = new CustumButton("예매하기");
	JScrollPane sc = new JScrollPane(mainPanel);
	Data movie;
	int m_no = 0;
	public MovieInfor(int m_no, boolean isFromMain) {
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		borderPanel.add(northPanel, BorderLayout.NORTH);
		
		
		setMainPanel();
		borderPanel.add(sc);
		add(borderPanel);
		new A_setFrame(this, "영화 정보", 600, 500);
	}
	
	private void setMainPanel() {
		setNorthPanel();
		setCenterPanel();
		setSouthPanel();
	}
	
	private void setNorthPanel() {
		JPanel p = new JPanel(new BorderLayout(5,5));
		p.setBackground(Color.white);
		p.setBorder(createEmptyBorder(0,0,20,0));
		p.setPreferredSize(new Dimension(0, 245));
		
		p.add(new JLabel(getter.getImageIcon("datafiles/movies/" + movie.get(0) + ".jpg", 150, 225)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/limits/" + movie.get(2) + ".png").getImage(), 5, 0, 20, 20, null);
			}
		}, BorderLayout.WEST);
		JPanel InforPanel = new JPanel(new BorderLayout());
		InforPanel.setBackground(Color.white);
		InforPanel.add(new JLabel("제목: " + movie.get(1).toString()) {{
			setFont(font.deriveFont(24f));
		}}, BorderLayout.NORTH);
		String genre = Connections.select("select g_name from genre where g_no = ?", movie.get(5)).get(0).get(0).toString();
		JTextArea infor = new JTextArea("\n감독: " + movie.get(3) + "\n\n장르: " + genre + "\n\n개봉일: " + movie.get(6));
		InforPanel.add(infor);
		
		JPanel reservationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		reservationPanel.setBackground(Color.white);
		reservationPanel.setBorder(createEmptyBorder(20, 0, 20, 0));
		reservationPanel.add(reservation);
		
		InforPanel.add(reservationPanel, BorderLayout.SOUTH);
		
		p.add(InforPanel);
		mainPanel.add(p, BorderLayout.NORTH);
	}
	
	private void setCenterPanel() {
		JTextArea ta = new JTextArea(movie.get(4).toString());
		ta.setLineWrap(true);
		ta.setFocusable(false);
		ta.setFont(font);
		JScrollPane scsc = new JScrollPane(ta);
		scsc.setPreferredSize(new Dimension(0, 200));
		mainPanel.add(scsc);
	}
	
	private void setSouthPanel() {
		
	}
	
	public static void main(String[] args) {
		new MovieInfor(1, true);
	}
}
