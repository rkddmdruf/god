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
import java.awt.geom.Arc2D.Double;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class MovieInfor extends JFrame{
	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,10,10,10));
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5, 5, 5, 5));
	}};
	JButton reservationBut = new CustumButton("예매하기");
	int m_no;
	Data movie;
	Font font = new Font("맑은 고딕", 1, 24);

	MovieInfor(int m_no){
		this.m_no = m_no;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		borderPanel.add(new JScrollPane(mainPanel) {{
			getVerticalScrollBar().setUnitIncrement(20);
		}});
		setNorthPanel();
		setCenterPanel();
		setChat();
		add(borderPanel);
		
		reservationBut.addActionListener(e->{
			if(User.getUser() == null) {
				getter.mg("로그인을 해주세요.", JOptionPane.ERROR_MESSAGE);
				new Login();
				dispose();
				return;
			}
			LocalDate userD = LocalDate.parse(User.getUser().get(4).toString());
			LocalDate nows = LocalDate.of(2006, 9, 10);
			if(movie.getInt(2) == 4)
				if(!nows.isAfter(userD)) {
					getter.mg("미성년자는 시청 금지입니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			new Reservation(m_no);
		});
		SetFrame.setFrame(this, "영화 정보", 800, 450);
	}
	
	private void setChat() {
		Color[] colors = {Color.red, Color.blue, Color.yellow, Color.green};
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		List<Integer> ageList = new ArrayList<>();
		int totals = 0;
		for(int i = 0; i < 4; i++) {
			List<Data> list = Connections.select("select * from(\r\n"
					+ "SELECT \r\n"
					+ "case\r\n"
					+ "	when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 19 then 0\r\n"
					+ "    when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 13 then 1\r\n"
					+ "    when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 5 then 2\r\n"
					+ "    else 3\r\n"
					+ "end as caseifs\r\n"
					+ "\r\n"
					+ " FROM moviedb.review join user on user.u_no = review.u_no\r\n"
					+ " where m_no = ?) as d where caseifs = ?", m_no, i);
			totals += list.size();
			ageList.add(list.size());
		}
		int total = totals;
		JLabel chat = new JLabel() {
			private double start = 90;
			private double end = 0;
			private double theta = 360.0 / total;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				for(int i = 0; i < 4; i++) {
					g2.setColor(colors[i]);
					start += end;
					end = theta * ageList.get(i);
					Arc2D.Double arc = new Arc2D.Double(30, 30, 175, 175, start, end, Arc2D.PIE);
					g2.fill(arc);
				}
			}
		};
		chat.setPreferredSize(new Dimension(250,250));
		
		JPanel gridPanel = new JPanel(new GridLayout(4, 1, 3,3));
		gridPanel.setBorder(createEmptyBorder(30,0,100,0));
		gridPanel.setBackground(Color.white);
		
		String[] ageName = "성인 청소년 어린이 유아".split(" ");
		
		for(int i = 0; i < 4;i++) {
			final int index = i;
			JLabel l = new JLabel() {
				private int pursent = 100 / total;
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(colors[index]);
					g.fillRect(0, 5, 17, 17);
					g.setColor(Color.black);
					g.setFont(font.deriveFont(20f));
					g.drawString(ageName[index], 20, 20);
					g.drawString(pursent + "%", 85, 20);
				}
			};
			gridPanel.add(l);
		}
		
		JPanel reviewPanel = new JPanel(new GridLayout(4, 1, 3, 3));
		reviewPanel.setPreferredSize(new Dimension(350, 0));
		reviewPanel.setBackground(Color.white);
		List<Data> review = Connections.select("SELECT user.u_no, u_name, re_com\r\n"
				+ " FROM moviedb.review join user on user.u_no = review.u_no\r\n"
				+ " where m_no = ?", m_no);
		for(int i = 0; i < review.size(); i++) {
			Data data = review.get(i);
			
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			
			JPanel txtPanel = new JPanel(new GridLayout(2, 1));
			txtPanel.setBackground(Color.white);
			txtPanel.add(new JLabel(data.get(1).toString()));
			txtPanel.add(new JLabel(data.get(2).toString()));
			
			p.add(new JLabel(getter.getImageIcon("datafiles/user/" + data.getInt(0) + ".jpg", 55,55)), BorderLayout.WEST);
			p.add(txtPanel);
			reviewPanel.add(p);
		}
		
		panel.add(new JScrollPane(reviewPanel), BorderLayout.EAST);
		panel.add(gridPanel);
		panel.add(chat, BorderLayout.WEST);
		mainPanel.add(panel, BorderLayout.SOUTH);
	}
	
	private void setCenterPanel() {
		JTextArea t = new JTextArea(movie.get(4).toString());
		t.setFont(font.deriveFont(20f));
		t.setFocusable(false);
		t.setCursor(getCursor().getDefaultCursor());
		mainPanel.add(new JScrollPane(t) {{
			setBackground(Color.white);
			setBorder(createCompoundBorder(createEmptyBorder(25,0,0,0), createLineBorder(Color.black)));
			setPreferredSize(new Dimension(0, 250));
		}});
	}
	
	private void setNorthPanel() {
		JPanel panel = new JPanel(new BorderLayout(10,10));
		panel.setBackground(Color.white);
		
		JLabel img = new JLabel(getter.getImageIcon("datafiles/movies/" + m_no + ".jpg", 160, 240)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/limits/" + movie.get(2) + ".png").getImage(), 5, 0, 25, 25, null);
			}
		};
		img.setVerticalAlignment(JLabel.BOTTOM);
		img.setPreferredSize(new Dimension(160, 242));
		
		JPanel inforPanel = new JPanel(new BorderLayout(15,15));
		inforPanel.setBackground(Color.white);
		
		JLabel name = new JLabel("제목: " + movie.get(1));
		name.setFont(font);
		
		JTextArea ta = new JTextArea("감독: " + movie.get(3)
			+ "\n\n장르: " + Connections.select("select g_name from genre where g_no = ?", movie.get(5)).get(0).get(0)
			+ "\n\n개봉일: " + movie.get(6));
		
		
		inforPanel.add(name, BorderLayout.NORTH);
		inforPanel.add(ta);
		inforPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			add(reservationBut);
			setBorder(createEmptyBorder(25, 0, 25, 0));
		}}, BorderLayout.SOUTH);
		
		panel.add(inforPanel);
		panel.add(img, BorderLayout.WEST);
		
		mainPanel.add(panel, BorderLayout.NORTH);
		
		 
	}
	
	public static void main(String[] args) {
		new MovieInfor(1);
	}
}
