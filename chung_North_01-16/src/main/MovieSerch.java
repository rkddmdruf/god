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
	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,3,3,3));
	}};
	
	JPanel nomalPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
	}};
	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(200, 25));
	}};
	JButton serchBut = new CustumButton("검색") {{
		setPreferredSize(new Dimension(75, 25));
	}};
	JComboBox<String> order = new JComboBox<String>("전체      ,예매순,평점순".split(","));
	JComboBox<String> genre = new JComboBox<String>() {{
		addItem("전체");
		for(Data d : Connections.select("select g_name from genre")) addItem(d.get(0).toString());
	}};
	JPanel serchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2)) {{
		setBackground(Color.white);
		JLabel l = new JLabel(" 검색창");
		l.setFont(font);
		add(l);
		add(serch);
		add(serchBut);
		add(order);
		add(genre);
	}};
	
	JPanel moviePanel = new JPanel(new GridLayout(0, 4, 15, 25)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(moviePanel) {{
		getVerticalScrollBar().setUnitIncrement(20);
	}};
	
	String[] query = {
			"SELECT * FROM moviedb.movie where m_name like ? and g_no between ? and ?;",
			
			"SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation\r\n"
			+ "right join movie on movie.m_no = reservation.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ "group by movie.m_no order by c desc;",
			
			"SELECT movie.*, avg(re_star) as a FROM moviedb.review \r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ? \r\n"
			+ "group by m_no order by a desc, m_no;"
	};
	List<Data> list = new ArrayList<>();
	
	List<JLabel> labels = new ArrayList<>();
	MovieSerch() {
		nomalPanel.add(serchPanel, BorderLayout.NORTH);
		nomalPanel.add(sc);
		
		borderPanel.add(nomalPanel);
		if(!User.admin) 
			borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		
		add(borderPanel);
		setAction();
		setMoviePanelAndLabelsAction();
		SetFrames.setframe(this, "영화 검색", 950, 450);
	}
	private void setMoviePanelAndLabelsAction() {
		labels.clear();
		moviePanel.removeAll();
		int g = genre.getSelectedIndex();
		Object[] cno = {
				"%" + serch.getText() + "%",
				g == 0 ? 1 : g,
				g == 0 ? 20 : g,
		};
		list = Connections.select(query[order.getSelectedIndex()], cno[0], cno[1], cno[2]);
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			
			JPanel p = new JPanel(new BorderLayout(1, 5));
			p.setPreferredSize(new Dimension(0 ,250));
			p.setBackground(Color.white);
			
			JLabel top = new JLabel("", JLabel.CENTER);
			top.setForeground(Color.white);
			top.setBackground(new Color(255, 0, 0, 100));
			top.setOpaque(false);
			if((i < 10 && order.getSelectedIndex() == 1) || (i < 5 && order.getSelectedIndex() == 2)) {
				top.setText("No." + (i + 1));
				top.setOpaque(true);
			}
			
			JLabel age = new JLabel(getter.getImageIcon("datafiles/limits/" + list.get(index).getInt(2) + ".png", 40, 40), JLabel.RIGHT);
			age.setPreferredSize(new Dimension(42, 0));
			age.setVerticalAlignment(JLabel.TOP);
			
			JLabel l = new JLabel("") {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/movies/" + list.get(index).get(0) + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			labels.add(l);
			
			p.add(new JLabel("") {{ setPreferredSize(new Dimension(40,40)); }}, BorderLayout.EAST);
			p.add(age, BorderLayout.WEST);
			p.add(top, BorderLayout.NORTH);
			p.add(l);
			p.add(new JTextArea(list.get(i).get(1) + "\n3.1% | 개봉일: " + list.get(i).get(6)) {{
				setFont(font.deriveFont(1, 11));
				setFocusable(false);
				setCursor(getCursor().getDefaultCursor());
			}}, BorderLayout.SOUTH);
			moviePanel.add(p);
		}
		
		for(int i = 0; i < (5 - list.size() > 0 ? 4 : 0); i++) {
			moviePanel.add(new JLabel("") {{setPreferredSize(new Dimension(0, 250));}});
		}
		
		for(int i = 0; i < list.size(); i++) {
			final int index = i;
			labels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new MovieInfor(list.get(index).getInt(0));
				}
			});
		}
		revalidate();
		repaint();
	};
	
	private void setAction() {
		ItemListener item = e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				setMoviePanelAndLabelsAction();
			}
		};
		order.addItemListener(item);
		genre.addItemListener(item);
		serchBut.addActionListener(e->{
			if(serch.getText().isEmpty()) {
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
			}
			setMoviePanelAndLabelsAction();
			if(list == null || list.isEmpty()) {
				JOptionPane.showMessageDialog(null, "검색결과가 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				serch.setText("");
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				setMoviePanelAndLabelsAction();
				return;
			}
		});
	}
	public static void main(String[] args) {
		new MovieSerch();
	}
}
