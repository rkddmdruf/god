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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MovieSerch extends JFrame{
	
	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 10, 10, 10));
	}};
	JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
	}};
	
	////아래는 검색 판넬 관련 Component
	JTextField serch = new JTextField() {{
		setPreferredSize(new Dimension(175, 25));
	}};
	JButton serchBut = new CustumButton("검색");
	JComboBox<String> order = new JComboBox<>("전체            ,예매순,평점순".split(","));
	JComboBox<String> genre = new JComboBox<String>() {{
		addItem("전체");
		for(Data d : Connections.select("select * from genre")) {
			addItem(d.get(0).toString());
		}
	}};
	JPanel serchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
		setBackground(Color.white);
		setPreferredSize(new Dimension(800, 30));
		add(new JLabel("검색창") {{ setFont(new Font("맑은 고딕", 1, 18)); }} );
		add(serch);
		add(serchBut);
		add(order);
		add(genre);
	}};
	
	///아래는 영화 생긴거 관련 변수
	JPanel moviePanel = new JPanel(new GridLayout(0, 4,15,15)) {{
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(moviePanel);
	
	String[] query = {"SELECT * FROM moviedb.movie where m_plot like ? and g_no between ? and ?;",
			
			"SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation "
			+ "			join movie on movie.m_no = reservation.m_no "
			+ "			where m_plot like ? and g_no between ? and ? "
			+ "            group by movie.m_no order by c desc, movie.m_no;",
			
			"SELECT movie.*, avg(re_star) as a FROM moviedb.review "
			+ "		right join movie on movie.m_no = review.m_no "
			+ "		where m_plot like ? and g_no between ? and ? "
			+ "		group by movie.m_no order by a desc, movie.m_no;"
	};
	List<Data> movieList = new ArrayList<>();
	List<JLabel> imageList = new ArrayList<>();
	int u_no = 0;
	public MovieSerch(int u_no) {
		this.u_no = u_no;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(u_no == -1) { new Login(); }
				else { new Main(u_no); }
				dispose();
			}
		});
		String title = "관리자 검색";
		if(u_no != -1) {
			title = "영화 검색";
			borderPanel.add(new NorthPanel(this, u_no), BorderLayout.NORTH);
		}
		setJScrollPane();
		setAction();
		mainPanel.add(sc);
		mainPanel.add(serchPanel, BorderLayout.NORTH);
		borderPanel.add(mainPanel);
		add(borderPanel);
		new A_setFrame(this, title, 900, 450);
	}
	
	private void setJScrollPane() {
		moviePanel.removeAll();
		imageList.clear();
		String text = "%" + serch.getText() + "%";
		int genreN = genre.getSelectedIndex();
		int[] cno = {genreN == 0 ? 1 : genreN, genreN == 0 ? 20 : genreN};
		movieList = Connections.select(query[order.getSelectedIndex()], text, cno[0], cno[1]);
		for(int i = 0; i < movieList.size(); i++) {
			int index = i;
			Data data = movieList.get(index);
			JPanel p = new JPanel(new BorderLayout(1,10));
			p.setBackground(Color.white);
			p.setPreferredSize(new Dimension(0, 240));
			p.add(new JLabel(), BorderLayout.NORTH);
			if((order.getSelectedIndex() == 1 && i < 10) || (order.getSelectedIndex() == 2 && i < 5)) {
				p.add(new JLabel("No." + (i+1), JLabel.CENTER) {{
					setBackground(new Color(255, 0, 0, 100));
					setOpaque(true);
					setForeground(Color.white);
				}}, BorderLayout.NORTH);
			}
			p.add(new JLabel(getter.getImage("datafiles/limits/" + data.get(2) + ".png", 35, 35), JLabel.LEFT) {{
				setVerticalAlignment(JLabel.TOP);
				setBorder(createEmptyBorder(0, 3, 0, 0));
			}}, BorderLayout.WEST);
			imageList.add(new JLabel(getter.getImage("datafiles/movies/" + data.get(0) + ".jpg", 120, 200), JLabel.LEFT) {{
				setVerticalAlignment(JLabel.TOP);
				setBackground(Color.red);
				setOpaque(true);
			}});
			p.add(imageList.get(i));
			p.add(new JLabel("") {{ setPreferredSize(new Dimension(40, 0));}}, BorderLayout.EAST);//이건 어떻게 할지 생각해보기!
			p.add(new JTextArea(data.get(1).toString() + "\n1.1% | 개봉일: " + data.get(6)) {{
				setFocusable(false);
				setCursor(getCursor().getDefaultCursor());
			}}, BorderLayout.SOUTH);
			moviePanel.add(p);
		}
	}
	
	private void setAction() {
		ItemListener itemAction = e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				setJScrollPane();
				revalidate();
				repaint();
			}
		};
		order.addItemListener(itemAction);
		genre.addItemListener(itemAction);
		serchBut.addActionListener(e->{
			setJScrollPane();
			if(movieList.isEmpty()) {
				JOptionPane.showMessageDialog(null, "검색결과가 없습니다.", "경고", JOptionPane.ERROR_MESSAGE);
				serch.setText(""); order.setSelectedIndex(0); genre.setSelectedIndex(0);
				setJScrollPane();
			}
			revalidate();
			repaint();
		});
		
		for(int i = 0; i < imageList.size(); i++){
			final int index = i;
			imageList.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int mno = Integer.parseInt(movieList.get(index).get(0).toString());
					if(u_no == -1) { new MovieChage(u_no, mno); }
					else { new MovieInfor(u_no, mno, false); }
					dispose();
				}
			});
		}
	}
	
	public static void main(String[] args) {
		new MovieSerch(-1);
	}
}
