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

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class MovieSerch extends JFrame{
	Font font = new Font("맑은 고딕", 1, 22);
	JPanel borderPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,5,5,5));
	}};
	
	JPanel nomalPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
	}};
	JPanel mainPanel = new JPanel(new GridLayout(0,4, 7,7)) {{
		setBackground(Color.white);
	}};
	
	JPanel serchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 3)) {{
		setBackground(Color.white);
	}};
	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(175, 25));
	}};
	JButton serchBut = new CustumButton("검색");
	JComboBox<Object> order = new JComboBox<Object>("전체,예매순,평점순".split(",")) {{
		setBackground(Color.white);
	}};
	JComboBox<Object> genre = new JComboBox<Object>() {{
		setBackground(Color.white);
		addItem("전체");
		for(Data d : Connections.select("select g_name from genre")) addItem(d.get(0));
	}};
	
	List<Data> list = new ArrayList<>();
	List<JLabel> labels = new ArrayList<>();
	String[] query = {
			"SELECT * FROM moviedb.movie where m_name like ? and g_no between ? and ?;",
			
			"SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ "group by movie.m_no order by a desc, movie.m_no",
			
			"SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation\r\n"
			+ "right join movie on movie.m_no = reservation.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ "group by m_no order by c desc"
	};
	MovieSerch(){
		setSerchPanel();
		setMainPanel();
		setAction();
		if(!User.admin)
			borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		nomalPanel.add(serchPanel, BorderLayout.NORTH);
		nomalPanel.add(new JScrollPane(mainPanel));
		borderPanel.add(nomalPanel);
		add(borderPanel);
		
		SetFrame.setFrame(this, User.admin ? "관리자 검색" : "영화 검색", 900, 400);
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		labels.clear();
		int gno = genre.getSelectedIndex();
		Object[] objects = {"%" + serch.getText() + "%", gno == 0 ? 1 : gno, gno == 0 ? 20 : gno};
		list = Connections.select(query[order.getSelectedIndex()], objects);
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(1, 5));
			p.setPreferredSize(new Dimension(0, 250));
			p.setBackground(Color.white);
			
			JLabel rank = new JLabel("", JLabel.CENTER);
			if((order.getSelectedIndex() == 1 && i < 10) || (order.getSelectedIndex() == 2 && i < 5)) {
				rank.setBackground(new Color(255,0,0,100));
				rank.setOpaque(true);
				rank.setForeground(Color.white);
				rank.setText("No." + (i + 1));
			}
			
			JLabel agelimitImage = new JLabel(getter.getImageIcon("datafiles/limits/" + list.get(i).getInt(2) + ".png", 30, 30), JLabel.RIGHT);
			agelimitImage.setVerticalAlignment(JLabel.TOP);
			agelimitImage.setPreferredSize(new Dimension(33, 30));
			
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + list.get(index).get(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			labels.add(img);
			JTextArea tf = new JTextArea(list.get(index).get(1) + "\n3.2% | 개봉일: " + list.get(index).get(6));
			tf.setFocusable(false);
			tf.setCursor(getCursor().getDefaultCursor());
			tf.setFont(font.deriveFont(13f));
			
			p.add(rank, BorderLayout.NORTH);
			p.add(new JLabel("") {{
				setPreferredSize(new Dimension(30, 30));
			}}, BorderLayout.EAST);
			p.add(agelimitImage, BorderLayout.WEST);
			p.add(img);
			p.add(tf, BorderLayout.SOUTH);
			
			mainPanel.add(p);
		}
		
		for(int i = 0; i < labels.size(); i++) {
			final int index = i;
			labels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(User.admin)  new MovieChange(list.get(index).getInt(0));
						else		new MovieInfor (list.get(index).getInt(0));
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
				return;
			}
			setMainPanel();
			if(list.size() == 0) {
				getter.mg("검색결과가 없습니다.", JOptionPane.ERROR_MESSAGE);
				serch.setText("");
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				setMainPanel();
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
