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
import java.awt.geom.Arc2D.Double;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
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
	
	Font font = new Font("맑은 고딕", 0, 12);
	JPanel borderPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5,5,5,5));
	}};
	JPanel mainPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10, 5, 5, 5));
	}};
	JScrollPane sc = new JScrollPane(mainPanel);
	
	JPanel moviePoster = new JPanel(new BorderLayout(5,5));
	JButton but = new CustumButton("예매하기");
	JTextArea movieInfor = new JTextArea();
	JScrollPane movieInforSc = new JScrollPane(movieInfor);
	JPanel chatPanel = new JPanel(new BorderLayout());
	Data movie;
	
	int u_no = 0, m_no = 0;
	double start = 90, end = 0;
	public MovieInfor(int u_no, int m_no) {
		this.u_no = u_no; this.m_no = m_no;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		
		borderPanel.add(new NorthPanel(this, u_no), BorderLayout.NORTH);
		borderPanel.add(sc);
		add(borderPanel);
		setMoviePoster();
		setMovieInfor();
		if(Connections.select("select * from review where m_no = ?", m_no).isEmpty()) {
			chatPanel.setBackground(Color.white);
			chatPanel.setPreferredSize(new Dimension(200, 300));
			mainPanel.add(chatPanel, BorderLayout.SOUTH);
		}else setChat();
		new A_setFrame(this, "영화 정보", 725, 500);
	}
	
	private void setMoviePoster() {
		JLabel movieImage = new JLabel(getImage("datafiles/movies/" + m_no + ".jpg", 150, 225)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/limits/" + movie.get(2) + ".png").getImage(), 5, 0, 25, 25, null);
			}
		};
		movieImage.setVerticalAlignment(JLabel.BOTTOM);
		movieImage.setPreferredSize(new Dimension(150, 227));
		
		JPanel moviePosterInfor = new JPanel(new BorderLayout());
		moviePosterInfor.setBackground(Color.white);
		moviePosterInfor.add(new JLabel("제목: " + movie.get(1)) {{
			setFont(font.deriveFont(1, 22f));
		}}, BorderLayout.NORTH);
		String infor = "\n감독: " + movie.get(3) + "\n\n장르: " + Connections.select("select g_name from genre where g_no = ?", movie.get(5)).get(0).get(0) + "\n\n개봉일: " + movie.get(6);
		moviePosterInfor.add(new JTextArea(infor) {{
			setFocusable(false);
			setCursor(getCursor().getDefaultCursor());
		}});
		moviePosterInfor.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			setBorder(createEmptyBorder(0, 0, 30, 0));
			add(but);
		}}, BorderLayout.SOUTH);
		moviePoster.setBackground(Color.white);
		moviePoster.add(movieImage, BorderLayout.WEST);
		moviePoster.add(moviePosterInfor);
		mainPanel.add(moviePoster,BorderLayout.NORTH);
	}
	
	private void setMovieInfor() {
		movieInfor.setText(movie.get(4).toString());
		movieInfor.setFont(font.deriveFont(1, 18f));
		movieInfor.setFocusable(false);
		movieInfor.setCursor(getCursor().getDefaultCursor());
		
		movieInforSc.setBackground(Color.white);
		movieInforSc.setPreferredSize(new Dimension(0, 200));
		movieInforSc.setBorder(createCompoundBorder(createEmptyBorder(15, 0, 0, 0), createLineBorder(Color.black)));
		mainPanel.add(movieInforSc);
	}
	
	private void setChat() {
		Color[] colors = {Color.red, Color.blue, Color.yellow, Color.green};
		List<Data> rv = Connections.select(
				  "SELECT user.u_no, u_name, re_com FROM moviedb.review\r\n"
				  + "join user on user.u_no = review.u_no\r\n"
				  + " where m_no = ?;", m_no);
		List<Integer> age = new ArrayList<>();
		int total = 0;
		for(int i = 0; i < 4; i++) {
			int size = Connections.select("SELECT ages, u_name, re_com\r\n"
					+ "FROM (\r\n"
					+ "    SELECT \r\n"
					+ "        CASE \r\n"
					+ "            WHEN TIMESTAMPDIFF(YEAR, u_birth, NOW()) >= 19 THEN 0\r\n"
					+ "            WHEN TIMESTAMPDIFF(YEAR, u_birth, NOW()) >= 13 THEN 1\r\n"
					+ "            WHEN TIMESTAMPDIFF(YEAR, u_birth, NOW()) >= 5 THEN 2\r\n"
					+ "            ELSE 3\r\n"
					+ "        END AS ages, u_name, re_com\r\n"
					+ "    FROM moviedb.review \r\n"
					+ "    JOIN user ON user.u_no = review.u_no\r\n"
					+ "    WHERE m_no = ?\r\n"
					+ ") AS subquery\r\n"
					+ "WHERE ages = ?;", m_no, i).size();
			age.add(size);
			total += size;
		}
		double theta = 360.0 / total;
		JLabel l = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				for(int i = 0; i < 4; i++) {
					start = start + end;
					end = age.get(i) * theta;
					g2.setColor(colors[i]);
					Arc2D.Double arc = new Arc2D.Double(20, 12, 175, 175, start, end, Arc2D.Double.PIE);
					g2.fill(arc);
				}
			}
		};
		l.setPreferredSize(new Dimension(200, 200));
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 1));
		gridPanel.setBorder(createEmptyBorder(20, 10, 100, 0));
		gridPanel.setBackground(Color.white);
		
		int pursent = 100 / total;
		String[] ageNames = "성인,청소년,어린이,유아".split(",");
		for(int i = 0; i < 4; i++) {
			int index = i;
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			
			Font font = this.font.deriveFont(1, 18f);
			JLabel rect = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(colors[index]);
					g.fillRect(0, 5, 15, 15);
				}
			};
			rect.setPreferredSize(new Dimension(15, 0));
			
			JLabel ageName = new JLabel(ageNames[i]);
			ageName.setFont(font);
			
			JLabel pursentLabel = new JLabel(age.get(i) * pursent + "%", JLabel.LEFT);
			pursentLabel.setPreferredSize(new Dimension(50, 0));
			pursentLabel.setFont(font);
			
			p.add(rect, BorderLayout.WEST);
			p.add(ageName);
			p.add(pursentLabel, BorderLayout.EAST);
			gridPanel.add(p);
		}
		chatPanel.setBackground(Color.white);
		chatPanel.add(l, BorderLayout.WEST);
		chatPanel.add(gridPanel);
		JPanel scPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		scPanel.setBackground(Color.white);
		for(Data d : rv) {
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			p.add(new JLabel(getImage("datafiles/user/" + d.get(0) + ".jpg", 60, 60)), BorderLayout.WEST);
			p.add(new JTextArea(d.get(1) + "\n\n" + d.get(2)) {{
				setLineWrap(true);
				setBorder(createEmptyBorder(7, 7, 7, 0));
				setFont(font.deriveFont(1, 11));
			}});
			p.setBorder(createLineBorder(Color.black));
			p.setPreferredSize(new Dimension(0, 60));
			scPanel.add(p);
		}
		int fn = 4 - rv.size();
		for(int i = 0; i < (fn < 0 ? 0 : fn); i++) {
			scPanel.add(new JLabel(""));
		}
		chatPanel.add(new JScrollPane(scPanel) {{
			setPreferredSize(new Dimension(330, 0));
		}}, BorderLayout.EAST);
		mainPanel.add(chatPanel, BorderLayout.SOUTH);
	}
	
	private ImageIcon getImage(String file, int w, int h) {
		return new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(w, h, 4));
	}
	
	public static void main(String[] args) {
		new MovieInfor(1, 1);
	}
}
