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
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JFrame{
	Font font = new Font("맑은 고딕", 1, 24);

	JPanel borderPanel = new JPanel(new BorderLayout(5,5));
	
	JPanel centerPanel = new JPanel(null);
	List<JLabel> adverLabels = new ArrayList<>();
	List<Data> adverList = Connections.select("select * from movie where m_no in (6,2,32,9,18) order by field(m_no,6,2,32,9,18)");
	
	JPanel southPanel = new JPanel(new GridLayout(0, 2, 5, 5));
	
	JScrollPane scB;
	List<JLabel> reviewLabels = new ArrayList<>();
	List<Data> reviewList = Connections.select("SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
			+ "join movie on movie.m_no = review.m_no\r\n"
			+ "group by movie.m_no order by a desc, movie.m_no limit 5;");
	
	JScrollPane scA;
	List<JLabel> reservationLabels = new ArrayList<>();
	List<Data> reservationList = Connections.select("SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation\r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ "group by movie.m_no order by c desc, movie.m_no limit 10");
	NorthPanel northPanel = new NorthPanel(this);
	Thread thread;
	public Main() {
		borderPanel.setBackground(Color.white);
		borderPanel.setBorder(createEmptyBorder(0, 5, 5, 5));
		
		borderPanel.add(northPanel, BorderLayout.NORTH);
		borderPanel.add(centerPanel);
		add(borderPanel);
		setCenterPanel();
		setWestPanel();
		setEastPanel();
		setAction();
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(thread != null) thread.interrupt();
			}
		});
		A_setFrame.setting(this, "메인", 650, 560);
	}
	
	private void setCenterPanel() {
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					g.drawImage(new ImageIcon("datafiles/advertising/" + (index+1) + ".jpg").getImage(), 0, 0, 630, 230, null);
					g.setColor(Color.white);
					g.setFont(font);
					g.drawString(adverList.get(index).get(1).toString(), 10, 150);
					g.setFont(font.deriveFont(0, 14));
					g.drawString(adverList.get(index).get(3).toString(), 10, 170);
				}
			};
			img.setBounds(i * 630, 0, 630, 230);
			adverLabels.add(img);
			centerPanel.add(img);
		}
		thread = new Thread(()->{
			try {
				while(!Thread.interrupted()) {
					Thread.sleep(2000);
					for(int i = 0; i < 630; i++) {
						adverLabels.get(0).setBounds(-i, 0, 630, 230);
						adverLabels.get(1).setBounds(630 - i, 0, 630, 230);
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

	private void setWestPanel() {
		JButton movieAllShow = new CustumButton("영화 전체보기");
		movieAllShow.setFont(font.deriveFont(1,8));
		
		JButton kiosc = new CustumButton("먹거리키오스크");
		kiosc.setFont(font.deriveFont(1,8));
		
		JPanel westPanel = new JPanel(new BorderLayout());
		westPanel.setBorder(createLineBorder(Color.black));
		
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		butPanel.setBackground(Color.white);
		butPanel.add(movieAllShow);
		butPanel.add(kiosc);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 10));
		
		for(int i = 0; i < 10; i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(5, 5));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(115, 200));
			p.setBorder(createEmptyBorder(15,5,20,5));
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(font.deriveFont(1, 26));
					g.drawString(new StringBuilder(index + 1).toString(), 5, 5);
					g.drawImage(new ImageIcon("datafiles/movies/" + reservationList.get(index).get(0) + ".jpg").getImage(), 0, 0, 105, 145, null);
				}
			};
			reservationLabels.add(img);
			p.add(img);
			p.add(new JLabel(reservationList.get(index).get(1).toString()), BorderLayout.SOUTH);
			gridPanel.add(p);
		}
		westPanel.add(scA = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		scA.setBorder(null);
		westPanel.add(butPanel, BorderLayout.NORTH);
		southPanel.add(westPanel);
	}

	private void setEastPanel() {
		southPanel.setBackground(Color.white);
		JPanel eastPanel = new JPanel(new GridLayout(0, 5, 10, 10));
		eastPanel.setBackground(Color.white);
		for(int i = 0; i < 5; i++) {
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setBackground(Color.white);
			p.setBorder(createEmptyBorder(5,5,15,5));
			p.setPreferredSize(new Dimension(120, 225));
			
			JLabel reviewPoint = null;
			
			try {
				BufferedImage original = ImageIO.read(new File("datafiles/icon/별on.png"));
				int xx = original.getWidth(), yy = original.getHeight();
				BufferedImage recolor = new BufferedImage(xx, yy, BufferedImage.TYPE_INT_ARGB);
				for(int y = 0; y < yy; y++) {
					for(int x = 0; x < xx; x++) {
						recolor.setRGB(x, y, original.getRGB(x, y) != 16777215 ? new Color(255,215,0).getRGB() : 0);
					}
				}
				reviewPoint = new JLabel(reviewList.get(i).get(9).toString().substring(0, 3), JLabel.RIGHT) {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.drawImage(recolor.getScaledInstance(40, 40, 4), 50, 10, 20, 20, null);
					}
				};
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			reviewPoint.setFont(font.deriveFont(1, 25));
			
			JLabel img = new JLabel(getter.getImageIcon("datafiles/movies/" + reviewList.get(i).get(0) + ".jpg", 110, 145));
			img.setVerticalAlignment(JLabel.BOTTOM);
			
			JLabel name = new JLabel(reviewList.get(i).get(1).toString());
			name.setFont(font.deriveFont(1, 12));
			
			p.add(reviewPoint, BorderLayout.NORTH);
			p.add(img);
			p.add(name, BorderLayout.SOUTH);
			reviewLabels.add(img);
			eastPanel.add(p);
		}
		southPanel.add(scB = new JScrollPane(eastPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	}
	
	private void setAction() {
		MouseAdapter m1 = mouseActions(scA);
		MouseAdapter m2 = mouseActions(scB);
		scA.addMouseListener(m1);
		scA.addMouseMotionListener(m1);
		
		scB.addMouseListener(m2);
		scB.addMouseMotionListener(m2);	
		for(int i = 0; i < reservationList.size(); i++) {
			reservationLabels.get(i).addMouseListener(m1);
			reservationLabels.get(i).addMouseMotionListener(m1);
			reservationLabels.get(i).addMouseListener(mouseClickedNewMovieInfor(reservationList, i));
			if(i >= reviewList.size()) continue;
			reviewLabels.get(i).addMouseListener(m2);
			reviewLabels.get(i).addMouseMotionListener(m2);
			reviewLabels.get(i).addMouseListener(mouseClickedNewMovieInfor(reviewList, i));
			adverLabels.get(i).addMouseListener(mouseClickedNewMovieInfor(adverList, i));
		}
	}
	
	private MouseAdapter mouseClickedNewMovieInfor(List<Data> list, int index) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new MovieInfor(Integer.parseInt(list.get(index).get(0).toString()), true);
				dispose();
			}
		};
	}
	
	private MouseAdapter mouseActions(JScrollPane sc) {
		return new MouseAdapter() {
			private int x;
			@Override public void mousePressed(MouseEvent e) { x = e.getX(); }
			@Override
			public void mouseDragged(MouseEvent e) {
				JScrollBar scbar = sc.getHorizontalScrollBar();
				scbar.setValue(scbar.getValue() + (x - e.getX()));
				x = e.getX();
			}
		};
	}
	public static void main(String[] args) {
		new Main();
	}
}
