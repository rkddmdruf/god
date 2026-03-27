package main;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import utils.*;


public class MovieSerch extends CFrame{

	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(200, 25));
	}};
	JButton serchButton = new CButton("검색");
	JComboBox<String> order = new JComboBox<>("전체,예매순,평점순".split(","));
	JComboBox<Id> genre = new JComboBox<Id>() {{
		addItem(new Id(0, "전체"));
		for(Data data : Connections.select("select * from genre")) {
			addItem(new Id(data.getInt(0), data.getString(1)));
		}
	}};
	String[] query = {"select * from movie where m_name like ? and g_no between ? and ?", 
			
			"SELECT movie.*, count(r_no) as c FROM moviedb.reservation\r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ "group by movie.m_no order by c desc, movie. m_no;;",
			
			"SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
			+ "join movie on movie.m_no = review.m_no\r\n"
			+ " where m_name like ? and g_no between ? and ? "
			+ "group by movie.m_no order by a desc, movie.m_no"
		};
	JPanel mainPanel = new JPanel(new GridLayout(0, 4, 7, 7)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel);
	
	public MovieSerch() {
		borderPanel.setBorder(createEmptyBorder(5, 15, 10, 15));
		setFrame(User.admin ? "관리자 검색" : "영화 검색", 925, 430 - (User.admin ? 50 : 0));
		
		JPanel northPanel = new JPanel(new BorderLayout(10, 10));
		northPanel.setBackground(Color.white);
		if(!User.admin)
			northPanel.add(new NorthPanel(this) {
				@Override
				public void setLoginAction() {
					super.setLoginAction();
					login.addActionListener(e->{
						getter.frames.add(()->{ new MovieSerch(); });
					});
				}
			}, BorderLayout.NORTH);
		
		northPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6)) {{
			setBackground(Color.white);
			add(new JLabel("검색창    ") {{
				setFont(new Font("맑은 고딕", 1, 11));
			}});
			add(serch);
			add(serchButton);
			add(order);
			add(genre);
		}});
		
		setMainPanel();
		setAction();
		borderPanel.add(northPanel, BorderLayout.NORTH);
		borderPanel.add(sc);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(!User.admin) {
					getter.frames.pop().run();
					dispose();
				}else {
					//new Main();
					dispose();
				}
			}
		});
		revalidate();
		repaint();
	}
	
	private void setAction() {
		ItemListener items = e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				setMainPanel();
			}
		};
		order.addItemListener(items);
		genre.addItemListener(items);
		serchButton.addActionListener(e->{
			if(serch.getText().isEmpty()) {
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				setMainPanel();
				return;
			}
			setMainPanel();
			if(mainPanel.getComponentCount() == 0) {
				getter.err("검색결과가 없습니다.");
				serch.setText("");
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				setMainPanel();
				return;
			}
		});
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		List<Data> list = new ArrayList<>();
		int gno = genre.getSelectedIndex();
		Object[] objs = {"%" + serch.getText() + "%", gno == 0 ? 1 : gno, gno == 0 ? 20 : gno};
		list = Connections.select(query[order.getSelectedIndex()], objs);
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			Data data = list.get(i);
			
			JPanel p = new JPanel(new BorderLayout(2, 5));
			p.setPreferredSize(new Dimension(0, 250));
			p.setBackground(Color.white);
			
			JLabel imgLabel = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + data.getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			imgLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(User.admin) {
						//new movieChange(data.getInt(0));
					}else {
						getter.frames.add(()->{ new MovieSerch(); });
						//new MovieInfor(data.getInt(0));
					}
					dispose();
				}
			});
			p.add(imgLabel);// 이미지
			
			p.add(new JLabel(getter.getImage("datafiles/limits/" + data.getInt(2) + ".png", 30, 30), JLabel.RIGHT) {{
				setVerticalAlignment(JLabel.TOP);
				setPreferredSize(new Dimension(33, 33));
			}}, BorderLayout.WEST);//나이
			p.add(new JTextArea(data.getString(1) + "\n" + "3.2%" + " | 개봉일: " + data.get(6)), BorderLayout.SOUTH);//설명
			p.add(new JLabel(" ") {{// 빈공간
				setPreferredSize(new Dimension(33, 0));
			}}, BorderLayout.EAST);
			
			JLabel rank = new JLabel("No." + (i + 1), JLabel.CENTER);
			rank.setVerticalAlignment(JLabel.CENTER);
			rank.setForeground(Color.white);
			rank.setPreferredSize(new Dimension(0, 25));
			
			p.add(rank, BorderLayout.NORTH);
			mainPanel.add(p);
			if(order.getSelectedIndex() != 0 && genre.getSelectedIndex() == 0) {
				if(order.getSelectedIndex() == 1 && i >= 10) continue;
				if(order.getSelectedIndex() == 2 && i >= 5) continue;
				rank.setBackground(new Color(255, 0, 0, 100));
				rank.setOpaque(true);
			}
		}
		SwingUtilities.invokeLater(() ->{
			sc.getVerticalScrollBar().setValue(0);
		});
		revalidate();
		repaint();
	}
	public static void main(String[] args) {
		new MovieSerch();
	}
}
