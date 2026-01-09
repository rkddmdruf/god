package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MovieInfor extends JFrame{

	Connections c = new Connections();
	JPanel borderPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
	}};
	JPanel mainPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
	}};
	
	JScrollPane sc = new JScrollPane(mainPanel);
	JPanel moviePoster = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
	}};
	JTextArea infor = new JTextArea();
	JPanel userReviewPanel = new JPanel(new BorderLayout());
	JButton ㅇㅁ = new CustumButton("예매하기");
	
	Data movie;
	int u_no = 0, m_no = 0;
	public MovieInfor(int u_no, int m_no) {
		this.u_no = u_no; this.m_no = m_no;
		movie = c.select("select * from movie where m_no = ?", m_no).get(0);
		JPanel northPanel = new NorthPanel(u_no, this);
		borderPanel.add(northPanel, BorderLayout.NORTH);
		setPoster();
		mainPanel.add(moviePoster, BorderLayout.NORTH);
		borderPanel.add(sc);
		add(borderPanel);
		new A_setFrame(this, "영화 정보", 700, 400);
	}
	
	private void setPoster() {
		moviePoster.add(new JLabel(getImage("datafiles/movies/" + m_no + ".jpg", 150, 225)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(getImage("datafiles/limits/1.png", 40, 40).getImage(), 10, 0, 30, 30, null);
			}
		}, BorderLayout.WEST);
		JPanel name = new JPanel(new BorderLayout(10, 10)) {{ setBackground(Color.white); }};
		JLabel title = new JLabel("제목: " + movie.get(1).toString());
		title.setFont(new Font("맑은 고딕", 1, 26));
		JTextArea ta = new JTextArea("감독: " + movie.get(3).toString() + "\n\n장르: " + "범죄" + "\n\n개봉일: " + movie.get(6).toString()) {{
			setLineWrap(true);
			setFocusable(false);
			setFont(new Font("맑은 고딕", 0, 12));
		}};
		name.add(title, BorderLayout.NORTH);
		name.add(ta);
		name.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
			setBackground(Color.white);
			add(ㅇㅁ);
		}}, BorderLayout.SOUTH);
		moviePoster.add(name);
	}
	
	private ImageIcon getImage(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	public static void main(String[] args) {
		new MovieInfor(1, 1);
	}
}
