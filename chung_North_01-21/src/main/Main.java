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
	JPanel borderPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0,5,5,5));
	}};
	
	JPanel adverPanel = new JPanel(null) {{
		setBackground(Color.white);
	}};
	Thread thread;
	List<Data> adverList = Connections.select("select * from movie where m_no in(6, 2, 32, 9, 18) order by field(m_no,6, 2, 32, 9, 18)");
	List<JLabel> adverLabels = new ArrayList<>();
	
	JButton movieAllShow = new CustumButton("영화 전체보기") {{
		setFont(font.deriveFont(0, 9));
	}};
	JButton kiosc = new CustumButton("먹거리키오스크") {{
		setFont(font.deriveFont(0, 9));
	}};
	
	List<Data> listA = Connections.select("SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation \r\n"
			+ "right join movie on movie.m_no = reservation.m_no\r\n"
			+ "group by m_no order by c desc limit 10;");
	List<JLabel> imgA = new ArrayList<>();
	
	List<Data> listB = Connections.select("SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "group by movie.m_no order by a desc, movie.m_no limit 5");
	List<JLabel> imgB = new ArrayList<>();
	
	JScrollPane scA;
	JScrollPane scB;
	
	JPanel southPanel = new JPanel(new GridLayout(0, 2, 3, 3)) {{
		setBackground(Color.white);
	}};
	
	public Main() {
		setAdverPanel();
		setWestPanel();
		setEastPanel();
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		borderPanel.add(adverPanel);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		add(borderPanel);
		setAction();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				thread.interrupt();
			}
		});
		SetFrame.setFrame(this, "메인", 650, 550);
	}
	
	private void setEastPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 5, 0, 0));
		panel.setBackground(Color.white);
		Image imgs = null;
		try {
			BufferedImage oimg = ImageIO.read(new File("datafiles/icon/별on.png"));
			BufferedImage rimg = new BufferedImage(oimg.getWidth(), oimg.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for(int i = 0; i < oimg.getHeight(); i++) 
				for(int j = 0; j < oimg.getWidth(); j ++)
					rimg.setRGB(j, i, (oimg.getRGB(j, i) != 16777215 ? new Color(255, 215, 0).getRGB() : Color.white.getRGB()));
			imgs = rimg;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Image img = imgs;
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JLabel l = new JLabel(listB.get(i).get(listB.get(i).size() - 1).toString().substring(0, 3), JLabel.RIGHT) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(img, 50, 10, 17, 17, null);
				}
			};
			l.setFont(font);
			
			JLabel movieImage = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + listB.get(index).get(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			imgB.add(movieImage);
			
			JLabel name = new JLabel(listB.get(index).get(1).toString(), JLabel.LEFT);
			name.setFont(font.deriveFont(1, 13));
			
			JPanel p = new JPanel(new BorderLayout(10, 10));
			p.setPreferredSize(new Dimension(120, 0));
			p.setBorder(createEmptyBorder(10,5,10,5));
			p.setBackground(Color.white);
			
			p.add(l, BorderLayout.NORTH);
			p.add(movieImage);
			p.add(name, BorderLayout.SOUTH);
			panel.add(p);
		}
		southPanel.add(scB = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		scB.setBorder(createLineBorder(Color.black));
	}
	
	private void setWestPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		panel.setBorder(createLineBorder(Color.black));
		
		panel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)) {{
			setBackground(Color.white);
			add(movieAllShow);
			add(kiosc);
		}}, BorderLayout.NORTH);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 10));
		gridPanel.setBackground(Color.white);
		
		for(int i = 0; i < 10; i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(3,3));
			p.setPreferredSize(new Dimension(120, 210));
			p.setBackground(Color.white);
			p.setBorder(createEmptyBorder(20,5,20,5));
			
			JLabel l = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + listA.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					g.setFont(font.deriveFont(34f));
					g.setColor(Color.white);
					g.drawString((index + 1) + "", 10, 30);
				}
			};
			
			JLabel name = new JLabel(listA.get(i).get(1).toString(), JLabel.CENTER);
			name.setFont(font.deriveFont(12f));
			
			imgA.add(l);
			p.add(l);
			p.add(name, BorderLayout.SOUTH);
			gridPanel.add(p);
		}
		
		panel.add(scA = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		scA.setBorder(null);
		southPanel.add(panel);
		
	}
	
	private void setAdverPanel() {
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JLabel l = new JLabel(getter.getImageIcon("datafiles/advertising/" + (i + 1) + ".jpg", 640, 240)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(font);
					g.drawString(adverList.get(index).get(1).toString(), 10, 170);
					g.setFont(font.deriveFont(0, 13f));
					g.drawString(adverList.get(index).get(3).toString(), 10, 190);
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
						adverLabels.get(1).setBounds(640-i, 0, 640, 240);
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
		MouseAdapter m1 = setMouseDragg(scA);
		MouseAdapter m2 = setMouseDragg(scB);
		
		scA.addMouseListener(m1);
		scA.addMouseMotionListener(m1);
		
		scB.addMouseListener(m2);
		scB.addMouseMotionListener(m2);
		
		for(int i = 0; i < 10; i++) {
			imgA.get(i).addMouseListener(m1);
			imgA.get(i).addMouseMotionListener(m1);
			imgA.get(i).addMouseListener(setMouseClick(listA, i));
			if(i >= 5) continue;
			adverLabels.get(i).addMouseListener(setMouseClick(adverList, i));
			imgB.get(i).addMouseListener(m2);
			imgB.get(i).addMouseMotionListener(m2);
			imgB.get(i).addMouseListener(setMouseClick(listB, i));
		}
	}
	
	private MouseAdapter setMouseClick(List<Data> list, int index) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//new MovieInfor(list.get(index).getInt(0));
				dispose();
			}
		};
	}
	private MouseAdapter setMouseDragg(JScrollPane sc) {
		return new MouseAdapter() {
			private int x;
			@Override
			public void mousePressed(MouseEvent e) {
				x = e.getX();
			}
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
