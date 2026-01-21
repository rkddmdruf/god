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
	LocalDate nows = LocalDate.of(2025, 9, 10);
	Font font = new Font("맑은 고딕", 1, 26);
	JPanel borderPanel = new JPanel(new BorderLayout(7,7)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5,5,5,5));
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout(5,5)) {{
		setBorder(createEmptyBorder(10, 5, 10, 5));
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel);
	
	JButton reservation = new CustumButton("예매하기");
	int m_no;
	Data movie;
	MovieInfor(int m_no){
		this.m_no = m_no;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		setNorthPanel();
		setInforCenterPanel();
		setChat();
		borderPanel.add(sc);
		add(borderPanel);
		
		reservation.addActionListener(e->{
			if(User.getUser() == null) {
				getter.mg("로그인을 해주세요.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(movie.getInt(2) == 4) {
				LocalDate userb =  LocalDate.parse(User.getUser().get(4).toString());
				int ageY = nows.getYear() - userb.getYear();
				if(nows.getDayOfYear() - userb.getDayOfYear() <= 0) 
					ageY -= 1;
				if(ageY < 19) {
					getter.mg("미성년자는 시청 금지입니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			new reservation(m_no);
		});
		setFrame.setframe(this, "영화 정보", 800, 450);
	}
	
	private void setChat() {
		JPanel panel = new JPanel(new BorderLayout(5,5));
		panel.setBackground(Color.white);
		Color[] colors = {Color.red, Color.blue, Color.yellow, Color.green};
		List<Integer> ageList = new ArrayList<>();
		int totals = 0;
		for(int i = 0; i < 4; i++) {
			List<Data> list = Connections.select("select * from (SELECT \r\n"
					+ "case\r\n"
					+ "	when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 19 then 0\r\n"
					+ "    when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 13 then 1\r\n"
					+ "    when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 5 then 2\r\n"
					+ "    else 3\r\n"
					+ "end as c\r\n"
					+ " FROM moviedb.review \r\n"
					+ "join user on user.u_no = review.u_no\r\n"
					+ "where m_no = ?) as te where c = ?", m_no, i);
			totals += list.size();
			ageList.add(list.size());
		}
		int total = totals;
		JLabel chat = new JLabel() {
			private double start = 90;
			private double end = 0;
			private double theta = 360.0 / total;
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				for(int i = 0; i < 4; i++) {
					g2.setColor(colors[i]);
					start += end;
					end = theta * ageList.get(i);
					Arc2D.Double arc = new Arc2D.Double(30,30, 175, 175, start, end, Arc2D.PIE);
					g2.fill(arc);
				}
			}
		};
		chat.setPreferredSize(new Dimension(250,250));
		
		
		
		String[] ageName = "성인 청소년 어린이 유아".split(" ");
		JPanel gridPanel = new JPanel(new GridLayout(4, 1, 5,5));
		gridPanel.setBackground(Color.white);
		gridPanel.setBorder(createEmptyBorder(30, 0, 80, 0));
		int pursent = 100 / total;
		
		for(int i = 0; i < 4; i++) {
			final int index = i;
			JLabel l = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(colors[index]);
					g.fillRect(5, 0, 20,20);
					g.setColor(Color.black);
					g.setFont(font.deriveFont(20f));
					g.drawString(ageName[index], 28, 18);
					g.drawString(pursent * ageList.get(index) + "%", 90, 18);
					
				}
			};
			gridPanel.add(l);
		}
		
		List<Data> rl = Connections.select("SELECT user.u_no, u_name, re_com FROM moviedb.review \r\n"
				+ "join user on user.u_no = review.u_no where m_no = ?;", m_no);
		JPanel reviewPanel = new JPanel(new GridLayout(0, 1, 4,4));
		reviewPanel.setBackground(Color.white);
		reviewPanel.setPreferredSize(new Dimension(350, 0));
		for(int i = 0; i < rl.size(); i++) {
			JPanel p = new JPanel(new BorderLayout());
			p.setPreferredSize(new Dimension(50,50));
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			
			p.add(new JLabel(getter.getImageIcon("datafiles/user/" + rl.get(i).getInt(0) + ".jpg", 50, 50)), BorderLayout.WEST);
			JPanel bp = new JPanel(new BorderLayout());
			bp.setBackground(Color.white);
			
			JLabel lname = new JLabel(rl.get(i).get(1).toString());
			JLabel re_com = new JLabel(rl.get(i).get(2).toString());
			bp.add(lname, BorderLayout.NORTH);
			bp.add(re_com, BorderLayout.SOUTH);
			
			p.add(bp);
			reviewPanel.add(p);
		}
		
		for(int i = 0; i < 5 - rl.size(); i++) {
			reviewPanel.add(new JLabel("") {{
				setPreferredSize(new Dimension(50,50));
			}});
		}
		panel.add(new JScrollPane(reviewPanel), BorderLayout.EAST);
		panel.add(gridPanel);
		panel.add(chat, BorderLayout.WEST);
		mainPanel.add(panel, BorderLayout.SOUTH);
	}
	
	private void setInforCenterPanel() {
		JTextArea infor = new JTextArea(movie.get(4).toString());
		infor.setFont(font.deriveFont(16f));
		infor.setCursor(getCursor().getDefaultCursor());
		infor.setFocusable(false);
		mainPanel.add(new JScrollPane(infor) {{
			setPreferredSize(new Dimension(0, 200));
		}});
	}
	
	private void setNorthPanel() {
		JPanel p = new JPanel(new BorderLayout(10,10));
		p.setBackground(Color.white);
		p.setPreferredSize(new Dimension(0, 242));
		JLabel img = new JLabel(getter.getImageIcon("datafiles/movies/" + m_no + ".jpg", 165, 240)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/limits/" + movie.getInt(2) + ".png").getImage(), 5, 0, 25, 25, null);
			}
		};
		img.setVerticalAlignment(JLabel.BOTTOM);
		
		JPanel inforPanel = new JPanel(new BorderLayout(0,0));
		inforPanel.setBackground(Color.white);
		JLabel name = new JLabel("제목: " + movie.get(1).toString());
		name.setFont(font);
		
		JTextArea tf = new JTextArea("\n감독: " + movie.get(3)
		+ "\n\n장르: " + Connections.select("select g_name from genre where g_no = ?", movie.get(5)).get(0).get(0)
		+ "\n\n개봉일: " + movie.get(6));
		tf.setFont(font.deriveFont(12f));
		tf.setFocusable(false);
		tf.setCursor(getCursor().getDefaultCursor());
		
		inforPanel.add(name, BorderLayout.NORTH);
		inforPanel.add(tf);
		inforPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBorder(createEmptyBorder(30, 0, 20, 0));
			setBackground(Color.white);
			add(reservation);
		}}, BorderLayout.SOUTH);
		
		p.add(inforPanel);
		p.add(img, BorderLayout.WEST);
		
		mainPanel.add(p, BorderLayout.NORTH);
	}
	
	public static void main(String[] args) {
		new MovieInfor(1);
	}
}
