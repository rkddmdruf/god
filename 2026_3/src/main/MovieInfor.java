package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import utils.*;


public class MovieInfor extends CFrame{
	Font font = new Font("맑은 고딕", 1, 25);
	
	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,5,5,5));
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5,5,5,5));
	}};
	
	JButton reservationBut = new CustumButton("예매하기");
	int m_no;
	Data movie;
	public MovieInfor(int m_no) {
		this.m_no = m_no;
		reservationBut.setEnabled(false);
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		setNorthPanel();
		borderPanel.add(new NorthPanel(this) {
			@Override
			void loginAction() {
				super.loginAction();
				login.addActionListener(e->{
					if(login.getText().equals("로그인"))
						getter.push(() ->{new MovieInfor(m_no);});
				});
			}
			
			@Override
			void movieSerchAction() {
				super.movieSerchAction();
				movieSerch.addActionListener(e->{
					getter.push(() -> {new MovieInfor(m_no);});
				});
			}
		}, BorderLayout.NORTH);
		borderPanel.add(new JScrollPane(mainPanel));
		add(borderPanel);
		
		reservationBut.addActionListener(e->{
			if(User.getUser() == null) {
				getter.mg("로그인을 해주세요.", JOptionPane.ERROR_MESSAGE);
				new Login();
				
				dispose();
				return;
			}
			if(movie.getInt(2) == 4) {
				LocalDate nows = LocalDate.of(2025, 9, 10);
				LocalDate userD = LocalDate.parse(User.getUser().get(4).toString());
				if(!nows.isAfter(userD)) {
					getter.mg("미성년자는 시청 금지입니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			new Reservation(m_no);
			dispose();
			return;
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				getter.pop().run();
				dispose();
			}
		});
		setFrame("영화 정보", 825, 350);
	}
	
	int scValue = 0;
	private void setNorthPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(Color.white);
		
		JLabel img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/movies/" + movie.getInt(0) + ".jpg").getImage(), 40, 30, 160, 220, null);
				g.drawImage(new ImageIcon("datafiles/limits/" + movie.getInt(2) + ".png").getImage(), 20, 10, 40, 40, null);
			}
		};
		img.setPreferredSize(new Dimension(200, 0));
		
		JPanel inforPanel = new JPanel(new BorderLayout(10,10));
		inforPanel.setBackground(Color.white);
		
		String gname = Connections.select("select g_name from genre where g_no = ?", movie.get(5)).get(0).get(0).toString();
		inforPanel.add(new JTextArea("\n제목: " + movie.getString(1) + "\n\n감독: " + movie.get(3) + "\n\n장르: " + gname + "\n\n개봉일: " + movie.get(6)) {{
			setBorder(createEmptyBorder(40,0,0,0));
			setFocusable(false);
			setCursor(getCursor().getDefaultCursor());
		}});
		inforPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBorder(createEmptyBorder(20, 0, 30, 0));
			setBackground(Color.white);
			add(reservationBut);
		}}, BorderLayout.SOUTH);
		
		JTextArea t = new JTextArea(movie.get(4).toString());
		t.setFont(font.deriveFont(0, 12));
		t.setFocusable(false);
		t.setCursor(getCursor().getDefaultCursor());
		
		JScrollPane sc = new JScrollPane(t) {{
			setBackground(Color.white);
			setPreferredSize(new Dimension(400, 250));
			setBorder(createCompoundBorder(createEmptyBorder(20,0,0,0), createLineBorder(Color.black)));
		}};
		
		Timer timers = new Timer(10, e-> {
			JScrollBar bar = sc.getVerticalScrollBar();
			scValue = bar.getValue();
			bar.setValue(bar.getValue() + 1);
			if(bar.getValue() == scValue) {
				reservationBut.setEnabled(true);
			}
		});
		timers.start();
		panel.add(sc, BorderLayout.EAST);
		
		panel.add(img, BorderLayout.WEST);
		panel.add(inforPanel);
		mainPanel.add(panel, BorderLayout.NORTH);
	}
	
	public static void main(String[] args) {
		new MovieInfor(1);
	}
}
