package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.ScrollPane;
import java.awt.geom.Arc2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;


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
	boolean isFromMain;
	public MovieInfor(int m_no, boolean isFromMain) {
		this.m_no = m_no;
		this.isFromMain = isFromMain;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		borderPanel.add(northPanel, BorderLayout.NORTH);
		
		
		setMainPanel();
		borderPanel.add(sc);
		add(borderPanel);
		
		reservation.addActionListener(e->{
			LocalDate nows = LocalDate.parse("2006-09-10");
			if(User.getUno() == null) {
				JOptionPane.showMessageDialog(null, "로그인을 해주세요", "경고", JOptionPane.ERROR_MESSAGE);
				new Login();
				dispose();
				return;
			}
			if(LocalDate.parse(User.getData().get(4).toString()).isAfter(nows)) {
				JOptionPane.showMessageDialog(null, "미성년자는 시청 금지입니다.", "경고", JOptionPane.ERROR_MESSAGE);
				return;
			}
			new Reservation(m_no, isFromMain);
			dispose();
		});
		A_setFrame.setting(this, "영화 정보", 750, 500);
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
		final Color[] colors = {Color.red, Color.blue, Color.yellow, Color.green};
		
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.setPreferredSize(new Dimension(0, 225));
		southPanel.setBackground(Color.white);
		
		String QueryString = "select *\r\n"
				+ "from (SELECT \r\n"
				+ "case \r\n"
				+ "	when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 19 then 0\r\n"
				+ "    when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 13 then 1\r\n"
				+ "    when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 5 then 2\r\n"
				+ "    else 3\r\n"
				+ "    end as caze\r\n"
				+ " FROM moviedb.review\r\n"
				+ "join user on user.u_no = review.u_no\r\n"
				+ " where m_no = ?) as test\r\n"
				+ " where caze = ?";
		List<Integer> ageSizeList = new ArrayList<>();
		int total = 0;
		for(int i = 0; i < 4; i++) {
			int size = Connections.select(QueryString, m_no, i).size();
			ageSizeList.add(size);
			total += size;
		}
		int totals = total;
		JLabel l = new JLabel() {
			private double start = 90;
			private double end = 0;
			
			private final double theta = 360.0 / totals;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				for(int i = 0; i < 4; i++) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(colors[i]);
					start += end;
					end = ageSizeList.get(i) * theta;
					Arc2D.Double arc = new Arc2D.Double(20, 40, 150, 150, start, end, Arc2D.PIE);
					g2.fill(arc);
				}
			}
		};
		l.setPreferredSize(new Dimension(200,225));
		
		JPanel ageNamePanel = new JPanel(new GridLayout(4, 1, 3, 3));
		ageNamePanel.setBorder(createEmptyBorder(10, 0, 70, 0));
		ageNamePanel.setBackground(Color.white);
		
		String[] ageName = "성인,청소년,어린이,유아".split(",");
		int n = 100 / total;
		
		for (int i = 0; i < 4; i++) {
			String pursent = (ageSizeList.get(i) * n) + "%";
			final int index = i;
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			JLabel rect = new JLabel() {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(colors[index]);
					g.fillRect(0, 5, 15, 15);
					g.setColor(Color.black);
					g.setFont(font);
					g.drawString(ageName[index], 17, 20);
					g.drawString(pursent,80, 20);
					
				};
			};
			p.add(rect);
			ageNamePanel.add(p);
		}
		String queryReviews = "SELECT user.u_no, u_name, re_com FROM moviedb.review\r\n"
				+ "join user on user.u_no = review.u_no\r\n"
				+ " where m_no = ?;";
		List<Data> review = Connections.select(queryReviews, m_no);
		JPanel reviewPanel = new JPanel(new GridLayout(0, 1, 3,3));
		reviewPanel.setBackground(Color.white);
		
		for(Data d : review) {
			JPanel p = new JPanel(new BorderLayout(5, 5));
			p.setBorder(createLineBorder(Color.black));
			p.setBackground(Color.white);
			
			JPanel namerePanel = new JPanel(new GridLayout(0, 1 ,0,0));
			namerePanel.setBackground(Color.white);
			namerePanel.add(new JLabel(d.get(1).toString()));
			namerePanel.add(new JLabel(d.get(2).toString()));
			
			p.add(new JLabel(getter.getImageIcon("datafiles/user/" + d.get(0) + ".jpg", 55, 55)), BorderLayout.WEST);
			p.add(namerePanel);
			reviewPanel.add(p, BorderLayout.WEST);
		}
		for(int i = 0; i < 4 - review.size(); i++) {
			reviewPanel.add(new JLabel(" "));
		}
		southPanel.add(new JScrollPane(reviewPanel) {{
			setPreferredSize(new Dimension(350, 0));
		}}, BorderLayout.EAST);
		southPanel.add(l, BorderLayout.WEST);
		southPanel.add(ageNamePanel);
		
		mainPanel.add(southPanel, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		new MovieInfor(1, true);
	}
}
