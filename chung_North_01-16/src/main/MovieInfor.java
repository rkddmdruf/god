package main;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

public class MovieInfor extends JFrame{
	Font font = new Font("맑은 고딕", 1, 26);
	
	JPanel borderPanel = new JPanel(new BorderLayout(5,5)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0,5,5,5));
	}};
	
	Data movie;
	
	JPanel mainPanel = new JPanel(new BorderLayout(5,5)) {{
		setBorder(createEmptyBorder(10,5,5,5));
		setBackground(Color.white);
	}};
	JScrollPane sc = new JScrollPane(mainPanel);
	
	JButton reservation = new CustumButton("예매하기");
	int m_no = 0;
	
	MovieInfor(int m_no){
		this.m_no = m_no;
		movie = Connections.select("select * from movie where m_no = ?", m_no).get(0);
		System.out.println(movie);
		borderPanel.add(new NorthPanel(this), BorderLayout.NORTH);
		borderPanel.add(sc);
		setNorthPanel();
		setCenterPanel();
		setSouthPanel();
		add(borderPanel);
		reservation.addActionListener(e->{
			if(User.getData() == null) {
				JOptionPane.showMessageDialog(null, "로그인을 해주세요", "정보", JOptionPane.ERROR_MESSAGE);
				new Login();
				dispose();
				return;
			}
			LocalDate dateUser = LocalDate.parse(User.getData().get(4).toString());
			LocalDate age = LocalDate.of(2025,9,10);
			
			if(movie.getInt(2) == 4) {
				if(age.getYear() - dateUser.getYear() - 1 < 18) {
					if(age.getDayOfYear() - dateUser.getDayOfYear() >= 0) {
						JOptionPane.showMessageDialog(null, "미성년자는 시청 금지입니다", "경고", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
			new Reservation(m_no);
			dispose();
		});
		SetFrames.setframe(this, "영화 정보", 800, 450);
	}
	
	private void setNorthPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBackground(Color.white);
		panel.setBorder(createEmptyBorder(0,0,20,0));
		JLabel movieLabel = new JLabel(getter.getImageIcon("datafiles/movies/" + m_no + ".jpg", 170, 225)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/limits/" + movie.get(2) + ".png").getImage(), 5, 0, 30, 30, null);
			}
		};
		movieLabel.setVerticalAlignment(JLabel.BOTTOM);
		movieLabel.setPreferredSize(new Dimension(170, 227));
		
		JPanel inforPanel = new JPanel(new BorderLayout());
		inforPanel.setBackground(Color.white);
		
		JPanel gridPanel = new JPanel(new GridLayout(3, 1, 10, 10));
		gridPanel.setBackground(Color.white);
		gridPanel.add(new JLabel("감독: " + movie.get(3)));
		gridPanel.add(new JLabel("장르: " + Connections.select("select g_name from genre where g_no = ?", movie.getInt(5)).get(0).get(0)));
		gridPanel.add(new JLabel("개봉일: " + movie.get(6)));
		
		inforPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			setBorder(createEmptyBorder(40,0,30,0));
			add(reservation);
		}}, BorderLayout.SOUTH);
		inforPanel.add(gridPanel);
		inforPanel.add(new JLabel("제목: " + movie.get(1)) {{
			setFont(font);
		}}, BorderLayout.NORTH);
		
		panel.add(inforPanel);
		panel.add(movieLabel, BorderLayout.WEST);
		mainPanel.add(panel, BorderLayout.NORTH);
	}
	
	private void setCenterPanel() {
		JTextArea infor = new JTextArea(movie.get(4).toString());
		infor.setFont(font.deriveFont(20f));
		infor.setFocusable(false);
		infor.setCursor(getCursor().getDefaultCursor());
		mainPanel.add(new JScrollPane(infor) {{
			setPreferredSize(new Dimension(0, 225));
		}});
	}
	
	private void setSouthPanel() {
		Color[] colors = {Color.red, Color.blue, Color.yellow, Color.green};
		JPanel southPanel = new JPanel(new BorderLayout(5,5));
		southPanel.setBackground(Color.white);
		southPanel.setPreferredSize(new Dimension(0, 300));
		
		List<Integer> ageList = new ArrayList<>();
		int total = 0;
		for(int i = 0; i < 4; i++) {
			int size = Connections.select("select *\r\n"
					+ "from (\r\n"
					+ "SELECT \r\n"
					+ "case \r\n"
					+ "  when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 19 then 0\r\n"
					+ "  when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 13 then 1\r\n"
					+ "  when timestampdiff(YEAR, u_birth, \"2025-09-10\") >= 5 then 2\r\n"
					+ "  else 3\r\n"
					+ "  end as times\r\n"
					+ " FROM moviedb.review\r\n"
					+ "join user on user.u_no = review.u_no\r\n"
					+ "where m_no = ?\r\n"
					+ ") as c where times = ?", m_no, i).size();
			total += size;
			ageList.add(size);
		}
		int totals = total;
		JLabel chart = new JLabel("") {
			private double start = 90;
			private double end = 0;
			private double theta = 360.0 / totals;
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				for(int i = 0; i < 4; i++) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(colors[i]);
					start += end;
					end = theta * ageList.get(i);
					Arc2D.Double arc = new Arc2D.Double(20, 60, 200, 200, start, end, Arc2D.PIE);
					g2.fill(arc);
				}
			}
		};
		chart.setPreferredSize(new Dimension(235,300));
		
		JPanel gridPanel = new JPanel(new GridLayout(4, 1, 1, 1));
		gridPanel.setBorder(createEmptyBorder(30, 0, 120, 0));
		gridPanel.setBackground(Color.white);
		
		String[] ageName = "성인,청소년,어린이,유아".split(",");
		
		for(int i = 0; i < 4; i++) {
			final int index = i;
			JLabel l = new JLabel() {
				private int pursent = 100 / totals;
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setFont(font.deriveFont(1,19));
					g.setColor(colors[index]);
					g.fillRect(0, 3, 20, 20);
					g.setColor(Color.black);
					g.drawString(ageName[index], 21, 20);
					g.drawString((ageList.get(index) * pursent) + "%", 80, 20);
				}
			};
			gridPanel.add(l);
		}
		
		JPanel scPanel = new JPanel(new GridLayout(0, 1, 3, 3));
		scPanel.setBackground(Color.white);
		List<Data> reviews = Connections.select("SELECT user.u_no, u_name, re_com\r\n"
				+ " FROM moviedb.review\r\n"
				+ "join user on user.u_no = review.u_no\r\n"
				+ "where m_no = ?", m_no);
		for(int i = 0; i < reviews.size(); i++) {
			Data r = reviews.get(i);
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(Color.white);
			p.setBorder(createLineBorder(Color.black));
			
			JPanel rePanel = new JPanel(new GridLayout(2, 1, 7,7));
			rePanel.setBackground(Color.white);
			rePanel.add(new JLabel(r.get(1).toString()));
			rePanel.add(new JLabel(r.get(2).toString()));
			
			p.add(new JLabel(getter.getImageIcon("datafiles/user/" + r.getInt(0) + ".jpg", 40, 40)), BorderLayout.WEST);
			p.add(rePanel);
			scPanel.add(p);
		}
		for(int i = 0; i < 7 - reviews.size(); i++) {
			scPanel.add(new JLabel(""));
		}
		
		southPanel.add(new JScrollPane(scPanel) {{
			setPreferredSize(new Dimension(350, 0));
		}}, BorderLayout.EAST);
		southPanel.add(gridPanel);
		southPanel.add(chart, BorderLayout.WEST);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		new MovieInfor(1);
	}
}
