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

import static javax.swing.BorderFactory.* ;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JFrame{
	Font font = new Font("맑은 고딕", 1, 24);
	JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0 ,10, 10, 10));
	}};
	JPanel adverPanel = new JPanel(null);
	JPanel southPanel = new JPanel(new GridLayout(0, 2, 3, 3)) {{
		
		setPreferredSize(new Dimension(0, 240));
		setBackground(Color.white);
	}};
	
	JButton kiosc = new CustumButton("먹거리키오스크") {{
		setFont(font.deriveFont(0, 9));
	}};
	JButton movieAllShow = new CustumButton("영화 전체보기") {{
		setFont(font.deriveFont(0, 9));
	}};
	
	List<JLabel> adverLabels = new ArrayList<>();
	List<Data> adverlist = Connections.select("select * from movie where m_no in (6, 2, 32, 9, 18) order by field(m_no, 6, 2, 32, 9, 18)");
	
	
	List<JLabel> imgA = new ArrayList<>();
	List<Data> listA = Connections.select("select movie.*, count(movie.m_no) as c from reservation \r\n"
			+ "right join movie on movie.m_no = reservation.m_no \r\n"
			+ "group by movie.m_no order by c desc, movie.m_no limit 10");
	JScrollPane scA;
	
	List<JLabel> imgB = new ArrayList<>();
	List<Data> listB = Connections.select("SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "group by movie.m_no order by a desc, movie.m_no limit 5");
	JScrollPane scB;
	Thread thread;
	Main(){
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		setAdverPanel();
		setwPanel();
		setePanel();
		borderPanel.add(adverPanel);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		add(borderPanel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(thread != null) thread.interrupt();
			}
		});
		setAction();
		setFrame.setframe(this, "메인", 650, 550);
	}
	
	private void setePanel() {
		JPanel mainPanel = new JPanel(new GridLayout(1, 5, 5, 5));
		mainPanel.setBackground(Color.white);
		Image imgs = null;
		try {
			BufferedImage oi = ImageIO.read(new File("datafiles/icon/별on.png"));
			BufferedImage ri = new BufferedImage(oi.getWidth(), oi.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for(int i = 0; i < oi.getHeight(); i++)
				for(int j = 0; j < oi.getWidth(); j++)
					ri.setRGB(j, i, (oi.getRGB(j, i) != 16777215 ? new Color(255, 215, 0).getRGB() : Color.white.getRGB()));
			imgs = ri;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Image img = imgs;
		for(int i = 0; i < 5; i++) {
			final int index = i;
			
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			p.setBorder(createEmptyBorder(15,5,15,5));
			p.setPreferredSize(new Dimension(140, 190));
			JLabel star = new JLabel(listB.get(i).get(listB.get(i).size() - 1).toString().substring(0, 3), JLabel.RIGHT) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(img, 50, 10, 20, 20, null);
				}
			};
			star.setFont(font);
			
			JLabel movieImg = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + listB.get(index).get(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
		
			JLabel name = new JLabel(listB.get(index).get(1).toString(), JLabel.LEFT);
			imgB.add(movieImg);
			p.add(star, BorderLayout.NORTH);
			p.add(movieImg);
			p.add(name, BorderLayout.SOUTH);
			mainPanel.add(p);
		}
		southPanel.add(scB = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBackground(Color.white);
			setBorder(createLineBorder(Color.black));
		}});
	}
	private void setwPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.white);
		mainPanel.setBorder(createLineBorder(Color.black));
		
		mainPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)) {{
			setBackground(Color.white);
			add(movieAllShow);
			add(kiosc);
		}}, BorderLayout.NORTH);
		
		JPanel gridPanel = new JPanel(new GridLayout(1, 10, 5, 5));
		gridPanel.setBackground(Color.white);
		
		for(int i = 0; i < 10; i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(5, 5));
			p.setPreferredSize(new Dimension(120, 180));
			p.setBackground(Color.white);
			p.setBorder(createEmptyBorder(15,5,15,5));
			
			JLabel l = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + listA.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					g.setColor(Color.white);
					g.setFont(font.deriveFont(30f));
					g.drawString((index + 1) + "", 10, 30);
				}
			};
			imgA.add(l);
			p.add(l);
			p.add(new JLabel(listA.get(index).get(1).toString(), JLabel.CENTER), BorderLayout.SOUTH);
			gridPanel.add(p);
		}
		
		mainPanel.add(scA = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBorder(null);
		}});
		southPanel.add(mainPanel);
	}
	
	private void setAdverPanel() {
		for(int i = 0; i < 5;i++) {
			final int index = i;
			Data d = adverlist.get(i);
			JLabel l = new JLabel(getter.getImageIcon("datafiles/advertising/" + (i + 1) + ".jpg", 630, 230)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(font);
					g.drawString(d.get(1).toString(), 10, 150);
					g.setFont(font.deriveFont(0, 14));
					g.drawString(d.get(3).toString(), 10, 180);
				}
			};
			l.setBounds(i * 630, 0, 630, 230);
			
			adverLabels.add(l);
			adverPanel.add(l);
		}
		
		thread = new Thread(()->{
			try {
				while(!Thread.interrupted()) {
					Thread.sleep(2000);
					for(int i = 0; i < 630; i++) {
						adverLabels.get(0).setBounds(-i, 0,  630, 230);
						adverLabels.get(1).setBounds(630-i, 0,  630, 230);
						
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
		MouseAdapter m1 = setMouseAction(scA);
		MouseAdapter m2 = setMouseAction(scB);
		
		scA.addMouseListener(m1);
		scA.addMouseMotionListener(m1);
		scB.addMouseListener(m2);
		scB.addMouseMotionListener(m2);
		
		for(int i = 0; i < 10; i++) {
			imgA.get(i).addMouseListener(m1);
			imgA.get(i).addMouseMotionListener(m1);
			imgA.get(i).addMouseListener(setMouseClicked(listA, i));
			if(i >= 5) return;
			imgB.get(i).addMouseListener(m2);
			imgB.get(i).addMouseMotionListener(m2);
			imgB.get(i).addMouseListener(setMouseClicked(listB, i));
			adverLabels.get(i).addMouseListener(setMouseClicked(adverlist, i));
		}
	}
	
	private MouseAdapter setMouseClicked(List<Data> list, int i) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new MovieInfor(list.get(i).getInt(0));
				dispose();
				return;
			}
		};
	}
	private MouseAdapter setMouseAction(JScrollPane s) {
		return new MouseAdapter() {
			private int x;
			@Override
			public void mousePressed(MouseEvent e) {
				x = e.getX();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				JScrollBar sb = s.getHorizontalScrollBar();
				sb.setValue(sb.getValue() + (x - e.getX()));
				x = e.getX();
			}
		};
	}
	public static void main(String[] args) {
		new Main();
	}
}
