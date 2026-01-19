package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JFrame{
	Font font = new Font("맑은 고딕", 1, 22);
	JPanel borderPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5,5,5,5));
	}};
	JPanel southPanel = new JPanel(new GridLayout(0, 2, 5, 5));
	JPanel adverPanel = new JPanel(null);
	List<Data> adverList = Connections.select("select * from movie where m_no in(6, 2, 32, 9, 18) order by field(m_no, 6, 2, 32, 9, 18)");
	List<JLabel> adverLabels = new ArrayList<>();
	
	List<Data> reviewList = Connections.select("SELECT movie.*, avg(re_star) as a FROM moviedb.review \r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "group by movie.m_no order by a desc, m_no limit 5;");
	List<JLabel> reviewLabels = new ArrayList<>();
	
	List<Data> reservationList = Connections.select("SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation \r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ "group by reservation.m_no order by c desc, m_no limit 10;");
	List<JLabel> reservationLabels = new ArrayList<>();
	
	JButton movieAllShow = new CustumButton("영화 전체보기") {{
		setFont(font.deriveFont(0,9));
	}};
	JButton kiosc = new CustumButton("먹거리키오스크") {{
		setFont(font.deriveFont(0,9));
	}};
	
	JScrollPane scB, scA;
	Thread thread;
	public Main() {
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		borderPanel.add(adverPanel);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		setSouthWestPanel();
		setSouthEastPanel();
		setAdverPanel();
		setAction();
		add(borderPanel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(thread != null) thread.interrupt();
			}
		});
		setFrame.setframe(this, "메인", 650, 580);
	}
	
	private void setSouthEastPanel() {
		
		JPanel ePanel = new JPanel(new GridLayout(1, 5, 10, 10));
		ePanel.setPreferredSize(new Dimension(600, 200));
		ePanel.setBackground(Color.white);
		scB = new JScrollPane(ePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scB.setBackground(Color.white);
		scB.setBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(15, 3, 15, 3)));
		
		Image imgs = null;
		try {
			BufferedImage oimg = ImageIO.read(new File("datafiles/icon/별on.png"));
			BufferedImage rimg = new BufferedImage(oimg.getWidth(), oimg.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for(int y = 0; y < oimg.getHeight(); y ++) {
				for(int x = 0; x < oimg.getWidth(); x ++) {
					rimg.setRGB(x, y, oimg.getRGB(x, y) == 16777215 ? Color.white.getRGB() : new Color(255, 215, 0).getRGB());
				}	
			}
			imgs = rimg.getScaledInstance(20, 20, 4);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Image img = imgs;
		
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setBackground(Color.white);
			JLabel star = new JLabel(reviewList.get(i).get(9).toString().substring(0, 3), JLabel.RIGHT) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(img, 49, 7, 21, 21, null);
				}
			};
			star.setFont(font.deriveFont(25f));
			
			p.add(star, BorderLayout.NORTH);
			JLabel m = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + reviewList.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			reviewLabels.add(m);
			p.add(m);
			p.add(new JLabel(reviewList.get(i).get(1).toString(), JLabel.LEFT), BorderLayout.SOUTH);
			ePanel.add(p);
		}
		southPanel.add(scB);
	}
	
	private void setSouthWestPanel() {
		JPanel wPanel = new JPanel(new BorderLayout());
		wPanel.setBackground(Color.white);
		wPanel.setBorder(createLineBorder(Color.black));
		
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3));
		butPanel.setBackground(Color.white);
		butPanel.add(movieAllShow);
		butPanel.add(kiosc);
		
		wPanel.add(butPanel, BorderLayout.NORTH);
		
		JPanel westMoviePanel = new JPanel(new GridLayout(1, 10, 5,5));
		westMoviePanel.setBackground(Color.white);
		westMoviePanel.setBorder(createEmptyBorder(0, 5, 15, 5));
		scA = new JScrollPane(westMoviePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scA.setBorder(createLineBorder(Color.white));
		for(int i = 0; i < 10; i++) {
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(110, 180));
			JLabel m = new JLabel(getter.getImageIcon("datafiles/movies/" + reservationList.get(i).getInt(0) + ".jpg", 110, 150));
			p.add(m);
			reservationLabels.add(m);
			p.add(new JLabel(reservationList.get(i).get(1).toString(), JLabel.CENTER) {{
				setFont(font.deriveFont(12f));
			}}, BorderLayout.SOUTH);
			westMoviePanel.add(p);
		}
		wPanel.add(scA);
		southPanel.add(wPanel);
	}
	
	private void setAdverPanel() {
		for (int i = 0; i < 5; i++) {
			final int index = i;
			JLabel l = new JLabel(getter.getImageIcon("datafiles/advertising/" + (i + 1) + ".jpg", 640, 240)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(font);
					g.drawString(adverList.get(index).get(1).toString(), 10, 170);
					g.setFont(font.deriveFont(0, 13));
					g.drawString(adverList.get(index).get(3).toString(), 10, 194);
				}
			};
			l.setBounds(i * 640, 0, 640, 240);
			adverLabels.add(l);
			adverPanel.add(l);
		}
		
		thread = new Thread(()->{
			try {
				while(!Thread.interrupted()) {
					Thread.sleep(2000);
					for(int i = 0; i < 640; i++) {
						adverLabels.get(0).setBounds(-i, 0, 640, 240);
						adverLabels.get(1).setBounds(640 - i, 0, 640, 240);
						Thread.sleep(1);
					}
					adverLabels.add(adverLabels.get(0));
					adverLabels.remove(0);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		thread.start();
	}
	
	private void setAction() {
		
		
		MouseAdapter m1 = setJScrollPaneAction(scA);
		MouseAdapter m2 = setJScrollPaneAction(scB);
		scA.addMouseListener(m1);
		scA.addMouseMotionListener(m1);
		scB.addMouseListener(m2);
		scB.addMouseMotionListener(m2);
		for(int i = 0 ; i < 10; i++) {
			reservationLabels.get(i).addMouseListener(setClickedMouseAction(reservationList, i));
			reservationLabels.get(i).addMouseListener(m1);
			reservationLabels.get(i).addMouseMotionListener(m1);
			if(i >= 5) continue;
			reviewLabels.get(i).addMouseListener(setClickedMouseAction(reviewList, i));
			reviewLabels.get(i).addMouseListener(m2);
			reviewLabels.get(i).addMouseMotionListener(m2);
			adverLabels.get(i).addMouseListener(setClickedMouseAction(adverList, i));
		}
	}
	
	private MouseAdapter setClickedMouseAction(List<Data> list, int index) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(list.get(index).get(0));
			}
		};
	}
	private MouseAdapter setJScrollPaneAction(JScrollPane sc) {
		return new MouseAdapter() {
			private int start;
			@Override
			public void mousePressed(MouseEvent e) { 	start = e.getX(); 	}
			@Override
			public void mouseDragged(MouseEvent e) {
				JScrollBar sb = sc.getHorizontalScrollBar();
				sb.setValue(sb.getValue() + (start - e.getX()));
				start = e.getX();
			}
		};
	}
	public static void main(String[] args) {
		new Main();
	}
}
