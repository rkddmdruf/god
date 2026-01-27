package main;

import utils.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import static javax.swing.BorderFactory.*;

public class MovieInfor extends JFrame{
	JFrame f = this;
	Font font = new Font("맑은 고딕", 1, 24);
	
	JPanel borderPanel = new JPanel(new BorderLayout(7,7)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,7,7,7));
	}};
	
	JPanel mainPanel = new JPanel(new BorderLayout(10,10));
	JScrollPane sc = new JScrollPane(mainPanel);
	
	JButton but = new CustumButton("예매하기");
	int m_no;
	Data movie;
	public MovieInfor(int m_no) {
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		this.m_no = m_no;
		borderPanel.add(new NorthPanel(f) {
			@Override
			void setLogin() {
				super.setLogin();
				login.addActionListener(e->{
					getter.r2 = getter.getR();
					getter.setR(()->{ new MovieInfor(m_no); });
				});
			};
		}, BorderLayout.NORTH);
		
		mainPanel.setBorder(createEmptyBorder(7,7,7,7));
		mainPanel.setBackground(Color.white);
		sc.setBackground(Color.white);
		
		setNorthPanel();
		setInforPanel();
		setSouthPanel();
		
		borderPanel.add(sc);
		add(borderPanel);
		but.addActionListener(e->{
			if(User.getUser() == null) {
				getter.mg("로그인을 해주세요.", JOptionPane.ERROR_MESSAGE);
				getter.r2 = getter.getR();
				getter.setR(() -> { new MovieInfor(m_no); });
				new Login();
				dispose();
				return;
			}
			if(movie.getInt(2) == 4) {
				LocalDate nows = LocalDate.of(2025 - 19, 9, 10);
				LocalDate userD = LocalDate.parse(User.getUser().get(4).toString());
				if(!nows.isAfter(userD)) {
					getter.mg("미성년자는 시청 금지입니다.", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			new Reservation(m_no);
			getter.setR(()->{ new MovieInfor(m_no); });
			dispose();
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(getter.r2 != null) {
					getter.r2.run();
					getter.r2 = null;
					return;
				}
				getter.fromMove(f);
			}
		});
		
		setFrame.setframe(this, "영화 정보", 800, 400);
	}
	
	private void setSouthPanel() {
		Color[] colors = {Color.red, Color.blue, Color.yellow, Color.green};
		
		JPanel panel = new JPanel(new BorderLayout(5,5));
		panel.setPreferredSize(new Dimension(250, 250));
		panel.setBackground(Color.white);
		
		List<Integer> ageList = Arrays.asList(0, 0, 0, 0);
		int totals = 0;
		
		for(int i = 0; i < 4; i++) {
			List<Data> list = Connections.select("select * from (SELECT \r\n"
					+ "case \r\n"
					+ "	when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 19 then 0\r\n"
					+ "    when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 13 then 1\r\n"
					+ "    when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 5 then 2\r\n"
					+ "    else 3\r\n"
					+ "end as caseData\r\n"
					+ " FROM moviedb.review \r\n"
					+ "join user on user.u_no = review.u_no\r\n"
					+ "where m_no = ?) as t where caseData = ?;", m_no, i);
			int size = list.size();
			totals += size;
			ageList.set(i, size);
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
					Arc2D.Double arc = new Arc2D.Double(30,40, 170, 170, start, end, Arc2D.PIE);
					g2.fill(arc);
				}
				g2.dispose();
			}
		};
		chat.setPreferredSize(new Dimension(230,250));
		
		JPanel gridPanel = new JPanel(new GridLayout(4, 1, 5, 5));
		gridPanel.setBackground(Color.white);
		gridPanel.setBorder(createEmptyBorder(20, 0, 100, 0));
		
		String[] ageName = "성인 청소년 어린이 유아".split(" ");
		int pursent = 100 / total;
		for(int i = 0; i < 4; i++) {
			final int index = i;
			JLabel l = new JLabel() {
				protected void paintComponent(Graphics g) {
					g.setColor(colors[index]);
					g.fillRect(0, 0, 22, 22);
					g.setColor(Color.black);
					g.setFont(font.deriveFont(20f));
					g.drawString(ageName[index], 25, 20);
					g.drawString((pursent * ageList.get(index)) + "%", 100, 20);
				};
			};
			gridPanel.add(l);
		}
		
		JPanel reviewPanel = new JPanel(new GridLayout(0, 1, 5,5));
		reviewPanel.setBackground(Color.white);
		
		List<Data> list = Connections.select("SELECT user.u_no, u_name, re_com FROM moviedb.review \r\n"
				+ "join user on user.u_no = review.u_no\r\n"
				+ "where m_no = ?;", m_no);
		for(int i = 0; i < list.size(); i++) {
			JPanel p = new JPanel(new BorderLayout(10,10));
			p.setBorder(createLineBorder(Color.black));
			p.setPreferredSize(new Dimension(0, 50));
			p.setBackground(Color.white);
			
			p.add(new JLabel(getter.getImage("datafiles/user/" + list.get(i).get(0) + ".jpg", 50,50)), BorderLayout.WEST);
			
			JPanel userP = new JPanel(new BorderLayout());
			userP.setBackground(Color.white);
			
			userP.add(new JLabel(list.get(i).get(1).toString()) {{
				setFont(font.deriveFont(12f));
			}}, BorderLayout.NORTH);
			
			userP.add(new JLabel(list.get(i).get(2).toString()) {{
				setFont(font.deriveFont(0, 12));
			}});
			
			p.add(userP);
			reviewPanel.add(p);
		}
		
		for(int i = 0; i < (5 - list.size()); i++)
			reviewPanel.add(new JLabel(""));
		
		JScrollPane scRe = new JScrollPane(reviewPanel);
		scRe.setPreferredSize(new Dimension(365, 250));
		
		panel.add(scRe, BorderLayout.EAST);
		panel.add(gridPanel);
		panel.add(chat, BorderLayout.WEST);
		mainPanel.add(panel, BorderLayout.SOUTH);
	}
	
	private void setInforPanel() {
		JTextArea infor = new JTextArea(movie.get(4).toString());
		infor.setFont(font.deriveFont(20f));
		infor.setFocusable(false);
		infor.setCursor(getCursor().getDefaultCursor());
		
		JScrollPane inforScroll = new JScrollPane(infor);
		inforScroll.setPreferredSize(new Dimension(0, 250));
		inforScroll.setBorder(createCompoundBorder(createEmptyBorder(20, 0, 0, 0), createLineBorder(Color.black)));
		inforScroll.setBackground(Color.white);
		
		mainPanel.add(inforScroll);
	}
	
	private void setNorthPanel() {
		JPanel nPanel = new JPanel(new BorderLayout(7,7));
		nPanel.setBackground(Color.white);
		
		JLabel movieImage = new JLabel(getter.getImage("datafiles/movies/" + m_no + ".jpg", 160, 240)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/limits/" + movie.get(2) + ".png").getImage(), 5, 10, 20, 20, null);
			}
		};
		movieImage.setPreferredSize(new Dimension(160, 252));
		movieImage.setVerticalAlignment(JLabel.BOTTOM);
		
		JPanel inforPanel = new JPanel(new BorderLayout(16,16));
		inforPanel.setBackground(Color.white);
		
		String genre = Connections.select("select g_name from genre where g_no = ?", movie.get(5)).get(0).get(0).toString();
		
		inforPanel.add(new JLabel("제목: " + movie.get(1)) {{
			setFont(font);
		}}, BorderLayout.NORTH);
		
		inforPanel.add(new JTextArea("감독: " + movie.get(3) + "\n\n장르: " + genre  + "\n\n개봉일: " + movie.get(6)));
		
		inforPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			setBorder(createEmptyBorder(0, 0, 30, 0));
			add(but);
		}}, BorderLayout.SOUTH);
		
		nPanel.add(inforPanel);
		nPanel.add(movieImage, BorderLayout.WEST);
		mainPanel.add(nPanel, BorderLayout.NORTH);
	}
	
	public static void main(String[] args) {
		new MovieInfor(1);
	}
}
