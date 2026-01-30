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

import javax.imageio.ImageIO;
import javax.swing.*;
import utils.*;
import static javax.swing.BorderFactory.*;

public class Main extends CFrame{
	Font font = new Font("맑은 고딕", 1, 24);
	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 10,10,10));
	}};
	
	JPanel southPanel = new JPanel(new GridLayout(0, 2, 5, 5)) {{
		setBackground(Color.white);
	}};
	
	JPanel adverPanel = new JPanel(null);
	List<JLabel> adverLabels = new ArrayList<>();
	List<Data> adverList = Connections.select("select * from movie where m_no in (6, 2, 32, 9, 18) order by field(m_no,6, 2, 32, 9, 18)");
	
	List<Data> listA = Connections.select("SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation \r\n"
			+ "right join movie on movie.m_no = reservation.m_no\r\n"
			+ "group by movie.m_no order by c desc, movie.m_no limit 10");
	List<JLabel> imgA = new ArrayList<>();
	JScrollPane scA;
	
	List<Data> listB = Connections.select("SELECT movie.*, avg(re_star) as c FROM moviedb.review \r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "group by movie.m_no order by c desc, movie.m_no limit 5");
	List<JLabel> imgB = new ArrayList<>();
	JScrollPane scB;
	
	JButton kiosc = new CustumButton("먹거리키오스크") {{
		setFont(font.deriveFont(0, 9));
	}};
	JButton movieAllShow = new CustumButton("영화 전체보기") {{
		setFont(font.deriveFont(0, 9));
	}};
	
	Thread thread;
	public Main() {
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		setAdver();
		setwPanel();
		setePanel();
		borderPanel.add(adverPanel);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		add(borderPanel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				thread.interrupt();
			}
		});
		setAction();
		setFrame("메인", 650, 575);
	}
	
	private void setePanel() {
		Image img = null;
		try {
			BufferedImage o = ImageIO.read(new File("datafiles/icon/별on.png"));
			BufferedImage r = new BufferedImage(o.getWidth(), o.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for(int y = 0; y < o.getHeight(); y ++) {
				for(int x = 0; x < o.getWidth(); x++) {
					r.setRGB(x, y, (o.getRGB(x, y) == 16777215 ? Color.white.getRGB() : new Color(255, 215, 0).getRGB()));
				}
			}
			img = r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Image star = img;
		
		JPanel panel = new JPanel(new GridLayout(0, 5, 5, 5));
		panel.setBackground(Color.white);
		
		for(int i = 0; i < listB.size(); i++) {
			Data d = listB.get(i);
			JPanel p = new JPanel(new BorderLayout(5, 5));
			p.setPreferredSize(new Dimension(140, 250));
			p.setBackground(Color.white);
			p.setBorder(createEmptyBorder(15, 5, 15, 5));
			
			JLabel point = new JLabel(d.get(9).toString().substring(0, 3), JLabel.RIGHT) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(star, 65, 5, 20, 20, null);
				}
			};
			point.setFont(font);
			
			JLabel movie = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + d.getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			imgB.add(movie);
			
			JLabel name = new JLabel(d.get(1).toString());
			name.setFont(font.deriveFont(1, 13));
			
			p.add(movie);
			p.add(name, BorderLayout.SOUTH);
			p.add(point, BorderLayout.NORTH);
			panel.add(p);
		}
		southPanel.add(scB = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBackground(Color.white);
		}});
	}
	

	private void setwPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(createLineBorder(Color.black));
		panel.setBackground(Color.white);
		
		JPanel butPanel = new JPanel(new FlowLayout(0, 3, 3));
		butPanel.setBackground(Color.white);
		
		butPanel.add(movieAllShow);
		butPanel.add(kiosc);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 10, 5, 5));
		gridPanel.setBackground(Color.white);
		
		for (int i = 0; i < listA.size(); i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout());
			p.setPreferredSize(new Dimension(125, 200));
			p.setBackground(Color.white);
			p.setBorder(createEmptyBorder(15,5,25,5));
			JLabel img = new JLabel(){
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + listA.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					g.setColor(Color.white);
					g.setFont(font.deriveFont(30f));
					g.drawString((index + 1) + "", 10, 30);
				}
			};
			
			JLabel name = new JLabel(listA.get(index).get(1).toString(), JLabel.CENTER);
			name.setFont(font.deriveFont(1, 13));
			
			imgA.add(img);
			p.add(img);
			p.add(name, BorderLayout.SOUTH);
			gridPanel.add(p);
		}
		
		panel.add(scA = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBorder(null);
		}});
		panel.add(butPanel, BorderLayout.NORTH);
		southPanel.add(panel);
	}
	
	private void setAdver() {
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JLabel l = new JLabel(getter.getImage("datafiles/advertising/" + (i+1) + ".jpg", 630, 240)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					
					g.setFont(font);
					g.drawString(adverList.get(index).get(1).toString(), 10, 150);
					
					g.setFont(font.deriveFont(0, 13));
					g.drawString(adverList.get(index).get(3).toString(), 10, 180);
					
				}
			};
			l.setBounds(i * 630, 0, 630, 240);
			adverPanel.add(l);
			adverLabels.add(l);
		}
		
		thread = new Thread(()->{
			try {
				while(!thread.isInterrupted()) {
					Thread.sleep(2000);
					for(int i = 0; i <= 630;i++) {
						adverLabels.get(0).setBounds(-i, 0, 630, 240);
						adverLabels.get(1).setBounds(630-i, 0, 630, 240);
						Thread.sleep(1);
					}
					adverLabels.add(adverLabels.get(0));
					adverLabels.remove(0);
				}
				revalidate();
				repaint();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		thread.start();
	}
	
	private void setAction() {
		MouseAdapter m1 = mouseDrag(scA);
		MouseAdapter m2 = mouseDrag(scB);
		
		scA.addMouseListener(m1);
		scA.addMouseMotionListener(m1);
		scB.addMouseListener(m2);
		scB.addMouseMotionListener(m2);
		
		for (int i = 0; i < 10; i++) {
			final int index = i;
			imgA.get(index).addMouseListener(m1);
			imgA.get(index).addMouseMotionListener(m1);
			imgA.get(index).addMouseListener(mouseClikedAction(listA, index));
			if(i >= 5) continue;
			imgB.get(index).addMouseListener(m2);
			imgB.get(index).addMouseMotionListener(m2);
			imgB.get(index).addMouseListener(mouseClikedAction(listB, index));
			adverLabels.get(index).addMouseListener(mouseClikedAction(adverList, index));
		}
	}
	
	private MouseAdapter mouseClikedAction(List<Data> list, int i) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(list.get(i).get(0));
			}
		};
	}
	private MouseAdapter mouseDrag(JScrollPane sc) {
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
