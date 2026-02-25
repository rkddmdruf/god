package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.CFrame;
import utils.Connections;
import utils.Data;
import utils.getter;

public class Main extends CFrame{
	
	List<Data> adverList = Connections.select("select * from movie where m_no in(6,2,32,9,18) order by field(m_no,6,2,32,9,18)");
	List<JLabel> adverLabels = new ArrayList<>();
	JPanel adverPanel = new JPanel(null);
	JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10, 15, 10, 15));
	}};
	
	
	JPanel moviePanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
	}};
	Thread thread;
	int w = 600;
	public Main() {
		borderPanel.add(new NorthPanel(this) {
			@Override
			void loginAction() {
				super.loginAction();
				login.addActionListener(e->{
					if(login.getText().equals("로그인"))
						getter.push(() -> { new Main(); }); 
				});
			}
			@Override
			void movieSerchAction() {
				super.movieSerchAction();
				movieSerch.addActionListener(e->{
					getter.push(() -> { new Main(); }); 
				});
			}
		}, BorderLayout.NORTH);
		setadverPanel();
		setMoviePanel();
		borderPanel.add(moviePanel, BorderLayout.SOUTH);
		borderPanel.add(adverPanel);
		add(borderPanel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(thread != null) thread.interrupt();
			}
		});
		setFrame("메인", w, 515);
	}
	
	private void setMoviePanel() {
		JLabel l = new JLabel("MOVIE", JLabel.CENTER);
		l.setFont(new Font("맑은 고딕", 0, 13));
		moviePanel.add(l, BorderLayout.NORTH);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.white);
		mainPanel.setBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(5, 5, 5, 5)));
		
		JPanel gridPanel = new JPanel(new GridLayout(1, 10, 15, 15));
		gridPanel.setBackground(Color.white);
		
		JScrollPane scroll = new JScrollPane(gridPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
			setBorder(null);
		}};
		MouseAdapter ma = new MouseAdapter() {
			int start = 0;
			
			@Override
			public void mousePressed(MouseEvent e) {
				start = e.getX();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				JScrollBar sb = scroll.getHorizontalScrollBar();
				sb.setValue(sb.getValue() + (start - e.getX()));
				start = e.getX();
			}
		};
		
		List<JPanel> panels = new ArrayList<>();
		List<Data> list = Connections.select("SELECT movie.*, count(r_no) as c FROM moviedb.reservation \r\n"
				+ "join movie on movie.m_no = reservation.m_no\r\n"
				+ "group by reservation.m_no order by c desc, m_no limit 10;");
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			Data data = list.get(i);
			JPanel p = new JPanel(new BorderLayout(5, 5));
			p.setBackground(Color.white);
			p.setBorder(createEmptyBorder(0,0,10,0));
			
			p.add(new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + data.getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					g.setColor(Color.white);
					g.setFont(new Font("맑은 고딕", 1, 24));
					g.drawString((index + 1) +"", 1, 27);
				}
			{
				addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						new MovieInfor(data.getInt(0));
						getter.push(() -> { new Main(); }); 
						dispose();
					};
				});
				addMouseListener(ma);
				addMouseMotionListener(ma);
			}});
			panels.add(p);
			p.add(new JLabel(data.getString(1)) {{
				setFont(new Font("맑은 고딕", 0, 10));
				setPreferredSize(new Dimension(100, 25));
			}}, BorderLayout.SOUTH);
			gridPanel.add(p);
		}
		
		
		gridPanel.addMouseListener(ma);
		gridPanel.addMouseMotionListener(ma);
		mainPanel.add(scroll);
		
		moviePanel.setPreferredSize(new Dimension(0, 200));
		moviePanel.add(mainPanel);
	}

	private void setadverPanel() {
		int w = this.w - 30;
		int h = 225;
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JLabel l = new JLabel(getter.getImageIcon("datafiles/advertising/" + (i + 1) + ".jpg", w, h)) {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.white);
					g.setFont(new Font("맑은 고딕", 1, 14));
					g.drawString(adverList.get(index).getString(1), 10, (h / 2) + 50);
					g.drawString(adverList.get(index).getString(3), 10, (h / 2) + 70);
				};
			};
			l.setBounds(i * w, 0, w, h);
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new MovieInfor(adverList.get(index).getInt(0));
					getter.push(() -> { new Main(); }); 
					dispose();
				}
			});
			adverLabels.add(l);
			adverPanel.add(l);
		}
		
		thread = new Thread(()->{
			try {
				while(!Thread.interrupted()) {
					Thread.sleep(1000);
					for(int i = 0; i < w; i++) {
						adverLabels.get(0).setBounds(-i, 0, w, h);
						adverLabels.get(1).setBounds(w - i, 0, w, h);
						Thread.sleep(1);
					}
					adverLabels.add(adverLabels.get(0));
					adverLabels.remove(0);
				}
			} catch (Exception e) {
				Thread.interrupted();
			}
		});
		thread.start();
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
