package main;

import static javax.swing.BorderFactory.*;

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

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JFrame{
	Font font = new Font("맑은 고딕", 1, 24);
	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBorder(createEmptyBorder(0, 5,5,5));
		setBackground(Color.WHITE);
	}};
	
	JPanel adverPanel = new JPanel(null) {{
		setBackground(Color.white);
	}};
	List<Data> adverList = Connections.select("select * from movie where m_no in (6,2,32,9,18) order by field(m_no,6,2,32,9,18)");
	List<JLabel> adverLabels = new ArrayList<>();
	
	List<Data> listB = Connections.select("SELECT movie.m_no, m_name, avg(re_star) as a FROM moviedb.review \r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "group by movie.m_no order by a desc, m_no limit 5;");
	List<JLabel> imgB = new ArrayList<>();
	JScrollPane scB;
	
	List<Data> listA = Connections.select("SELECT movie.m_no, m_name, count(movie.m_no) as s FROM moviedb.reservation\r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ " group by movie.m_no order by s desc, movie.m_no limit 10;");
	List<JLabel> imgA = new ArrayList<>();
	JScrollPane scA;
	
	JButton kiosc = new CustumButton("먹거리키오스크") {{
		setFont(font.deriveFont(10f));
	}};
	JButton movieAllShowBut = new CustumButton("영화 전체보기") {{
		setFont(font.deriveFont(10f));
	}};
	
	JPanel southPanel = new JPanel(new GridLayout(0, 2, 5,5)) {{
		setPreferredSize(new Dimension(0, 250));
		setBackground(Color.white);
	}};
	
	Thread thread;
	
	Main(){
		setAdverPanel();
		borderPanel.add(adverPanel);
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		add(borderPanel);
		setSouthWestPanel();
		setSouthEastPanel();
		setAction();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(thread != null) thread.interrupt();
			}
		});
		setFrame.setframe(this, "메인", 650, 600);
	}
	
	private void setSouthWestPanel() {
		JPanel panel = new JPanel(new BorderLayout(10,10));
		panel.setBackground(Color.white);
		panel.setBorder(createLineBorder(Color.black));
		
		panel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)) {{
			setBackground(Color.white);
			add(movieAllShowBut);
			add(kiosc);
		}}, BorderLayout.NORTH);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 10, 5, 5));
		gridPanel.setBackground(Color.white);
		for(int i = 0; i < 10; i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setPreferredSize(new Dimension(130, 0));
			p.setBackground(Color.white);
			p.setBorder(createEmptyBorder(5,5,10,5));
			
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + listA.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					g.setColor(Color.white);
					g.setFont(font.deriveFont(1, 40f));
					g.drawString((index + 1) + "", 5, 35);
				}
			};
			imgA.add(img);
			
			p.add(img);
			p.add(new JLabel(listA.get(i).get(1).toString(), JLabel.CENTER) {{
				setFont(font.deriveFont(12f));
			}}, BorderLayout.SOUTH);
			gridPanel.add(p);
		}
		panel.add(scA = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBorder(null);
			setBackground(Color.white);
		}});
		southPanel.add(panel);
	}
	
	private void setSouthEastPanel() {
		Image starImage = null;
		try {
			BufferedImage oimg = ImageIO.read(new File("datafiles/icon/별on.png"));
			BufferedImage rimg = new BufferedImage(oimg.getWidth(), oimg.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for(int y = 0; y < oimg.getHeight(); y++) {
				for(int x = 0; x < oimg.getWidth(); x++) {
					rimg.setRGB(x, y, (oimg.getRGB(x, y) != 16777215 ? new Color(255,215,0).getRGB() : Color.white.getRGB()));
				}
			}
			starImage = rimg;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Image star = starImage;
		JPanel panel = new JPanel(new GridLayout(0, 5, 5,5));
		panel.setBackground(Color.white);
		panel.setBorder(createEmptyBorder(10,0, 10, 0));
		for(int i = 0; i < 5; i++) {
			final int index = i;
			
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setBorder(createEmptyBorder(0,5,0,5));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(140, 0));
			
			JLabel reviewPoint = new JLabel(listB.get(i).get(listB.get(i).size() - 1).toString().substring(0, 3), JLabel.RIGHT) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(star, 70, 5, 20, 20, null);
				}
			};
			reviewPoint.setFont(font.deriveFont(22f));
			
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + listB.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			imgB.add(img);
			p.add(img);
			p.add(reviewPoint, BorderLayout.NORTH);
			p.add(new JLabel(listB.get(i).get(1).toString(), JLabel.LEFT) {{
				setFont(font.deriveFont(12f));
			}}, BorderLayout.SOUTH);
			panel.add(p);
		}
		southPanel.add(scB = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBorder(createLineBorder(Color.black));
		}});
	}

	private void setAdverPanel() {
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JLabel l = new JLabel(getter.getImageIcon("datafiles/advertising/" + (i+1) + ".jpg", 640, 240)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(font);
					g.drawString(adverList.get(index).get(1).toString(), 10, 150);
					g.setFont(font.deriveFont(0, 14f));
					g.drawString(adverList.get(index).get(3).toString(), 10, 170);
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
					for(int i = 0; i < 641; i++) {
						adverLabels.get(0).setBounds(-i, 0, 640, 240);
						adverLabels.get(1).setBounds(640-i, 0, 640, 240);
						Thread.sleep(1);
					}
					adverLabels.add(adverLabels.get(0));
					adverLabels.remove(0);
					revalidate();
					repaint();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		thread.start();
	}
	
	private void setAction() {
		MouseAdapter m1 = setDraggAction(scA);
		MouseAdapter m2 = setDraggAction(scB);
		scA.addMouseListener(m1);
		scA.addMouseMotionListener(m1);
		scB.addMouseListener(m2);
		scB.addMouseMotionListener(m2);
		for(int i = 0; i < 10; i++) {
			imgA.get(i).addMouseListener(m1);
			imgA.get(i).addMouseMotionListener(m1);
			imgA.get(i).addMouseListener(setMouseCilcked(listA.get(i)));
			if(i >= 5) continue;
			imgB.get(i).addMouseListener(m2);
			imgB.get(i).addMouseMotionListener(m2);
			imgB.get(i).addMouseListener(setMouseCilcked(listB.get(i)));
			adverLabels.get(i).addMouseListener(setMouseCilcked(adverList.get(i)));
		}
	}
	
	private MouseAdapter setMouseCilcked(Data data) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(data.get(0));
			}
		};
	}
	private MouseAdapter setDraggAction(JScrollPane sc) {
		return new MouseAdapter() {
			private int x;
			@Override
			public void mousePressed(MouseEvent e) { x = e.getX(); }
			@Override
			public void mouseDragged(MouseEvent e) {
				JScrollBar sb = sc.getHorizontalScrollBar();
				sb.setValue(sb.getValue() + (x - e.getX()));
				x = e.getX();
			}
		};
	}
	public static void main(String[] args) {
		new Main();
	}
}
