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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static javax.swing.BorderFactory.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JFrame{
	Connections c = new Connections();
	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,5,5,5));
	}};
	
	JPanel centerPanel = new JPanel(null) {{
		setBackground(Color.white);
	}};
	int u_no = 0;
	
	JPanel southPanel = new JPanel(new GridLayout(0, 2, 5, 5)) {{
		setBackground(Color.white);
		setPreferredSize(new Dimension(0, 225));
	}};
	JPanel movieA = new JPanel(new GridLayout(0, 10, 11, 11)) {{
		setBackground(Color.white);
	}};
	JPanel movieB = new JPanel(new GridLayout(0, 5, 11, 11)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc1 = new JScrollPane(movieA, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
		setBorder(createLineBorder(Color.blue));
		setBackground(Color.white);
	}};
	JScrollPane sc2 = new JScrollPane(movieB, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
		setBorder(createLineBorder(Color.black));
		setBackground(Color.white);
	}};
	
	JButton movieAllShow = new CustumButton("영화 전체보기");
	CustumButton kiosc = new CustumButton("먹거리키오스크");
	List<Data> ads = c.select("select * from movie where m_no in (6, 2, 32, 9, 18) ORDER BY FIELD(m_no, 6, 2, 32, 9, 18)");
	List<JPanel> panels = new ArrayList<>();
	
	List<Data> listA = c.select("SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation "
			+ "join movie on movie.m_no = reservation.m_no "
			+ "group by movie.m_no order by c desc, m_no limit 10;");
	List<Data> listB = c.select("SELECT movie.*, avg(re_star) a FROM moviedb.review "
			+ "join movie on movie.m_no = review.m_no "
			+ "group by movie.m_no order by a desc, m_no limit 5;");
	int start1 = 0, start2 = 1;
	int x1 = 0, x2 = 0;
	Main(int u_no){
		this.u_no = u_no;
		
		borderPanel.add(new NorthPanel(u_no, this), BorderLayout.NORTH);
		add(borderPanel);
		
		setAdverPanel();
		setSouthPanel();
		setAction();
		new A_setFrame(this, "메인", 600, 500);
	}
	
	private void setSouthPanel() {
		JPanel buttonP = new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);			
		}};
		buttonP.add(movieAllShow);
		buttonP.add(kiosc);
		JPanel westPanel = new JPanel(new BorderLayout(5, 5)) {{
			setBorder(createLineBorder(Color.black));
			setBackground(Color.white);
		}};
		
		
		for(int i = 0; i < 10; i++) {
			final int index = i;
			
			JPanel p = new JPanel(new BorderLayout(5, 5)) {{
				setBackground(Color.white);
			}};

			p.add(new JLabel(getImages("datafiles/movies/" + listA.get(i).get(0) + ".jpg", 100, 140)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(new Font("맑은 고딕", 1, 30));
					g.drawString((index + 1) + "", 10,30);
				}
			});
		
			p.add(new JLabel(listA.get(i).get(1).toString()) {{
				setPreferredSize(new Dimension(100, 20));
				setFont(new Font("맑은 고딕", 0, 11));
				setHorizontalAlignment(JLabel.CENTER);
			}}, BorderLayout.SOUTH);
			movieA.add(p);
		}
		
		sc1.setBorder(createEmptyBorder(0, 10, 15, 0));
		sc1.setPreferredSize(new Dimension(290, sc1.getSize().height));
		
		for(int i = 0; i < 5; i++) {
			final int index = i;
			Image image = null;
			try {
				BufferedImage imgs = ImageIO.read(new File("datafiles/icon/별on.png"));
				BufferedImage recolored = new BufferedImage(imgs.getWidth(), imgs.getHeight(), BufferedImage.TYPE_INT_ARGB);
				for (int y = 0; y < imgs.getHeight(); y++) {
					for (int x = 0; x < imgs.getWidth(); x++) {
						if(imgs.getRGB(x, y) != 16777215) {
							recolored.setRGB(x, y, new Color(255, 210, 2).getRGB());
						}
					}
				}
				image = recolored.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			Image images = image;
			JLabel star = new JLabel(listB.get(index).get(9).toString().substring(0, 3), JLabel.RIGHT) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(images, 40, 10, 20, 20, null);
				}
			};
			star.setFont(new Font("맑은 고딕", 1, 24));
			JLabel movieImg = new JLabel(new ImageIcon(new ImageIcon("datafiles/movies/" + listB.get(index).get(0) + ".jpg").getImage().getScaledInstance(110, 130, Image.SCALE_SMOOTH)));
			JLabel movieName = new JLabel(listB.get(index).get(1).toString(), JLabel.LEFT) {{
				setFont(new Font("맑은 고딕", 1, 12));
			}};
			movieB.setBorder(createEmptyBorder(10, 5, 15, 5));
			movieB.add(new JPanel(new BorderLayout(5, 5)) {{
				setBackground(Color.white);
				setPreferredSize(new Dimension(110, 190));
				add(star, BorderLayout.NORTH);
				add(movieImg);
				add(movieName, BorderLayout.SOUTH);
			}});
		}
		
		westPanel.add(buttonP, BorderLayout.NORTH);
		westPanel.add(sc1, BorderLayout.WEST);
		southPanel.add(westPanel);
		southPanel.add(sc2);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
	}
	
	private void setAdverPanel() {
		
		for (int i = 0; i < ads.size(); i++) {
			int index = i;
			
			JPanel p = new JPanel() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					
					Graphics2D g2 = (Graphics2D) g;
					
					g2.drawImage(getImages("datafiles/advertising/" + (index + 1) + ".jpg", 590, 200).getImage(), 0, 0, 590, 200, null);
					
					g2.setColor(Color.white);
					g2.setFont(new Font("맑은 고딕", 1, 20));
					g2.drawString(ads.get(index).get(1).toString(), 10, 150);
					
					g2.setFont(new Font("맑은 고딕", 0, 14));
					g2.drawString(ads.get(index).get(3).toString(), 10, 180);
				}
			};
			
			p.setBounds(i * 590, 0, 590, 200);
			centerPanel.add(p);
			panels.add(p);
		}
		
		new Thread(() -> {
			try {
				while (true) {
					
					Thread.sleep(2000);
					
					for (int i = 0; i < 590; i+=4) {
						panels.get(0).setBounds(-i - 1, 0, 590, 200);
						panels.get(1).setBounds(590 - i - 1, 0, 590, 200);
						
						Thread.sleep(1);
						
						repaint();
						revalidate();
					}
					
					panels.add(panels.get(0));
					panels.remove(0);
					
				}
			} catch (Exception e) {
			}
		}).start();
		
		borderPanel.add(centerPanel);
	}
	
	private void setAction() {
		
		MouseAdapter m1 = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				start1 = e.getX();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				x1 = x1 + (start1 - e.getX());
				System.out.println(x1);
				if(x1 < 0) x1 = 0;
				if(x1 >= 820) x1 = 820;
				System.out.println(sc1.getHorizontalScrollBar().getMaximumSize().width);
				sc1.getHorizontalScrollBar().setValue(x1);
				start1 = e.getX();
			}
		};
		sc1.addMouseListener(m1);
		sc1.addMouseMotionListener(m1);
		for(int i = 0; i < 10; i++) {
			int index = i;
			JPanel p = (JPanel) movieA.getComponent(index);
			p.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(listA.get(index).get(0));
				}
			});
			p.addMouseListener(m1);
			p.addMouseMotionListener(m1);
		}
		
		MouseAdapter m2 = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				start2 = e.getX();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				x2 = x2 + (start2 - e.getX());
				System.out.println(x2);
				if(x2 < 0) x2 = 0;
				if(x2 >= 315) x2 = 315;
				sc2.getHorizontalScrollBar().setValue(x2);
				start2 = e.getX();
			}
		};
		
		sc2.addMouseListener(m2);
		sc2.addMouseMotionListener(m2);
		for(int i = 0; i < 5; i++) {
			int index = i;
			JPanel p = (JPanel) movieB.getComponent(index);
			p.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(listB.get(index).get(0));
				}
			});
			p.addMouseListener(m2);
			p.addMouseMotionListener(m2);
		}
		
		for(int i = 0; i < 5; i++) {
			final int index = i;
			panels.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(Integer.parseInt(ads.get(index).get(0).toString()));
				}
			});
		}
	}
	
	private ImageIcon getImages(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	public static void main(String[] args) {
		new Main(1);
	}
}
