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
	Font font = new Font("맑은 고딕", 1, 20);
	
	JPanel borderPanel = new JPanel(new BorderLayout(7,7)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,5,5,5));
	}};
	JPanel nomalPanel = new JPanel(new BorderLayout(5, 5)) {{
		setBackground(Color.white);
	}};
	
	JPanel serchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)) {{
		setBackground(Color.white);
	}};
	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(175, 25));
	}};
	JButton serchBut = new CustumButton("검색");
	JComboBox<String> order = new JComboBox<String>("전체,예매순,평점순".split(","));
	JComboBox<String> genre = new JComboBox<String>() {{
		for(Data data : Connections.select("select g_name from genre"))
			addItem(data.get(0).toString());
	}};
	
	JPanel mainPanel = new JPanel(new GridLayout(0, 4, 10, 10)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel);
	List<Data> movies = new ArrayList<>();
	List<JLabel> labels = new ArrayList<>();
	String[] query = {"SELECT * FROM moviedb.movie where m_name like ? and g_no between ? and ?;",
			
			"SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation\r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ "group by reservation.m_no order by c desc, m_no;",
			
			"SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ? \r\n"
			+ "group by movie.m_no order by a desc, m_no;"};
	public MovieSerch() {
		setSerchPanel();
		setMainPanel();
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		borderPanel.add(nomalPanel);
		nomalPanel.add(sc);
		add(borderPanel);
		setAction();
		setFrame.setframe(this, User.admin ? "관리자 검색" : "영화 검색", 900, 399);
	}
	
	private void setMainPanel() {
		mainPanel.removeAll();
		labels.clear();
		
		int gno = genre.getSelectedIndex();
		Object[] objects = {"%" + serch.getText() + "%",
				gno == 0 ? 1 : gno, gno == 0 ? 20 : gno};
		movies = Connections.select(query[order.getSelectedIndex()], objects);
		for(int i = 0; i < movies.size(); i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(1, 4));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(0, 250));
			
			
			JLabel agelimit = new JLabel(getter.getImageIcon("datafiles/limits/" + movies.get(i).get(2) + ".png", 30, 30), JLabel.RIGHT);
			agelimit.setVerticalAlignment(JLabel.TOP);
			agelimit.setPreferredSize(new Dimension(33, 33));
			
			
			JLabel movieImage = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + movies.get(index).getInt(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			labels.add(movieImage);
			
			JTextArea infor = new JTextArea(movies.get(index).get(1) + "\n3.2% | 개봉일: " + movies.get(index).get(6)) {{
				setFocusable(false);
				setCursor(getCursor().getDefaultCursor());
				setFont(font.deriveFont(1, 12f));
			}};
			
			JLabel top = new JLabel("", JLabel.CENTER);
			if((order.getSelectedIndex() == 1 && i < 10) || (order.getSelectedIndex() == 2 && i < 5)) {
				top.setText("No." + (i+1));
				top.setForeground(Color.white);
				top.setBackground(new Color(255, 0, 0, 100));
				top.setOpaque(true);
			}
			p.add(top, BorderLayout.NORTH);
			p.add(new JLabel("") {{
				setPreferredSize(new Dimension(33, 33));
			}}, BorderLayout.EAST);
			p.add(agelimit, BorderLayout.WEST);
			p.add(movieImage);
			p.add(infor, BorderLayout.SOUTH);
			mainPanel.add(p);
		}
		movieClickedAction();
		revalidate();
		repaint();
	}
	
	private void movieClickedAction() {
		for(int i = 0; i < labels.size(); i++) {
			final int index = i;
			labels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(movies.get(index).get(0));
				}
			});
		}
	}
	
	private void setSerchPanel() {
		serchPanel.add(new JLabel("검색창") {{
			setFont(font);
		}});
		serchPanel.add(serch);
		serchPanel.add(serchBut);
		serchPanel.add(order);
		serchPanel.add(genre);
		nomalPanel.add(serchPanel, BorderLayout.NORTH);
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
			if(movies == null || movies.isEmpty()) {
				getter.mg("검색결과 없습니다.", JOptionPane.ERROR_MESSAGE);
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				setMainPanel();
				return;
			}
		});
	}
	
	public static void main(String[] args) {
		new MovieSerch();
	}
}
