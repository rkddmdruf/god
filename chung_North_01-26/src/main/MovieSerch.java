package main;

import utils.*;
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

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class MovieSerch extends JFrame{
	Font font = new Font("맑은 고딕", 1, 20);
	JFrame f = this;
	
	JPanel borderPanel = new JPanel(new BorderLayout(7,7)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 7, 7, 7));
	}};
	
	JPanel nomalPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
	}};
	
	JPanel serchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3)) {{
		setBackground(Color.white);
	}};
	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(160, 25));
	}};
	JButton serchBut = new CustumButton("검색");
	JComboBox<Object> order = new JComboBox<Object>("전체 예매순 평점순".split(" ")) {{
		setBackground(Color.white);
	}};
	JComboBox<Object> genre = new JComboBox<Object>() {{
		addItem("전체");
		setBackground(Color.white);
		for(Data d : Connections.select("select g_name from genre")) addItem(d.get(0));
	}};
	
	JPanel mainPanel = new JPanel(new GridLayout(0, 4, 10, 10)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel) {{
		setBackground(Color.white);
		setBorder(createLineBorder(Color.black));
	}};
	
	List<Data> listA = Connections.select("SELECT movie.m_no, avg(re_star) as a FROM moviedb.review "
			+ "right join movie on movie.m_no = review.m_no "
			+ "group by movie.m_no order by a desc, movie.m_no limit 10;");
	
	List<Data> listB = Connections.select("SELECT movie.m_no, count(r_no) as c FROM moviedb.reservation\r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ "group by movie.m_no order by c desc, movie.m_no limit 5;");
	
	String[] query = {
		"select * from movie where m_name like ? and g_no between ? and ?",
		
		"SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
				+ "right join movie on movie.m_no = review.m_no\r\n"
				+ "where m_name like ? and g_no between ? and ?\r\n"
				+ "group by movie.m_no order by a desc, movie.m_no;",
				
		"SELECT movie.*, count(r_no) as c FROM moviedb.reservation\r\n"
		+ "join movie on movie.m_no = reservation.m_no\r\n"
		+ "where m_name like ? and g_no between ? and ?\r\n"
		+ "group by movie.m_no order by c desc, movie.m_no;"
		
	};
	
	List<JLabel> imgs = new ArrayList<>();
	List<Data> list = new ArrayList<>();
	NorthPanel northPanel = new NorthPanel(f) {
		@Override
		void setLogin() {
			super.setLogin();
			login.addActionListener(e->{
				getter.r2 = getter.getR();
				getter.setR(()->{ new MovieSerch(); });
			});
		};
	};
	public MovieSerch(){
		setting();
		setSerchPanel();
		setMainPanel();
		nomalPanel.add(sc);
		borderPanel.add(nomalPanel);
		borderPanel.add(User.admin ? new JLabel() : northPanel, BorderLayout.NORTH);
		add(borderPanel);
		
		setAction();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(getter.r2 != null) {
					getter.r2.run();
					getter.r2 = null;
					return;
				}
				getter.fromMove(f);
			}
		});
		setFrame.setframe(this, User.admin ? "관리자 검색" : "영화 검색", 900, 400);
	}
	
	private void setting() {
		for(int i = 0; i < 10; i++) {
			listA.get(i).add(i + 1);
			if(i >= 5) continue;
			listB.get(i).add(i + 1);
		}
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		imgs.clear();
		int gno = genre.getSelectedIndex();
		Object[] objects = {"%" + serch.getText() + "%", gno == 0 ? 1 : gno, gno == 0 ? 20 : gno};
		list = Connections.select(query[order.getSelectedIndex()], objects);
		for(int i = 0; i < list.size(); i++) {
			Data data = list.get(i);

			JPanel p = new JPanel(new BorderLayout(1,5));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(0, 260));
			
			JLabel ageLimit = new JLabel(getter.getImage("datafiles/limits/" + data.getInt(2) + ".png", 30, 30), JLabel.RIGHT);
			ageLimit.setPreferredSize(new Dimension(33, 30));
			ageLimit.setVerticalAlignment(JLabel.TOP);
			
			JLabel spaceLabel = new JLabel("");
			spaceLabel.setPreferredSize(new Dimension(30,30));
			
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + data.get(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			
			JTextArea infor = new JTextArea(data.get(1) + "\n4.6% | 개봉일: " + data.get(6));
			infor.setFont(font.deriveFont(13f));
			infor.setFocusable(false);
			infor.setCursor(getCursor().getDefaultCursor());
			
			JLabel rank = new JLabel("", 0);
			
			if(order.getSelectedIndex() == 1) {
				for(int s = 0; s < 10; s++) {
					if(listA.get(s).getInt(0) == list.get(i).getInt(0)) {
						rank.setText("No." + listA.get(s).getInt(2));
						rank.setBackground(new Color(255, 0, 0, 100));
						rank.setForeground(Color.white);
						rank.setOpaque(true);
						break;
					}
				}
			}
			
			if(order.getSelectedIndex() == 2) {
				for(int s = 0; s < 5; s++) {
					if(listB.get(s).getInt(0) == list.get(i).getInt(0)) {
						rank.setText("No." + listB.get(s).getInt(2));
						rank.setBackground(new Color(255, 0, 0, 100));
						rank.setForeground(Color.white);
						rank.setOpaque(true);
						break;
					}
				}
			}
			
			imgs.add(img);
			p.add(img);
			p.add(rank, BorderLayout.NORTH);
			p.add(infor, BorderLayout.SOUTH);
			p.add(spaceLabel, BorderLayout.EAST);
			p.add(ageLimit, BorderLayout.WEST);
			mainPanel.add(p);
		}
		
		for(int i = 0; i < imgs.size(); i++) {
			final int index = i;
			imgs.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new MovieInfor(list.get(index).getInt(0));
					dispose();
				}
			});
		}
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
		serchBut.addActionListener(e->{
			setMainPanel();
		});
	}
	private void setSerchPanel() {
		JLabel l = new JLabel("검색창");
		l.setFont(font);
		
		serchPanel.add(l);
		serchPanel.add(serch);
		serchPanel.add(serchBut);
		serchPanel.add(order);
		serchPanel.add(genre);
		
		nomalPanel.add(serchPanel, BorderLayout.NORTH);
	}
	public static void main(String[] args) {
		new MovieSerch();
	}
}
