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

import static  javax.swing.BorderFactory.*;
import javax.swing.*;

public class MovieSerch extends JFrame{
	Font font = new Font("맑은 고딕", 1, 22);
	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 10,10,10));
	}};
	
	JPanel nomalPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
	}};
	JPanel mainPanel = new JPanel(new GridLayout(0, 4, 5, 5)) {{
		setBackground(Color.white);
	}};
	
	JPanel serchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3)) {{
		setBackground(Color.white);
	}};
	
	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(160, 25));
	}};
	JButton serchBut = new CustumButton("검색");
	JComboBox<Object> order = new JComboBox<Object>("전체, 예매순, 평점순".split(", "));
	JComboBox<Object> genre = new JComboBox<Object>() {{
		addItem("전체");
		for(Data d : Connections.select("select g_name from genre")) addItem(d.get(0));
	}};
	
	List<Data> movies = new ArrayList<>();
	List<JLabel> imgList = new ArrayList<>();
	
	String[] query = {
			"SELECT * FROM movie where m_name like ? and g_no between ? and ?",
			
			"select movie.*, count(movie.m_no) as c from reservation\r\n"
			+ "right join movie on movie.m_no = reservation.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ "group by movie.m_no order by c desc, movie.m_no",
			
			"SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ "group by movie.m_no order by a desc, movie.m_no"
	};
	MovieSerch() {
		if(!User.admin)
			borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		setSerchPanel();
		nomalPanel.add(new JScrollPane(mainPanel));
		nomalPanel.add(serchPanel, BorderLayout.NORTH);
		borderPanel.add(nomalPanel);
		setMainPanel();
		add(borderPanel);
		setAction();
		setFrame.setframe(this, User.admin ? "관리자 검색" : "영화 검색", 900, 450);
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		imgList.clear();
		int gno = genre.getSelectedIndex();
		Object[] objs = {"%" + serch.getText() + "%", gno == 0 ? 1 : gno, gno == 0 ? 20 : gno};
		movies = Connections.select(query[order.getSelectedIndex()], objs);
		System.out.println(movies);
		for(int i = 0; i < movies.size(); i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(1, 5));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(0, 250));
			
			JLabel rank = new JLabel("", JLabel.CENTER);
			if((order.getSelectedIndex() == 1 && i < 10) || (order.getSelectedIndex() == 2 && i < 5)) {
				rank.setText("No." + (i + 1));
				rank.setBackground(new Color(255,0,0,100));
				rank.setOpaque(true);
				rank.setForeground(Color.white);
			}
			
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + movies.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			
			JLabel ageLimit = new JLabel(getter.getImageIcon("datafiles/limits/" + movies.get(i).getInt(2) + ".png", 30, 30), JLabel.RIGHT);
			ageLimit.setVerticalAlignment(JLabel.TOP);
			ageLimit.setPreferredSize(new Dimension(33, 33));
			
			JTextArea infor = new JTextArea(movies.get(i).get(1) + "\n"
			+ "3.2% | 개봉일: " + movies.get(i).get(6));
			infor.setFocusable(false);
			infor.setCursor(getCursor().getDefaultCursor());
			
			JLabel space = new JLabel("");
			space.setPreferredSize(new Dimension(30,30));
			
			imgList.add(img);
			p.add(img);
			p.add(rank, BorderLayout.NORTH);
			p.add(ageLimit, BorderLayout.WEST);
			p.add(space, BorderLayout.EAST);
			p.add(infor, BorderLayout.SOUTH);
			
			mainPanel.add(p);
		}
		
		for(int i = 0; i < imgList.size(); i++) {
			final int index = i;
			imgList.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(User.admin)
						new MovieChange(movies.get(index).getInt(0));
					else new MovieInfor(movies.get(index).getInt(0));
					dispose();
				}
			});
		}
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
				genre.setSelectedIndex(0);
				order.setSelectedIndex(0);
				setMainPanel();
				return;
			}
			setMainPanel();
			if(movies == null || movies.isEmpty()) {
				getter.mg("검색결과가 없습니다.", JOptionPane.ERROR_MESSAGE);
				genre.setSelectedIndex(0);
				order.setSelectedIndex(0);
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
