package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
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
	
	JPanel mainPanel = new JPanel(new GridLayout(0, 4)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel) {{
		setBackground(Color.white);
	}};
	
	
	List<Data> list = new ArrayList<>();
	String s ="SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation "
			+ "join movie on movie.m_no = reservation.m_no "
			+ "where g_no between ? and ? and m_name like ? "
			+ "group by movie.m_no order by c desc, m_no;";
	
	String ss ="SELECT movie.*, avg(re_star) a FROM moviedb.review "
			+ "right join movie on movie.m_no = review.m_no "
			+ "where g_no between ? and ? and m_name like ? "
			+ "group by movie.m_no order by a desc, m_no";
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
		//int[] cno = {0}
		//switch (order.getSelectedIndex()) {
		//case 0 : { list = c.select(""); break;}
		//}
		list = c.select(s, 1, 20, tf.getText());
	}
	
	private ImageIcon getImage(String string, int w, int h) {
		return new ImageIcon(new ImageIcon(string).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	public static void main(String[] args) {
		new MovieSerch(1);
	}
}
