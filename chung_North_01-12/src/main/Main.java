package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.ScrollPane;
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

	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,10,10,10));
	}};
	JPanel adverPanel = new JPanel(null);
	List<Data> adverMovieList = Connections.select("SELECT * FROM moviedb.movie where m_no in(6, 2, 32, 9, 18)\r\n"
			+ " order by field(m_no, 6, 2, 32, 9, 18);");
	List<JLabel> adverLabel = new ArrayList<>();
	Thread thread;
	
	JPanel southPanel = new JPanel(new GridLayout(0,2, 5, 5)) {{
		setBackground(Color.white);
	}};
	
	Font butFont = new Font("맑은 고딕", 0, 9);
	JButton movieAllShow = new CustumButton("영화 전체보기") {{
		setFont(butFont);
	}};
	JButton kiosc = new CustumButton("먹거리키오스크") {{
		setFont(butFont);
	}};
	JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
		setBackground(Color.white);
		add(movieAllShow);
		add(kiosc);
	}};
	
	JPanel panelA = new JPanel(new BorderLayout(15, 15)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(5, 5, 5, 5));
	}};
	JPanel panelB = new JPanel(new GridLayout(0, 5, 10, 10)) {{
		setBorder(createEmptyBorder(15,5, 10, 5));
		setBackground(Color.white);
	}};
	List<JLabel> labelA = new ArrayList<>();
	List<JLabel> labelB = new ArrayList<>();
	List<Data> dataA = Connections.select("SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation "
			+ "join movie on movie.m_no = reservation.m_no "
			+ "group by movie.m_no order by c desc, movie.m_no limit 10;");
	List<Data> dataB = Connections.select("SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
			+ "join movie on movie.m_no = review.m_no\r\n"
			+ "group by movie.m_no order by a desc, movie.m_no limit 5;");
	JScrollPane scA;
	JScrollPane scB;
	int start1, start2, x1, x2, u_no;
	public Main(int u_no) {
		this.u_no = u_no;
		mainPanel();
		southLEFTPanel();
		southRIGHTPanel();
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		borderPanel.add(new NorthPanel(this, u_no), BorderLayout.NORTH);
		add(borderPanel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(thread != null) thread.interrupt();
			}
		});
		setAction();
		new A_setFrame(this, "메인", 650, 590);
	}
	
	private void mainPanel() {
		for(int i = 0; i < 5; i++) {
			int index = i;
			Data data = adverMovieList.get(index);
			JLabel l = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/advertising/" + (index + 1) + ".jpg").getImage(), 0, 0, 630, 230, null);
					g.setColor(Color.white);
					Font font = new Font("맑은 고딕", 1, 22);
					g.setFont(font);
					g.drawString(data.get(1).toString(), 10, 150);
					g.setFont(font.deriveFont(0, 12f));
					g.drawString(data.get(3).toString(), 10, 170);
				}
			};
			l.setBounds(i * 630, 0, 630, 230);
			adverLabel.add(l);
			adverPanel.add(adverLabel.get(index));
		}
		thread = new Thread(() -> {
			try {
				while(!thread.interrupted()) {
					System.out.println(adverMovieList.get(0).get(1));
					Thread.sleep(2000);
					for(int i = 0; i < 630; i++) {
						adverLabel.get(0).setBounds(-i - 1, 0, 630, 230);
						adverLabel.get(1).setBounds(630 - i - 1, 0, 630, 230);
						repaint();
						revalidate();
						Thread.sleep(1);
					}
					adverLabel.add(adverLabel.get(0));
					adverLabel.remove(0);
				}
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		});
		thread.start();
		borderPanel.add(adverPanel);
	}
	
	private void southLEFTPanel() {
		panelA.setBorder(createLineBorder(Color.black));
		panelA.add(butPanel, BorderLayout.NORTH);
		JPanel gridPanel = new JPanel(new GridLayout(0, 10, 10,10));
		gridPanel.setBorder(createEmptyBorder(5, 5, 5, 5));
		gridPanel.setBackground(Color.white);
		for(int i = 0; i < 10; i++) {
			int index = i;
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			JLabel img = new JLabel(getImage("datafiles/movies/" + dataA.get(index).get(0) + ".jpg", 110, 180)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(new Font("맑은 고딕", 1, 30));
					g.drawString((index + 1) + "",10, 30);
				}
			};
			labelA.add(img);
			p.add(labelA.get(i));
			p.add(new JLabel(dataA.get(index).get(1).toString(), JLabel.CENTER), BorderLayout.SOUTH);
			p.setPreferredSize(new Dimension(110, 180));
			gridPanel.add(p);
		}
		panelA.add(scA = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBorder(createLineBorder(Color.white));
		}});
		southPanel.add(panelA);
	}
	
	private void southRIGHTPanel() {
		for(int i = 0; i < 5; i++) {
			JPanel p = new JPanel(new BorderLayout(7,7));
			try {
				BufferedImage originalImage = ImageIO.read(new File("datafiles/icon/별on.png"));
				BufferedImage reColor = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
				for(int y = 0; y < originalImage.getHeight(); y++) {
					for(int x = 0; x < originalImage.getWidth(); x++) {
						if(originalImage.getRGB(x, y) != 16777215) {
							reColor.setRGB(x, y, new Color(255, 210, 2).getRGB());
						}
					}
				}
				Image img = reColor.getScaledInstance(50, 50, 4);
				JLabel l = new JLabel(dataB.get(i).get(9).toString().substring(0, 3), JLabel.RIGHT) {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.drawImage(img, 65, 12, 15, 15, null);
					}
				};
				l.setFont(new Font("맑은 고딕", 1, 24));
				p.add(l, BorderLayout.NORTH);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			p.setBackground(Color.white);
			labelB.add(new JLabel(getImage("datafiles/movies/" + dataB.get(i).get(0) + ".jpg", 120, 160)));
			p.add(labelB.get(i));
			p.add(new JLabel(dataB.get(i).get(1).toString(), JLabel.LEFT), BorderLayout.SOUTH);
			p.setPreferredSize(new Dimension(120, 200));
			panelB.add(p);
		}
		southPanel.add(scB = new JScrollPane(panelB, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	}
	
	private void setAction() {
		MouseAdapter m1 = new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) { start1 = e.getX(); }
			@Override
			public void mouseDragged(MouseEvent e) {
				scA.getHorizontalScrollBar().setValue(scA.getHorizontalScrollBar().getValue() + (x1 = start1 - e.getX()) );
				start1 = e.getX();
			}
		};
		MouseAdapter m2 = new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) { start2 = e.getX(); }
			@Override
			public void mouseDragged(MouseEvent e) {
				scB.getHorizontalScrollBar().setValue(scB.getHorizontalScrollBar().getValue() + (x2 = start2 - e.getX()) );
				start2 = e.getX();
			}
		};
		scA.addMouseListener(m1); scA.addMouseMotionListener(m1);
		scB.addMouseListener(m2); scB.addMouseMotionListener(m2);
		
		for(int i = 0; i < 10; i ++) {
			final int index = i;
			labelA.get(i).addMouseListener(m1);
			labelA.get(i).addMouseMotionListener(m1);
			labelA.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					NewMovieInfor(dataA.get(index).get(0));
				}
			});
			
			if(i >= 5) continue;
			labelB.get(i).addMouseListener(m2);
			labelB.get(i).addMouseMotionListener(m2);
			labelB.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					NewMovieInfor(dataB.get(index).get(0));
				}
			});
			adverLabel.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					NewMovieInfor(adverMovieList.get(index).get(0));
				}
			});
		}
	}
	
	private void NewMovieInfor(Object m_no) {
		
		new MovieInfor(u_no, Integer.parseInt(m_no.toString()));
		dispose();
	}
	private ImageIcon getImage(String file, int w, int h) {
		return new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	public static void main(String[] args) {
		new Main(1);
	}
}
