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

public class MovieSerch extends JFrame{
	Font font = new Font("맑은 고딕", 1, 20);
	
	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(0,5,5,5));
	}};
	
	JPanel nomalPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
	}};
	
	JPanel serchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2,2)) {{
		setBackground(Color.white);
	}};
	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(170, 25));
	}};
	JButton serchBut = new CustumButton("검색") {{
		setPreferredSize(new Dimension(100, 25));
	}};
	JComboBox<String> order = new JComboBox<String>("전체,예매순,평점순".split(",")) {{
		setBackground(Color.white);
	}};
	JComboBox<String> genre = new JComboBox<String>() {{
		setBackground(Color.white);
		addItem("전체");
		for(Data d : Connections.select("select g_name from genre")) addItem(d.get(0).toString());
	}};
	
	JPanel mainPanel = new JPanel(new GridLayout(0, 4, 7, 7)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel) {{
		getVerticalScrollBar().setUnitIncrement(20);
	}};
	List<Data> movies = new ArrayList<>();
	String[] query = {
			"SELECT * FROM moviedb.movie where m_name like ? and g_no between ? and ?;",
			
			"SELECT movie.*, count(movie.m_no) as s FROM moviedb.reservation\r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ " group by movie.m_no order by s desc, movie.m_no;",
			
			"SELECT movie.*, avg(re_star) as a FROM moviedb.review \r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "where movie.m_name like ? and g_no between ? and ?\r\n"
			+ "group by movie.m_no order by a desc, m_no;"
	};
	
	List<JLabel> imgs = new ArrayList<>();
	public MovieSerch() {
		setSerchPanel();
		if(!User.admin) {
			borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		}
		setMainPanel();
		nomalPanel.add(sc);
		borderPanel.add(nomalPanel);
		add(borderPanel);
		setFrame.setframe(this, User.admin ? "관리자 검색" : "영화 검색", 900, 450);
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		imgs.clear();
		int gno = genre.getSelectedIndex();
		Object[] object = {"%" + serch.getText() + "%", gno == 0 ? 1 : gno, gno == 0 ? 20 : gno};
		movies = Connections.select(query[order.getSelectedIndex()],object);
		for(int i = 0; i < movies.size(); i++) {
			Data m = movies.get(i);
			JPanel p = new JPanel(new BorderLayout(1, 5));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(0, 250));
			
			JLabel rank = new JLabel("", JLabel.CENTER);
			if((i < 10 && order.getSelectedIndex() == 1) || (i < 5 && order.getSelectedIndex() == 2)) {
				rank.setText("No." + (i + 1));
				rank.setBackground(new Color(255, 0, 0, 100));
				rank.setForeground(Color.white);
				rank.setOpaque(true);
			}
			JLabel ageLimit = new JLabel(getter.getImageIcon("datafiles/limits/" + m.getInt(2) + ".png", 30, 30), JLabel.RIGHT);
			ageLimit.setVerticalAlignment(JLabel.TOP);
			ageLimit.setPreferredSize(new Dimension(33,33));
			
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + m.getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			
			JTextArea infor = new JTextArea(m.get(1) + "\n 3.2% | 개봉일: " + m.get(6));
			infor.setFont(font.deriveFont(12f));
			
			imgs.add(img);
			p.add(rank, BorderLayout.NORTH);
			p.add(new JLabel("") {{
				setPreferredSize(new Dimension(30, 30));
			}}, BorderLayout.EAST);
			p.add(ageLimit, BorderLayout.WEST);
			p.add(img);
			p.add(infor, BorderLayout.SOUTH);
			mainPanel.add(p);
		}
		
		for(int i = 0; i < imgs.size(); i++) {
			final int index = i;
			imgs.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(User.admin) {
						movies.get(index).get(0);
					}else {
						movies.get(index).get(0);
					}
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
			if(serch.getText().isEmpty()) {
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				setMainPanel();
			}
			setMainPanel();
			if(movies == null || movies.isEmpty()) {
				getter.mg("검색결과가 없습니다.", JOptionPane.ERROR_MESSAGE);
				serch.setText("");
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				setMainPanel();
			}
		});
	}
	
	private void setSerchPanel() {
		setAction();
		serchPanel.add(new JLabel("검색창") {{
			setFont(font);
		}});
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
