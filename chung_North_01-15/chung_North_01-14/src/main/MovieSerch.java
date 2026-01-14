package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
	Font font = new Font("맑은 고딕", 1, 18);
	
	JPanel borderPanel = new JPanel(new BorderLayout(5, 5));
	
	JTextField serch = new JTextField() {{ setPreferredSize(new Dimension(180, 25));}};
	JButton serchBut = new CustumButton("검색");
	JComboBox<String> order = new JComboBox<String>("전체,예매순,평점순".split(","));
	JComboBox<String> genre= new JComboBox<String>() {{
		addItem("전체");
		for(Data d : Connections.select("select g_name from genre"))
			addItem(d.get(0).toString());
	}};
	JPanel serchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
	
	JPanel mainPanel = new JPanel(new GridLayout(0, 4, 7,7));
	JScrollPane sc = new JScrollPane(mainPanel);
	
	String[] query = {"select * from movie where m_name like ? and g_no between ? and ?",
			"SELECT movie.*, avg(re_star) as a FROM moviedb.review\r\n"
			+ "right join movie on movie.m_no = review.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ "group by movie.m_no order by a desc, movie.m_no;",
			
			"SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation\r\n"
			+ "join movie on movie.m_no = reservation.m_no\r\n"
			+ "where m_name like ? and g_no between ? and ?\r\n"
			+ "group by movie.m_no order by c desc, movie.m_no"
	};
	List<JLabel> labels = new ArrayList<>();
	List<Data> list = new ArrayList<>();
	boolean isFromMain = false;
	MovieSerch(boolean isFromMain){
		this.isFromMain = isFromMain;
		borderPanel.setBackground(Color.white);
		serchPanel.setBackground(Color.white);
		serchPanel.add(new JLabel("검색창") {{
			setFont(font);
		}});
		serchPanel.add(serch);
		serchPanel.add(serchBut);
		serchPanel.add(order);
		serchPanel.add(genre);
		
		
		setMainPanel();
		int admin = 0;
		if(User.admin) {
			admin = 55;
		}else {
			borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		}
		mainPanel.setBackground(Color.white);
		sc.setPreferredSize(new Dimension(0, 235 + admin));
		borderPanel.add(sc, BorderLayout.SOUTH);
		borderPanel.add(serchPanel);
		borderPanel.setBorder(createEmptyBorder(0, 5, 5, 5));
		add(borderPanel);
		setAction();
		new A_setFrame(this, "열화 검색", 900, 369);
	}
	
	private void setMainPanel() {
		labels.clear();
		mainPanel.removeAll();
		int g_no = genre.getSelectedIndex();
		int[] cno = {g_no == 0 ? 1 : g_no, g_no == 0 ? 20 : g_no};
		list = Connections.select(query[order.getSelectedIndex()],"%" + serch.getText() + "%", cno[0], cno[1]);
		for (int i = 0; i < list.size(); i++) {
			JPanel p = new JPanel(new BorderLayout(2,2));
			p.setBackground(Color.white);
			p.setBorder(createEmptyBorder(5,3,5,5));
			p.setPreferredSize(new Dimension(750 / 4, 200));
			JLabel age = new JLabel(getter.getImageIcon("datafiles/limits/" + list.get(i).get(2) + ".png", 30, 30));
			age.setVerticalAlignment(JLabel.TOP);
			p.add(age, BorderLayout.WEST);
			
			JLabel img = new JLabel(getter.getImageIcon("datafiles/movies/" + list.get(i).get(0) + ".jpg", 120, 170), JLabel.LEFT);
			p.add(img);
			labels.add(img);
			
			JTextArea t = new JTextArea(list.get(i).get(1) + "\n2.7% | 개봉일: " + list.get(i).get(6));
			t.setFocusable(false);
			t.setCursor(getCursor().getDefaultCursor());
			p.add(t, BorderLayout.SOUTH);
			
			mainPanel.add(p);
		}
		revalidate();
		repaint();
	}
	
	private void setAction() {
		ItemListener itemAction = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() != ItemEvent.SELECTED) return;
				setMainPanel();
			}
		};
		
		order.addItemListener(itemAction);
		genre.addItemListener(itemAction);
		serchBut.addActionListener(e->{
			if(serch.getText().isEmpty()) {
				setMainPanel();
			}
			setMainPanel();
			if(list.isEmpty()) {
				JOptionPane.showMessageDialog(null, "검색결과가 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				serch.setText("");
				setMainPanel();
			}
		});
		
		for(int i = 0; i < labels.size(); i++) {
			final int index = i;
			labels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new MovieInfor(Integer.parseInt(list.get(index).get(0).toString()), isFromMain);
				}
			});
		}
	}
	public static void main(String[] args) {
		new MovieSerch(true);
	}
}
