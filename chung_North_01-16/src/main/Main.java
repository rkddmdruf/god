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
import java.awt.event.MouseListener;
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
	JPanel borderPanel = new JPanel(new BorderLayout(7,7)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 10, 10, 10));
	}};
	
	
	JPanel eastPanel = new JPanel(new GridLayout(0, 5));
	JScrollPane scB = new JScrollPane(eastPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
		setBackground(Color.white);
		setBorder(createLineBorder(Color.black));
	}};
	List<Data> reviewList = Connections.select("SELECT movie.*, avg(re_star) as a FROM moviedb.review \r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "group by m_no order by a desc, m_no limit 5;");
	List<JLabel> listB = new ArrayList<>();
	
	JPanel westPanel = new JPanel(new GridLayout(0, 10));
	JScrollPane scA = new JScrollPane(westPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
		setBackground(Color.white);
		setBorder(createLineBorder(Color.white));
	}};
	List<Data> reservationList = Connections.select("SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation\r\n"
			+ "right join movie on movie.m_no = reservation.m_no\r\n"
			+ "group by movie.m_no order by c desc limit 10;");
	List<JLabel> listA = new ArrayList<>();
	JButton movieAllShow = new CustumButton("영화 전체보기") {{
		setFont(font.deriveFont(0, 9));
	}};
	JButton kiosc = new CustumButton("먹거리키오스크"){{
		setFont(font.deriveFont(0, 9));
	}};
	
	
	JPanel adverPanel = new JPanel(null) {{
		setBackground(Color.white);
	}};
	List<Data> adverList = Connections.select("select * from movie where m_no in(6, 2, 32 ,9, 18) order by field(m_no, 6, 2, 32, 9, 18);");
	List<JLabel> adverLabels = new ArrayList<>();
	
	Thread thread;
	Main(){
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(thread != null) thread.interrupt();
			}
		});
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		setAdverPanel();
		setMainPanel();
		setAction();
		add(borderPanel);
		SetFrames.setframe(this, "메인", 650, 575);
	}
	private void setMainPanel() {
		JPanel southPanel = new JPanel(new GridLayout(0, 2, 3, 3));
		southPanel.setPreferredSize(new Dimension(0, 225));
		
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(10, 10));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(120, 225));
			p.setBorder(createEmptyBorder(10,5,10,5));
			String reviewPoint = reviewList.get(i).get(reviewList.get(i).size() - 1).toString().substring(0, 3);
			Image imgs = null;
			try {
				BufferedImage oimg = ImageIO.read(new File("datafiles/icon/별on.png"));
				BufferedImage rimg = new BufferedImage(oimg.getWidth(), oimg.getHeight(), BufferedImage.TYPE_INT_ARGB);
				for(int y = 0; y < oimg.getHeight(); y++) {
					for(int x = 0; x < oimg.getWidth(); x++) {
						rimg.setRGB(x, y, (oimg.getRGB(x, y) != 16777215 ? new Color(255,215,0) : Color.white).getRGB());
					}
				}
				imgs = rimg.getScaledInstance(25, 25, 4);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			Image img = imgs;
			JLabel review = new JLabel(reviewPoint, JLabel.RIGHT) {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(img, 50, 10, 18, 18, null);
				};
			};
			review.setFont(font);
			
			p.add(review, BorderLayout.NORTH);
			JLabel movieImage = new JLabel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + reviewList.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);				}
			};
			listB.add(movieImage);
			p.add(movieImage);
			p.add(new JLabel(reviewList.get(i).get(1).toString(), JLabel.LEFT) {{
				setFont(font.deriveFont(1,12));
			}}, BorderLayout.SOUTH);
			eastPanel.add(p);
		}
		
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(Color.white);
		panel.setBorder(createLineBorder(Color.black));
		panel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)) {{
			setBackground(Color.white);
			add(movieAllShow);
			add(kiosc);
		}}, BorderLayout.NORTH);
		panel.add(scA);
		
		for(int i = 0; i < 10; i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(10, 10));
			p.setPreferredSize(new Dimension(120, 0));
			p.setBorder(createEmptyBorder(5, 7, 15, 7));
			p.setBackground(Color.white);
			JLabel movieImage = new JLabel("") {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + reservationList.get(index).get(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					g.setColor(Color.white);
					g.setFont(font.deriveFont(1, 30f));
					g.drawString((index + 1) + "", 10, 28);
				}
			};
			listA.add(movieImage);
			p.add(movieImage);
			p.add(new JLabel(reservationList.get(index).get(1).toString(), JLabel.CENTER), BorderLayout.SOUTH);
			westPanel.add(p);
		}
		
		southPanel.add(panel);
		southPanel.add(scB);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
	}
	
	private void setAdverPanel() {
		for(int i = 0; i < adverList.size(); i++) {
			final int index = i;
			JLabel l = new JLabel(getter.getImageIcon("datafiles/advertising/" + (i+1) + ".jpg", 630, 230)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(font);
					g.drawString(adverList.get(index).get(1).toString(), 10, 150);
					g.setFont(font.deriveFont(0, 15));
					g.drawString(adverList.get(index).get(3).toString(), 10, 170);
				}
			};
			l.setBounds(i * 630, 0, 630, 230);
			adverPanel.add(l);
			adverLabels.add(l);
		}
		
		thread = new Thread(()->{
			try {
				while(!thread.isInterrupted()) {
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
		
		MouseAdapter mA = mouseDrag(scA);
		scA.addMouseListener(mA);
		scA.addMouseMotionListener(mA);
		
		MouseAdapter mB = mouseDrag(scB);
		scB.addMouseListener(mB);
		scB.addMouseMotionListener(mB);
		
		for(int i = 0; i < 10 ;i++) {
			listA.get(i).addMouseListener(mA);
			listA.get(i).addMouseMotionListener(mA);
			listA.get(i).addMouseListener(mouseClickedAction(reservationList, i));
			if(i >= 5) continue;
			listB.get(i).addMouseListener(mB);
			listB.get(i).addMouseMotionListener(mB);
			listB.get(i).addMouseListener(mouseClickedAction(reviewList, i));
			adverLabels.get(i).addMouseListener(mouseClickedAction(adverList, i));
		}
	}
	
	private MouseAdapter mouseClickedAction(List<Data> list, int index) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new MovieInfor(list.get(index).getInt(0));
				dispose();
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
				System.out.println(x);
				JScrollBar b = sc.getHorizontalScrollBar();
				b.setValue(b.getValue() + x - e.getX());
				x = e.getX();
			}
		};
	}
	public static void main(String[] args) {
		new Main();
	}
}
