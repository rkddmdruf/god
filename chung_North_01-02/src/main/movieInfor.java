package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class movieInfor extends JFrame{
	JLabel logo = new JLabel(new ImageIcon(new ImageIcon("datafiles/로고1.jpg").getImage().getScaledInstance(125, 50, Image.SCALE_SMOOTH)));
	Font font = new Font("맑은 고딕", 0, 13);
	JButton Login = new JButton("로그인") {{
		setFont(font);
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	JButton movieSerch = new JButton("영화 검색") {{
		setFont(font);
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	JButton 예매 = new JButton("예매하기"){{
		setFont(new Font("맑은 고딕", 0, 10));
		setBackground(Color.blue);
		setForeground(Color.white);
	}};;
	JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	JScrollPane sc = new JScrollPane(mainPanel);
	double start = 0;
	double end = 0;
	
	int m_no = 0, u_no = 0;
	List<Object> movie = new ArrayList<>();
	movieInfor(int m_no, int u_no){
		this.m_no = m_no; this.u_no = u_no;
		if(u_no != 0 ) {
			Login.setText("내정보");
		}
		movie = setList("SELECT movie.*, genre.g_name FROM moviedb.movie \r\n"
				+ "join genre on genre.g_no = movie.g_no \r\n"
				+ "where m_no = ?;", m_no).get(0);
		add(borderPanel);
		borderPanel.add(sc);
		setFrame();
		setLogoPanel();
		setMainPanel();
		revalidate();
		repaint();
	}
	
	private void setMainPanel() {
		JPanel panel = new JPanel(new BorderLayout(5,5));
		JLabel img = new JLabel() {
			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(new ImageIcon("datafiles/movies/" + m_no + ".jpg").getImage(), 0, 5, 160, 240, null);
				g.drawImage(new ImageIcon("datafiles/limits/" + movie.get(2) + ".png").getImage(), 10, 2, 25,25, null);
			}
		};
		JLabel name = new JLabel("제목: " + movie.get(1));
		name.setFont(new Font("맑은 고딕", 1, 24));
		JTextArea ta = new JTextArea("감독: " + movie.get(3) + "\n\n\n"
				+ "장르: " + movie.get(movie.size()-1) + "\n\n\n"
						+ "개봉일: " + movie.get(6));
		ta.setFocusable(false);
		JPanel 예매판넬 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		예매판넬.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
		예매판넬.add(예매);
		JPanel inforPanel = new JPanel(new BorderLayout(20,20));
		inforPanel.add(name, BorderLayout.NORTH);
		inforPanel.add(ta);
		inforPanel.add(예매판넬, BorderLayout.SOUTH);
		img.setPreferredSize(new Dimension(160, 240));
		img.setVerticalAlignment(JLabel.TOP);
		panel.add(inforPanel);
		panel.add(img, BorderLayout.WEST);
		mainPanel.add(panel, BorderLayout.NORTH);
		///아래는 센터 위는 북쪽
		
		
		JTextArea t = new JTextArea(movie.get(4).toString());
		t.setFont(new Font("맑은 고딕", 1, 20));
		t.setLineWrap(true);
		t.setFocusable(false);
		mainPanel.add(new JScrollPane(t) {{
			setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(20,0,0,0), BorderFactory.createLineBorder(Color.black)));
			setPreferredSize(new Dimension(0, 200));
		}});
		
		//위는 센터 아래는 남쪽
		
		
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.setPreferredSize(new Dimension(0,250));
		List<List<Object>> review = setList("SELECT review.*, u_birth, u_name, user.u_no FROM moviedb.review \r\n"
				+ "join user on review.u_no = user.u_no\r\n"
				+ "where m_no = ?;", m_no);
		List<List<Integer>> ages = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			ages.add(new ArrayList<>());
		}
		int total = review.size();

		for(int i = 0; i < review.size(); i++) {
			int age = LocalDate.of(2025, 9, 10).getYear() - LocalDate.parse(review.get(i).get(6).toString()).getYear();
			System.out.println(age);
			if(age >= 19) ages.get(0).add(i);
			else if(age >= 13) ages.get(1).add(i);
			else if(age >= 5 ) ages.get(2).add(i);
			else if(age <  5 ) ages.get(3).add(i);
		}
		JLabel arc = new JLabel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				for(int i = 0; i < 4; i++) {
					for(int j = 0; j < ages.get(i).size(); j++) {
						Color c = null;
						if(i == 0) c = Color.red;
						if(i == 1) c = Color.blue;
						if(i == 2) c = Color.yellow;
						if(i == 3) c = Color.green;
						g.setColor(c);
						start = start + end;
						end = 360.0 / total;
						System.out.println(c);
						Arc2D.Double arc = new Arc2D.Double(10, 20, 200, 200, 90 + start, end, Arc2D.PIE);
						g2.fill(arc);
					}
				}
			}
		};
		JPanel persent = new JPanel(new GridLayout(4, 0));
		persent.setBorder(BorderFactory.createEmptyBorder(50,0,50,0));
		String[] string = "성인,청소년,어린이,유아".split(",");
		for(int i = 0; i < 4; i++) {final int index = i;
			JLabel l = new JLabel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					Color c = null;
					if(index == 0) c = Color.red;
					if(index == 1) c = Color.blue;
					if(index == 2) c = Color.yellow;
					if(index == 3) c = Color.green;
					g.setColor(c);
					g.fillRect(0, 0, 20, 20);
					g.setColor(Color.black);
					g.setFont(new Font("맑은 고딕", 1, 16));
					g.drawString(string[index], 30, 15);
					g.drawString((ages.get(index).size() == 0 ? 0 : (100 / total) * ages.get(index).size()) + "%", 100, 15);
					
				}
			};
			persent.add(l);
		}
		
		JPanel reCom = new JPanel(new GridLayout(0, 1, 10, 10));
		reCom.setBorder(BorderFactory.createLineBorder(Color.black));
		for(int i = 0; i < review.size(); i++) {final int index = i;
			JPanel com = new JPanel(new BorderLayout(5, 5));
			com.setPreferredSize(new Dimension(380, 60));
			com.setBorder(BorderFactory.createLineBorder(Color.black));
			com.add(new JLabel(new ImageIcon(new ImageIcon("datafiles/user/" + review.get(i).get(8) + ".jpg").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH))), BorderLayout.WEST);
			com.add(new JPanel(new BorderLayout(5,5)) {{
				setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
				add(new JLabel(review.get(index).get(7).toString()), BorderLayout.NORTH);
				add(new JLabel(review.get(index).get(3).toString()), BorderLayout.SOUTH);
			}});
			reCom.add(com);
		}
		for(int i = 0; i < 3; i++) {
			reCom.add(new JLabel(""));
		}
		JScrollPane scroll = new JScrollPane(reCom);
		scroll.setPreferredSize(new Dimension(400, 100));
		arc.setPreferredSize(new Dimension(250, 250));
		southPanel.add(arc, BorderLayout.WEST);
		southPanel.add(persent);
		southPanel.add(scroll, BorderLayout.EAST);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
	}
	
	
	
	private void setLogoPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.add(logo, BorderLayout.WEST);
		p.add(new JPanel(new GridLayout(0,2,5,5)) {{
			add(Login);
			add(movieSerch);
		}}, BorderLayout.EAST);
		borderPanel.add(p, BorderLayout.NORTH);
	}
	
	private void setAction() {
		예매.addActionListener(e->{
			if(u_no == 0) {
				JOptionPane.showMessageDialog(null, "로그인을 해주세요", "경고", JOptionPane.ERROR_MESSAGE);
				new Login();
				dispose();
				return;
			}
			new 예매(u_no, m_no);
			dispose();
		});
	}
	
	private void setFrame() {
		setTitle("영화 정보");
		setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		int w = 850 + 16; int h = 450 + 39;
		setBounds(960 - (w / 2), 540 - (h / 2), w, h);
		setVisible(true);
	}
	
	private List<List<Object>> setList(String string, Object...val) {
		List<List<Object>> list = new ArrayList<>();
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement st = c.prepareStatement(string);
			for(int i = 0; i < val.length; i++) {
				st.setObject(i+1, val[i]);
			}
			ResultSet re = st.executeQuery();
			while(re.next()) {
				List<Object> l = new ArrayList<>();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++)
					l.add(re.getObject(i+1));
				list.add(l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		new movieInfor(1, 1);
	}
}
