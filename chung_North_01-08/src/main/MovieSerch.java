package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class MovieSerch extends JFrame{
	Connections c = new Connections();
	JPanel borderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,5,5,5));
	}};
	
	JPanel serchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
		setBackground(Color.white);
	}};
	JTextField tf = new JTextField() {{
		setPreferredSize(new Dimension(200, 25));
	}};
	JButton serch = new CustumButton("검색");
	JComboBox<String> order = new JComboBox<String>("전체            ,예매순,평점순".split(",")) {{
		setBackground(Color.white);
	}};
	JComboBox<String> genre = new JComboBox<String>() {{
		setBackground(Color.white);
		addItem("전체");
		for(Data d : c.select("select * from genre")) {
			addItem(d.get(1).toString());
		}
	}};
	
	JPanel mainPanel = new JPanel(new GridLayout(0, 4, 15, 15)) {{
		setBorder(createEmptyBorder(5,5,5,5));
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel) {{
		setBackground(Color.white);
	}};
	
	
	List<Data> list = new ArrayList<>();
	String[] query = {"select * from movie where g_no between ? and ? and m_name like ?",
			"SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation "
					+ "join movie on movie.m_no = reservation.m_no "
					+ "where g_no between ? and ? and m_name like ? "
					+ "group by movie.m_no order by c desc, m_no;",
			"SELECT movie.*, avg(re_star) a FROM moviedb.review "
					+ "right join movie on movie.m_no = review.m_no "
					+ "where g_no between ? and ? and m_name like ? "
					+ "group by movie.m_no order by a desc, m_no"
			};
	
	int u_no = 0;
	MovieSerch(int u_no){
		this.u_no = u_no;
		String title = "영화 검색";
		if(u_no != -1) {
			title = "관리자 검색";
			borderPanel.add(new NorthPanel(u_no, this), BorderLayout.NORTH);			
		}
		
		setSerchPanel();
		borderPanel.add(serchPanel);
		setMainPanel();
		sc.setPreferredSize(new Dimension(0, 300 + (u_no == -1 ? 50 : 0) ));
		borderPanel.add(sc, BorderLayout.SOUTH);
		add(borderPanel);
		setAction();
		new A_setFrame(this, title, 900, 396);// 150, 66 -> 6x
	}
	
	private void setSerchPanel() {
		serchPanel.add(new JLabel("검색창") {{
			setFont(new Font("맑은 고딕", 1, 20));
		}});
		serchPanel.add(tf);
		serchPanel.add(serch);
		serchPanel.add(order);
		serchPanel.add(genre);
	}
	
	private void setMainPanel() {
		String txt = tf.getText();
		int index = genre.getSelectedIndex();
		int[] cno = {index == 0 ? 1 : index, index == 0 ? 20 : index};
		list = c.select(query[order.getSelectedIndex()], cno[0], cno[1], "%" + tf.getText() + "%");
		mainPanel.removeAll();
		mainPanel.setBorder(createEmptyBorder(5,5,5,5));
		for(int i = 0; i < list.size(); i++) {
			JPanel p = new JPanel(new BorderLayout(1, 5));
			if(order.getSelectedIndex() == 1 && i < 10) {
				mainPanel.setBorder(createEmptyBorder(0,0,0,0));
				p.add(new JLabel("No." + (i+1)) {{
					setHorizontalAlignment(JLabel.CENTER);
					setBackground(new Color(255, 0, 0, 100));
					setForeground(Color.white);
					setOpaque(true);
				}}, BorderLayout.NORTH);
			}
			if(order.getSelectedIndex() == 2 && i < 5) {
				mainPanel.setBorder(createEmptyBorder(0,0,0,0));
				p.add(new JLabel("No." + (i+1)) {{
					setHorizontalAlignment(JLabel.CENTER);
					setBackground(new Color(255, 0, 0, 100));
					setForeground(Color.white);
					setOpaque(true);
				}}, BorderLayout.NORTH);
			}
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(0, 225));
			
			p.add(new JLabel(getImage("datafiles/limits/" + list.get(i).get(2) + ".png", 30, 30)) {{
				setVerticalAlignment(JLabel.TOP);
			}}, BorderLayout.WEST);
			p.add(new JLabel(getImage("datafiles/movies/" + list.get(i).get(0) + ".jpg", 130, 190)) {{
				setBackground(Color.red);
				setOpaque(true);
				setHorizontalAlignment(JLabel.LEFT);
				setVerticalAlignment(JLabel.TOP);
			}});
			p.add(new JTextArea(list.get(i).get(1).toString() + "\n3.2% | 개봉일: " + list.get(i).get(6)) {{
				setLineWrap(true);
				setFocusable(false);
			}}, BorderLayout.SOUTH);
			p.add(new JLabel("") {{
				setPreferredSize(new Dimension(45, 225));
			}}, BorderLayout.EAST);
			mainPanel.add(p);
		}
		
		if(list.size() < 4) {
			for(int i = 0; i <4; i++) {
				mainPanel.add(new JLabel(""));
			}
		}
		setMainPanelAction();
		revalidate();
		repaint();
		
	}
	
	private void setAction() {
		order.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED)
				setMainPanel();
		});
		genre.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED)
				setMainPanel();
		});
		serch.addActionListener(e->{
			setMainPanel();
			if(list.size() == 0) {
				JOptionPane.showMessageDialog(null, "검색결과가 없습니다.","경고", JOptionPane.ERROR_MESSAGE);
				tf.setText("");
				order.setSelectedIndex(0);
				genre.setSelectedIndex(0);
				setMainPanel();
			}
		});
	}
	private void setMainPanelAction() {
		for(int i = 0; i < mainPanel.getComponentCount(); i++) {
			final int index = i;
			mainPanel.getComponent(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(list.get(index).get(0));
				}
			});
		}
	}
	private ImageIcon getImage(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	public static void main(String[] args) {
		new MovieSerch(1);
	}
}
