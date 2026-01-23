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
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		setNorthPanel();
		setInfor();
		setChat();
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		borderPanel.add(new JScrollPane(mainPanel));
		add(borderPanel);
		reservationBut.addActionListener(e->{
			if(User.getUser() == null) {
				getter.mg("로그인을 해주세요.", JOptionPane.ERROR_MESSAGE);
				new Login();
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
			new reservation(m_no);
			dispose();
			return;
		});
		setFrame.setframe(this, "영화 정보", 800, 400);
	}
	/*
	 * select * from (
select user.u_no, u_name, re_com from review
right join user on user.u_no = review.u_no
where m_no = ?
) as t*/
	private void setChat() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);

		Color[] colors = {Color.red, Color.blue, Color.yellow, Color.green};
		int totals = 0;
		List<Integer> ageList = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			List<Data> list = Connections.select("select * from (\r\n"
					+ "select \r\n"
					+ "case\r\n"
					+ "	when timestampdiff(year, u_birth, \"2025-09-10\") >= 19 then 0\r\n"
					+ "    when timestampdiff(year, u_birth, \"2025-09-10\") >= 13 then 1\r\n"
					+ "    when timestampdiff(year, u_birth, \"2025-09-10\") >= 5 then 2\r\n"
					+ "    else 3\r\n"
					+ "end as c\r\n"
					+ " from review\r\n"
					+ "right join user on user.u_no = review.u_no\r\n"
					+ "where m_no = ?\r\n"
					+ ") as t where c = ?", m_no, i);
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
					Arc2D.Double arc = new Arc2D.Double(30,30,170,170, start, end, Arc2D.PIE);
					g2.fill(arc);
				}
			}
		};
		chat.setPreferredSize(new Dimension(250,250));
		
		JPanel gridPanel = new JPanel(new GridLayout(4, 1, 5, 5));
		gridPanel.setBackground(Color.white);
		gridPanel.setBorder(createEmptyBorder(25,0,80, 0));
		String[] ageName = "성인 청소년 어린이 유아".split(" ");
		for(int i = 0; i < 4;i++) {
			final int index = i;
			JLabel l = new JLabel() {
				private int p = 100 / (total == 0 ? 1 : total);
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(colors[index]);
					g.fillRect(0, 0, 20, 20);
					g.setColor(Color.black);
					g.setFont(font.deriveFont(20f));
					g.drawString(ageName[index], 22, 20);
					g.drawString((p * ageList.get(index)) + "%", 90, 20);
				}
			};
			gridPanel.add(l);
		}
		List<Data> rList = Connections.select("select * from (\r\n"
				+ "select user.u_no, u_name, re_com from review\r\n"
				+ "right join user on user.u_no = review.u_no\r\n"
				+ "where m_no = ?\r\n"
				+ ") as t", m_no);
		System.out.println(rList);
		JPanel rPanel = new JPanel(new GridLayout(4, 1, 3, 3));
		rPanel.setPreferredSize(new Dimension(350, 0));
		rPanel.setBackground(Color.white);
		for(int i = 0; i < rList.size(); i++) {
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			
			JPanel ip = new JPanel(new GridLayout(2, 1));
			ip.setBackground(Color.white);
			ip.add(new JLabel(rList.get(i).get(1).toString()));
			ip.add(new JLabel(rList.get(i).get(2).toString()));
			
			p.add(new JLabel(getter.getImageIcon("datafiles/user/" + rList.get(i).getInt(0) + ".jpg", 50, 50)), BorderLayout.WEST);
			p.add(ip);
			
			rPanel.add(p);
		}
		panel.add(new JScrollPane(rPanel), BorderLayout.EAST);
		panel.add(gridPanel);
		panel.add(chat, BorderLayout.WEST);
		mainPanel.add(panel, BorderLayout.SOUTH);
	}
	
	private void setInfor() {
		JTextArea t = new JTextArea(movie.get(4).toString());
		t.setFont(font.deriveFont(16f));
		t.setFocusable(false);
		t.setCursor(getCursor().getDefaultCursor());
		
		mainPanel.add(new JScrollPane(t) {{
			setBackground(Color.white);
			setPreferredSize(new Dimension(0, 225));
			setBorder(createCompoundBorder(createEmptyBorder(20,0,0,0), createLineBorder(Color.black)));
		}});
	}
	private void setNorthPanel() {
		JPanel panel = new JPanel(new BorderLayout(5,5));
		panel.setBackground(Color.white);
		
		JLabel img = new JLabel(getter.getImageIcon("datafiles/movies/" + m_no + ".jpg", 160, 240)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon().getImage(), 5, 0, 20, 20, null);
			}
		};
		img.setVerticalAlignment(JLabel.BOTTOM);
		img.setPreferredSize(new Dimension(160, 242));
		
		JPanel inforPanel = new JPanel(new BorderLayout(10,10));
		inforPanel.setBackground(Color.white);
		
		inforPanel.add(new JLabel("제목: " + movie.get(1)) {{
			setFont(font);
		}}, BorderLayout.NORTH);
		String gno = Connections.select("select g_name from genre where g_no = ?", movie.get(5)).get(0).get(0).toString();
		inforPanel.add(new JTextArea("감독: " + movie.get(3) + "\n\n장르: " + gno+ "\n\n개봉일: " + movie.get(6)) {{
			
			setFocusable(false);
			setCursor(getCursor().getDefaultCursor());
		}});
		inforPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBorder(createEmptyBorder(20, 0, 30, 0));
			setBackground(Color.white);
			add(reservationBut);
		}}, BorderLayout.SOUTH);
		
		panel.add(img, BorderLayout.WEST);
		panel.add(inforPanel);
		
		mainPanel.add(panel, BorderLayout.NORTH);
	}
	
	public static void main(String[] args) {
		new MovieInfor(1);
	}
}
