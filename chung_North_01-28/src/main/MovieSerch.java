package main;

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
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import utils.*;

public class MovieSerch extends CFrame{
	Font font = new Font("맑은 고딕", 1, 20);
	
	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBorder(createEmptyBorder(0,5,5,5));
		setBackground(Color.white);
	}};
	
	JPanel nomalPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
	}};
	
	JPanel serchPanel = new JPanel(new FlowLayout(0, 3, 3)) {{
		setBackground(Color.white);
	}};
	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(165, 25));
	}};
	JButton serchBut = new CustumButton("검색");
	JComboBox<Object> order = new JComboBox<Object>("전체,예매순,평점순".split(",")) {{
		setBackground(Color.white);
	}};
	JComboBox<Object> genre = new JComboBox<Object>() {{
		addItem("전체");
		setBackground(Color.white);
		for (Data d : Connections.select("select g_name from genre")) {
			addItem(d.get(0));
		}
	}};
	
	List<Data> listA = Connections.select("SELECT movie.m_no, avg(re_star) as c FROM moviedb.review \r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "group by movie.m_no order by c desc, movie.m_no limit 10");
	List<Data> listB = Connections.select("SELECT movie.m_no, count(movie.m_no) as c FROM moviedb.reservation \r\n"
			+ "right join movie on movie.m_no = reservation.m_no\r\n"
			+ "group by movie.m_no order by c desc, movie.m_no limit 5");
	
	String[] query = {
			"SELECT * FROM moviedb.movie where m_name like ? and g_no between ? and ?;",
			
			
			"SELECT movie.*, avg(re_star) as c FROM moviedb.review \r\n"
					+ "right join movie on movie.m_no = review.m_no\r\n"
					+ "where m_name like ? and g_no between ? and ?\r\n"
					+ "group by movie.m_no order by c desc, movie.m_no",
					
			"SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation \r\n"
			+ "right join movie on movie.m_no = reservation.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ "group by movie.m_no order by c desc, movie.m_no"
	};
	List<Data> list = new ArrayList<>();
	List<JLabel> imgs = new ArrayList<>();
	JPanel mainPanel = new JPanel(new GridLayout(0, 4, 7, 7)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel);
	public MovieSerch() {
		setting();
		setSerchPanel();
		setMainPanel();
		if(!User.admin)
			borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		nomalPanel.add(sc);
		nomalPanel.add(serchPanel, BorderLayout.NORTH);
		borderPanel.add(nomalPanel);
		add(borderPanel);
		setAction();
		setFrame(User.admin ? "관리자 검색" : "영화 검색", 900, 400);
	}
	
	private void setting() {
		for(int i = 0; i < 10; i++) {
			listA.get(i).add((i + 1));
			if(i >= 5) continue;
			listB.get(i).add((i + 1));
		}
		System.out.println(listA);
	}
	
	private void setMainPanel() {
		imgs.clear();
		mainPanel.removeAll();
		int gno = genre.getSelectedIndex();
		Object[] obj = {"%" + serch.getText() + "%", gno , gno == 0 ? 20 : gno};
		list = Connections.select(query[order.getSelectedIndex()], obj);
		for (int i = 0; i < list.size(); i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(1, 5));
			p.setPreferredSize(new Dimension(0, 250));
			p.setBackground(Color.white);
			
			JLabel ageLimit = new JLabel(getter.getImage("datafiles/limits/" + list.get(i).getInt(2) + ".jpg", 30, 30), JLabel.RIGHT);
			ageLimit.setPreferredSize(new Dimension(33, 30));
			ageLimit.setVerticalAlignment(JLabel.TOP);
			
			JLabel space = new JLabel("");
			space.setPreferredSize(new Dimension(30,30));
			
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + list.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			imgs.add(img);
			
			JTextArea infor = new JTextArea(list.get(i).get(1) + "\n3.2% | 개봉일: " + list.get(index).get(6));
			infor.setFocusable(false);
			infor.setCursor(getCursor().getDefaultCursor());
			infor.setFont(font.deriveFont(1, 13));
			
			JLabel rank = new JLabel("", JLabel.CENTER);
			
			if(order.getSelectedIndex() != 0) {
				List<Data> listC = order.getSelectedIndex() == 1 ? listA : listB;
				for(int s = 0; s < listC.size(); s++) {
					if(list.get(i).getInt(0) == listC.get(s).getInt(0)) {
						rank.setText("No." + listC.get(s).getInt(2));
						rank.setBackground(new Color(255,0,0,100));
						rank.setForeground(Color.white);
						rank.setOpaque(true);
					}
				}
			}
			p.add(rank, BorderLayout.NORTH);
			p.add(img);
			p.add(infor, BorderLayout.SOUTH);
			p.add(ageLimit, BorderLayout.WEST);
			p.add(space, BorderLayout.EAST);
			mainPanel.add(p);
		}
		
		for(int i = 0; i < imgs.size(); i++) {
			final int index = i;
			imgs.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(User.admin) {
						new MovieChange(list.get(index).getInt(0));
					}else {
						new MovieInfor(list.get(index).getInt(0));
					}
					dispose();
				}
			});
		}
		
		SwingUtilities.invokeLater(()->{
			JScrollBar sb = sc.getVerticalScrollBar();
			sb.setValue(0);
		});
		revalidate();
		repaint();
	}
	
	private void setAction() {
		ItemListener items = e->{
			if(e.getStateChange() == ItemEvent.SELECTED)
				setMainPanel();
		};
		
		order.addItemListener(items);
		genre.addItemListener(items);
		serchBut.addActionListener(e->{
			if(serch.getText().isEmpty()) {
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				setMainPanel();
				return;
			}
			setMainPanel();
			if(list.isEmpty()) {
				getter.mg("검색 결과가 없습니다.", JOptionPane.ERROR_MESSAGE);
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				serch.setText("");
				setMainPanel();
				return;
			}
		});
	}
	
	private void setSerchPanel() {
		serchPanel.add(new JLabel("검색창") {{
			setFont(font);
		}});
		serchPanel.add(serch);
		serchPanel.add(serchBut);
		serchPanel.add(order);
		serchPanel.add(genre);
	}
	
	public static void main(String[] args) {
		new MovieSerch();
	}
}
