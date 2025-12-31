package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Main extends JFrame{
	
	JLabel logo = new JLabel(new ImageIcon(new ImageIcon("datafiles/로고1.jpg").getImage().getScaledInstance(125, 50, Image.SCALE_SMOOTH)));
	Font font = f(0, 12);
	JButton foodKiosc = new JButton("영화 전체보기") {{
		setFont(f(0,10));
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
	JButton movieAllShow = new JButton("먹거리키오스크") {{
		setFont(f(0,10));
		setBackground(Color.blue);
		setForeground(Color.white);
	}};
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
	JPanel borderPanel = new JPanel(new BorderLayout(10,10)) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}};
	int n1 = 1, n2 = 2;
	
	Main(){
		setNorthPanel();
		setCenterPanel();
		setSouthPanel();
		setFrame();
		
	}
	private Font f(int font, int size) {
		return new Font("맑은 고딕", font, size);
	}
	private void setNorthPanel() {
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		panel.add(logo, BorderLayout.WEST);
		JPanel gridPanel = new JPanel(new GridLayout(0, 2, 5,5));
		gridPanel.setBackground(Color.white);
		gridPanel.add(Login);
		gridPanel.add(movieSerch);
		panel.add(gridPanel, BorderLayout.EAST);
		borderPanel.add(panel, BorderLayout.NORTH);
	}
	
	private void setCenterPanel() {
		List<List<Object>> list = new ArrayList<>();
		getList(list, "select * from movie where m_no in (6, 2, 32, 9, 18);");
		JPanel panel = new JPanel(null);
		JLabel img1 = new JLabel(new ImageIcon(new ImageIcon("datafiles/advertising/" + n1 +".jpg").getImage().getScaledInstance(590, 200, Image.SCALE_SMOOTH)));
		img1.setBounds(0,0,590,200);
		JLabel img2 = new JLabel(new ImageIcon(new ImageIcon("datafiles/advertising/" + n2 + ".jpg").getImage().getScaledInstance(590, 200, Image.SCALE_SMOOTH)));
		img2.setBounds(590,0,590,200);
		panel.add(img1);
		panel.add(img2);
		borderPanel.add(panel);
		Timer t = new Timer(2000, e->{
			System.out.println(list.get(n1).get(2).toString() + " + " + list.get(n1+1).get(2).toString());
			img1.setIcon(new ImageIcon(new ImageIcon("datafiles/advertising/" + n1 +".jpg").getImage().getScaledInstance(590, 200, Image.SCALE_SMOOTH)));
			img2.setIcon(new ImageIcon(new ImageIcon("datafiles/advertising/" + n2 +".jpg").getImage().getScaledInstance(590, 200, Image.SCALE_SMOOTH)));
			new Thread() {
				public void run() {
					for(int i = 0; i < 590; i++) {
						img1.setBounds(0-i, 0, 590, 200);
						img2.setBounds(590-i, 0, 590, 200);
						try {
							Thread.sleep(1);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			}.start();
			n1++;
			n2++;
			if(n2 == 2) {
				n1 = 1;
			}
			if(n2 == 6) {
				n2 = 1;
			}
		});
		t.start();
	}
	private void setSouthPanel() {
		JPanel southPanel = new JPanel(new GridLayout(0, 2, 5, 5));
		JPanel wPanel = new JPanel(new BorderLayout());
		JPanel butPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel moviePanel = new JPanel(null);
		butPanel.add(foodKiosc);
		butPanel.add(movieAllShow);
		wPanel.add(butPanel, BorderLayout.NORTH);
		
		List<List<Object>> list = new ArrayList<>();
		getList(list, " SELECT movie.*, count(movie.m_no) as c FROM moviedb.reservation\r\n"
				+ "join movie on movie.m_no = reservation.m_no\r\n"
				+ "group by movie.m_no order by c desc, m_no limit 10");
		for(int i = 0; i < 10; i++) {final int index = i;
			JPanel p = new JPanel(new BorderLayout());
			JLabel img = new JLabel() {
				@Override
				public void paintComponent(Graphics g) {
					g.setFont(f(1, 20));
					g.setColor(Color.white);
					g.drawString(index+"", 10, 10);
					g.drawImage(new ImageIcon("datafiles/movies/" + list.get(index).get(0) + ".jpg").getImage(), 0, 0, 100, 140, null);
				}
			};
			p.add(img);
			p.add(new JLabel(list.get(index).get(1).toString(), JLabel.CENTER) {{
				setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				setFont(f(0,11));
			}}, BorderLayout.SOUTH);
			
			p.setBounds(i * 115, 10, 100, 160);
			moviePanel.add(p);
		}
		wPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(2, 2, 0, 5)));
		wPanel.add(moviePanel);
		southPanel.add(wPanel);
		southPanel.setPreferredSize(new Dimension(0, 210));
		southPanel.setBackground(Color.white);
		southPanel.add(new JLabel("sdfds"));
		borderPanel.add(southPanel, BorderLayout.SOUTH);
	}
	private void setFrame() {
		setTitle("메인");
		add(borderPanel);
		setIconImage(new ImageIcon("datafiles/로고1.jpg").getImage());
		getContentPane().setBackground(Color.white);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		int w = 600 + 16; int h = 500 + 39;
		setBounds(960 - (w / 2), 540 - (h / 2), w, h);
		setVisible(true);
	}
	
	private void getList(List<List<Object>> list, String string) {
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/moviedb?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			Statement ps = c.createStatement();
			ResultSet re = ps.executeQuery(string);
			while(re.next()) {
				List<Object> l = new ArrayList<>();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					l.add(re.getObject(i+1));
				}
				list.add(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Main();
	}
}
