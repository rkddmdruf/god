package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Arc2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class MovieInfor extends JFrame{
	Font font = new Font("맑은 고딕", 1, 26);
	JPanel borderPanel = new JPanel(new BorderLayout(7, 7)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,5,5,5));
	}};
	JPanel mainPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBorder(createEmptyBorder(10,5,5,5));
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel) {{
		setBackground(Color.white);
	}};
	
	JButton reservation = new CustumButton("예매하기");
	
	Data movie;
	int m_no = 0;
	MovieInfor(int m_no){
		this.m_no = m_no;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		borderPanel.add(sc);
		setMoviePoster();
		setInforPanel();
		setChat();
		add(borderPanel);
		reservation.addActionListener(e->{
			if(User.getUser() == null) {
				getter.mg("로그인을 해주세요", JOptionPane.ERROR_MESSAGE);
				new Login();
				return;
			}
			LocalDate user = LocalDate.parse(User.getUser().get(4).toString());
			if(user.isAfter(LocalDate.of(2025 - 19, 9, 10))) {
				getter.mg("미성년자는 시청 금지 입니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			new reservation(m_no);
		});
		setFrame.setframe(this, "영화 정보", 775, 500);
	}
	
	
	private void setChat() {
		Color[] colors = {Color.red, Color.blue, Color.yellow, Color.green};
		JPanel p = new JPanel(new BorderLayout(5,5));
		p.setBackground(Color.white);
		p.setPreferredSize(new Dimension(0, 250));
		
		int total = 0;
		List<Integer> age = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			int size = Connections.select("select * from (\r\n"
					+ "SELECT user.u_no, u_name, re_com, \r\n"
					+ "case\r\n"
					+ "	when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 19 then 0\r\n"
					+ "    when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 13 then 1\r\n"
					+ "    when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 5 then 2\r\n"
					+ "    else 3\r\n"
					+ "end as t\r\n"
					+ "FROM moviedb.review\r\n"
					+ "join user on user.u_no = review.u_no\r\n"
					+ "where m_no = 1) as te where t = ?", i).size();
			age.add(size);
			total += size;
		}
		int totals = total;
		JLabel chat = new JLabel() {
			double start = 90;
			double end = 0;
			double theta = 360.0 / totals;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				for(int i = 0; i < 4; i++) {
					g2.setColor(colors[i]);
					start += end;
					end = age.get(i) * theta;
					Arc2D.Double arc = new Arc2D.Double(30,30,175, 175, start, end, Arc2D.PIE);
					g2.fill(arc);
				}
			}
		};
		chat.setPreferredSize(new Dimension(220, 250));
		
		////////////////// 위에는 차트, 아래에는 그 네모난거 4개
		
		JPanel gridPanel = new JPanel(new GridLayout(4, 1, 3, 3));
		gridPanel.setBackground(Color.white);
		gridPanel.setBorder(createEmptyBorder(20, 0, 80, 0));
		
		String[] ageName = "성인,청소년,어린이,유아".split(",");
		for(int i = 0; i < 4; i++) {
			final int index = i;
			JLabel l = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(colors[index]);
					g.fillRect(0, 3, 15, 15);
					g.setColor(Color.black);
					g.setFont(font.deriveFont(20f));
					g.drawString(ageName[index], 17, 20);
					g.drawString("50%", 80, 20);
				}
			};
			gridPanel.add(l);
		}
		
		////////////////////////////////위에는 그 네모난거 아래는 리뷰
		
		JPanel reviewPanel = new JPanel(new GridLayout(4, 1));
		reviewPanel.setBackground(Color.white);
		List<Data> reviews = Connections.select("SELECT user.u_no, u_name, re_com\r\n"
				+ "FROM moviedb.review\r\n"
				+ "join user on user.u_no = review.u_no\r\n"
				+ "where m_no = 1");
		for(int i = 0; i < reviews.size(); i++) {
			final int index = i;
			JPanel panel = new JPanel(new BorderLayout(5,5));
			panel.setBackground(Color.white);
			panel.setBorder(createLineBorder(Color.black));
			panel.add(new JLabel(getter.getImageIcon("datafiles/user/" + reviews.get(i).getInt(0) + ".jpg", 50, 50)), BorderLayout.WEST);
			panel.add(new JPanel(new GridLayout(2, 1)) {{
				setBackground(Color.white);
				add(new JLabel(reviews.get(index).get(1).toString()));
				add(new JLabel(reviews.get(index).get(2).toString()));
			}});
			reviewPanel.add(panel);
		}
		p.add(new JScrollPane(reviewPanel), BorderLayout.EAST);
		p.add(gridPanel);
		p.add(chat, BorderLayout.WEST);
		mainPanel.add(p, BorderLayout.SOUTH);
	}
	
	private void setInforPanel() {
		JTextArea t = new JTextArea(movie.get(4).toString());
		t.setFont(font.deriveFont(22f));
		t.setFocusable(false);
		t.setCursor(getCursor().getDefaultCursor());
		JScrollPane s = new JScrollPane(t);
		s.setPreferredSize(new Dimension(0, 250));
		mainPanel.add(s);
	}
	
	private void setMoviePoster() {
		JPanel p = new JPanel(new BorderLayout(5, 5));
		p.setBorder(createEmptyBorder(0,0,20,0));
		p.setBackground(Color.white);
		
		JLabel m = new JLabel(getter.getImageIcon("datafiles/movies/" + m_no + ".jpg", 175, 225), JLabel.LEFT) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/limits/" + movie.getInt(2) + ".png").getImage(), 5, 0, 25, 25, null);
			}
		};
		m.setPreferredSize(new Dimension(180, 227));
		m.setVerticalAlignment(JLabel.BOTTOM);
		
		JPanel inforPanel = new JPanel(new BorderLayout(10, 10));
		inforPanel.setBackground(Color.white);
		inforPanel.add(new JLabel("제목: " + movie.get(1)) {{
			setFont(font);
		}}, BorderLayout.NORTH);
		
		String g = Connections.select("select g_name from genre where g_no = ?", movie.get(5)).get(0).get(0).toString();
		inforPanel.add(new JTextArea("감독: " + movie.get(3) + "\n\n장르: " + g + "\n\n개봉일: " + movie.get(6)));
		
		inforPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)) {{
			setBackground(Color.white);
			setBorder(createEmptyBorder(15,0,30,0));
			add(reservation);
		}}, BorderLayout.SOUTH);
		p.add(inforPanel);
		p.add(m, BorderLayout.WEST);
		mainPanel.add(p, BorderLayout.NORTH);
	}
	
	public static void main(String[] args) {
		new MovieInfor(1);
	}
}
