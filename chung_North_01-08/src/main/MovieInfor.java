package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.geom.Arc2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MovieInfor extends JFrame{

	Color[] colors = {Color.red, Color.blue, Color.yellow, Color.green};
	JPanel borderPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 5, 5, 5));
	}};
	JPanel mainPanel = new JPanel(new BorderLayout(20, 20)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10, 5, 5, 5));
	}};
	
	JScrollPane sc = new JScrollPane(mainPanel);
	JPanel moviePoster = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
	}};
	JTextArea infor = new JTextArea() {{
		setFont(new Font("맑은 고딕", 1, 20));
		setLineWrap(true);
		setFocusable(false);
		setCursor(getCursor().getDefaultCursor());
	}};
	JPanel userReviewPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setPreferredSize(new Dimension(0, 300));
	}};
	JButton ㅇㅁ = new CustumButton("예매하기");
	
	Data movie;
	int u_no = 0, m_no = 0;
	double start = 0, end = 0;
	public MovieInfor(int u_no, int m_no) {
		this.u_no = u_no; this.m_no = m_no;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		JPanel northPanel = new NorthPanel(u_no, this);
		borderPanel.add(northPanel, BorderLayout.NORTH);
		setPoster();
		Chat();
		infor.setText(movie.get(4).toString());
		mainPanel.add(new JScrollPane(infor) {{
			setPreferredSize(new Dimension(0, 200));
		}});
		mainPanel.add(moviePoster, BorderLayout.NORTH);
		mainPanel.add(userReviewPanel, BorderLayout.SOUTH);
		borderPanel.add(sc);
		add(borderPanel);
		new A_setFrame(this, "영화 정보", 800, 400);
	}
	
	private void setPoster() {
		moviePoster.add(new JLabel(getImage("datafiles/movies/" + m_no + ".jpg", 150, 225)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(getImage("datafiles/limits/" + movie.get(2) + ".png", 40, 40).getImage(), 10, 0, 30, 30, null);
			}
		}, BorderLayout.WEST);
		JPanel name = new JPanel(new BorderLayout(10, 10)) {{ setBackground(Color.white); }};
		JLabel title = new JLabel("제목: " + movie.get(1).toString());
		title.setFont(new Font("맑은 고딕", 1, 26));
		JTextArea ta = new JTextArea("감독: " + movie.get(3).toString() + "\n\n장르: " + Connections.select("select * from genre where g_no = ?", movie.get(5)).get(0).get(1)
		+ "\n\n개봉일: " + movie.get(6).toString()) {{
			setLineWrap(true);
			setFocusable(false);
			setFont(new Font("맑은 고딕", 0, 12));
		}};
		name.add(title, BorderLayout.NORTH);
		name.add(ta);
		name.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBorder(createEmptyBorder(0,0,30,0));
			setBackground(Color.white);
			add(ㅇㅁ);
		}}, BorderLayout.SOUTH);
		moviePoster.add(name);
	}
	
	
	private void Chat() {
		List<Data> review = Connections.select("SELECT review.*, TIMESTAMPDIFF(YEAR, u_birth, NOW()), user.* FROM moviedb.review \r\n"
				+ "join user on user.u_no = review.u_no\r\n"
				+ "where m_no = ?;", m_no);
		List<List<Integer>> ages = new ArrayList<>();
		for(int i = 0; i < 4; i++) 
			ages.add(new ArrayList<>());
		int total = review.size();
		JPanel rePanel = new JPanel(new GridLayout(0, 1, 5,5));
		for(int i = 0; i < review.size(); i++) {
			JPanel reviewPanel = new JPanel(new BorderLayout());
			reviewPanel.setBackground(Color.white);
			reviewPanel.setBorder(createLineBorder(Color.black));
			reviewPanel.add(new JLabel(getImage("datafiles/user/" + review.get(i).get(1) + ".jpg", 50, 50)), BorderLayout.WEST);
			reviewPanel.add(new JTextArea(review.get(i).get(8) + "\n" + review.get(i).get(3)) {{
				setLineWrap(true);
				setFocusable(false);
			}});
			rePanel.add(reviewPanel);
			int age = Integer.parseInt(review.get(i).get(6).toString());
			ages.get(age >= 19 ? 0 : (age >= 13 ? 1 : (age >= 5 ? 2 : 3))).add(i);
		}
		JLabel chat = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				for(int i = 0; i < 4; i++) {
					g.setColor(colors[i]);
					start = start + end;
					end = 360.0 / total * ages.get(i).size();
					Arc2D.Double arc = new Arc2D.Double(30, 50, 200, 200, 90 + start, end, Arc2D.PIE);
					g2.fill(arc);
				}
			}
		};
		chat.setPreferredSize(new Dimension(250, 0));
		JPanel gridPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		gridPanel.setBackground(Color.white);
		gridPanel.setBorder(createEmptyBorder(20, 0, 100, 0));
		String[] ageTitle = "성인,청소년,어린이,유아".split(",");
		for(int i = 0; i < 4; i++) {
			int index = i;
			double dou = ((double) ages.get(i).size() / total) * 100;
			gridPanel.add(new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(colors[index]);
					g.fillRect(20, 10, 30, 30);
					g.setColor(Color.black);
					g.setFont(new Font("맑은 고딕", 1, 22));
					g.drawString(" " + ageTitle[index] + " ", 50, 35);
					g.drawString((dou == 0 ? 0 : dou) + "%", 130, 35);
				}
			});
		}
		
		for(int i = 0; i < 4; i++) {
			rePanel.add(new JLabel());// 칸수 채우기 용
		}
		userReviewPanel.add(new JScrollPane(rePanel) {{
			setPreferredSize(new Dimension(300, 0));
		}}, BorderLayout.EAST);
		userReviewPanel.add(chat, BorderLayout.WEST);
		userReviewPanel.add(gridPanel);
	}
	private ImageIcon getImage(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	public static void main(String[] args) {
		new MovieInfor(1, 1);
	}
}
