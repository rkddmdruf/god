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
import static javax.swing.BorderFactory.*;

public class Main extends JFrame{
	Font font = new Font("맑은 고딕", 1, 24);
	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 10,10,10));
	}};
	JPanel southPanel = new JPanel(new GridLayout(0, 2, 5, 5)) {{
		setPreferredSize(new Dimension(0, 250));
		setBackground(Color.white);
	}};
	
	JPanel adverPanel = new JPanel(null) {{
		setBackground(Color.white);
	}};
	List<Data> adverList = Connections.select("select * from movie where m_no in (6, 2, 32, 9, 18) order by field(m_no,6, 2, 32, 9, 18)");
	List<JLabel> adverLabels = new ArrayList<>();
	
	List<Data> listB = Connections.select("SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "group by movie.m_no order by a desc, movie.m_no limit 5;");
	List<JLabel> imgB = new ArrayList<>();
	JScrollPane scB;
	
	List<Data> listA = Connections.select("SELECT movie.*, count(r_no) as c FROM moviedb.reservation\r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ "group by movie.m_no order by c desc, movie.m_no limit 10;");
	List<JLabel> imgA = new ArrayList<>();
	JScrollPane scA;
	
	JButton movieAllShow = new CustumButton("영화 전체보기") {{
		setFont(font.deriveFont(0, 9));
	}};
	JButton kiosc = new CustumButton("먹거리 키오스크") {{
		setFont(font.deriveFont(0, 9));
	}};
	
	Thread thread;
	Main(){
		
		setAdverPanel();
		setwPanel();
		setePanel();
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		setAction();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				thread.interrupt();
				getter.setRun(()->{new Main();});
			}
		});
		add(borderPanel);
		
		
		setFrame.setframe(this, "메인", 650, 560);
	}
	
	private void setwPanel() {
		JPanel wPanel = new JPanel(new BorderLayout(10,10));
		wPanel.setBackground(Color.white);
		wPanel.setBorder(createLineBorder(Color.black));
		
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		butPanel.setBackground(Color.white);
		
		butPanel.add(movieAllShow);
		butPanel.add(kiosc);
		
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 10, 5, 5));
		gridPanel.setBackground(Color.white);
		
		for(int i = 0; i < 10; i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(10,10));
			p.setBorder(createEmptyBorder(7, 5, 15, 5));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(135, 0));
			
			JLabel l = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + listA.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					g.setColor(Color.white);
					g.setFont(font.deriveFont(32f));
					g.drawString((index + 1) + "" , 20,30);
				}
			};
			imgA.add(l);
			
			JLabel name = new JLabel(listA.get(i).get(1).toString(), 0);
			name.setFont(font.deriveFont(13f));
			
			p.add(l);
			p.add(name, BorderLayout.SOUTH);
			
			gridPanel.add(p);
		}
		
		scA = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scA.setBackground(Color.white);
		scA.setBorder(null);
		wPanel.add(butPanel, BorderLayout.NORTH);
		wPanel.add(scA);
		southPanel.add(wPanel);
	}
	
	private void setePanel() {
		JPanel ePanel = new JPanel(new GridLayout(1, 5, 5, 5));
		ePanel.setBackground(Color.white);
		
		Image img = null;
		try {
			BufferedImage o = ImageIO.read(new File("datafiles/icon/별on.png"));
			BufferedImage r = new BufferedImage(o.getWidth(), o.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for(int y = 0; y < o.getHeight(); y++) {
				for(int x = 0; x < o.getHeight(); x++) {
					r.setRGB(x, y, (o.getRGB(x, y) == 16777215 ? Color.white.getRGB() : new Color(255, 215, 0).getRGB()));
				}
			}
			img = r;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Image star = img;
		Image i1 = new ImageIcon("datafiles/로고1.jpg").getImage();
		Image i2 = new ImageIcon("datafiles/로고2.jpg").getImage();
		System.out.println(i1.equals(i2));
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(5,5));
			p.setPreferredSize(new Dimension(140, 0));
			p.setBackground(Color.white);
			p.setBorder(createEmptyBorder(10, 5, 5, 5));
			
			JLabel l = new JLabel(listB.get(i).get(listB.get(i).size() - 1).toString().substring(0, 3), JLabel.RIGHT) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(star, 65, 8, 20, 20, null);
				}
			};
			l.setFont(font);
			
			JLabel movie = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + listB.get(index).get(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			imgB.add(movie);
			
			JLabel name = new JLabel(listB.get(index).get(1).toString(), JLabel.LEFT);
			name.setFont(font.deriveFont(14f));
			
			p.add(l, BorderLayout.NORTH);
			p.add(movie);
			p.add(name, BorderLayout.SOUTH);
			
			ePanel.add(p);
		}
		
		southPanel.add(scB = new JScrollPane(ePanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBorder(createLineBorder(Color.black));
			setBackground(Color.white);
		}});
	}
	
	private void setAdverPanel() {
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JLabel l = new JLabel(getter.getImage("datafiles/advertising/" + (i + 1) + ".jpg", 630, 230)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(font);
					g.drawString(adverList.get(index).get(1).toString(), 10, 170);
					g.setFont(font.deriveFont(0, 13));
					g.drawString(adverList.get(index).get(3).toString(), 10, 190);
				}
			};
			
			l.setBounds(i * 630, 0, 630, 230);
			adverPanel.add(l);
			adverLabels.add(l);
		}
		
		thread = new Thread(()->{
			try {
				while(!Thread.interrupted()) {
					Thread.sleep(2000);
					for(int i = 0; i <= 630; i++) {
						adverLabels.get(0).setBounds(-i, 0, 630, 230);
						adverLabels.get(1).setBounds(630-i, 0, 630, 230);
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
		borderPanel.add(adverPanel);
	}
	
	private void setAction() {
		MouseAdapter m1 = mouseDragAction(scA);
		MouseAdapter m2 = mouseDragAction(scB);
		
		scA.addMouseListener(m1);
		scA.addMouseMotionListener(m1);
		
		scB.addMouseListener(m2);
		scB.addMouseMotionListener(m2);
		for(int i = 0; i < 10; i++) {
			imgA.get(i).addMouseListener(m1);
			imgA.get(i).addMouseMotionListener(m1);
			imgA.get(i).addMouseListener(mouseClickedAction(listA, i));
			if(i >= 5) continue;
			imgB.get(i).addMouseListener(m2);
			imgB.get(i).addMouseMotionListener(m2);
			imgB.get(i).addMouseListener(mouseClickedAction(listB, i));
			adverLabels.get(i).addMouseListener(mouseClickedAction(adverList, i));
		}
	}
	
	private MouseAdapter mouseDragAction(JScrollPane sc) {
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
	private MouseAdapter mouseClickedAction(List<Data> list, int i) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(list.get(i).getInt(0));;
			}
		};
	}
	public static void main(String[] args) {
		new Main();
	}
}
