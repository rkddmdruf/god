package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;

import javax.swing.*;

public class SeatSelect extends JFrame{
	Font font = new Font("맑은 고딕", 1, 18);
	JPanel seatPanel = new JPanel(new BorderLayout(50, 50)) {{
		setBorder(createEmptyBorder(0,70 ,5, 70));
		setBackground(Color.white);
	}};
	
	List<String> seatTotal = new ArrayList<>();
	
	JComboBox<Integer> people = new JComboBox<Integer>() {{
		for(int i = 1; i <= 10; i++) addItem(i);
		setPreferredSize(new Dimension(60, 24));
	}};
	JLabel[][] labels = new JLabel[9][9];
	
	JLabel price = new JLabel("0원") {{
		setFont(font);
	}};
	JButton paymenet = new CustumButton("") {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(font);
			g.setColor(Color.white);
			g.drawString("좌석", 40, 60);
			g.drawString("결체", 40, 80);
		};
	};
	Data data;
	Data movie;
	public SeatSelect(Data data) {
		this.data = data;
		movie = Connections.select("select * from movie where m_no = ?", data.get(1)).get(0);
		JLabel screen = new JLabel("스크린", JLabel.CENTER);
		screen.setForeground(Color.black);
		screen.setBorder(createLineBorder(Color.black));
		screen.setBackground(Color.gray);
		screen.setOpaque(true);
		seatPanel.add(screen, BorderLayout.NORTH);
		
		setSeat();
		setSouthPanel();
		add(seatPanel);
		
		setAction();
		A_setFrame.setting(this, "좌석 예매", 600, 600);
	}
	
	private void setSouthPanel() {
		JPanel southPanel = new JPanel(new BorderLayout(10, 10));
		southPanel.setBorder(createCompoundBorder(createMatteBorder(1, 0, 0, 0, Color.black), createEmptyBorder(1,2,0,0)));
		southPanel.setBackground(Color.white);
		southPanel.setPreferredSize(new Dimension(0 ,150));
		
		JPanel gridPanel = new JPanel(new GridLayout(3, 1, 10, 10));
		gridPanel.setBackground(Color.white);
		gridPanel.setBorder(createEmptyBorder(0,0,60,0));
		gridPanel.add(new JLabel("이름: " + movie.get(1), JLabel.LEFT) {{
			setFont(font);
		}});
		gridPanel.add(new JLabel("일시: " + data.get(3), JLabel.LEFT) {{
			setFont(font);
		}});
		JLabel l = new JLabel("인원수: ");
		l.setFont(font);
		gridPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {{
			setBackground(Color.white);
			add(l);
			add(people);
		}});
		
		JPanel moneyPanel = new JPanel(new BorderLayout());
		moneyPanel.setBackground(Color.white);
		moneyPanel.setPreferredSize(new Dimension(120, 0));
		moneyPanel.add(price, BorderLayout.NORTH);
		moneyPanel.add(paymenet);
		
		southPanel.add(moneyPanel, BorderLayout.EAST);
		southPanel.add(gridPanel);
		southPanel.add(new JLabel(getter.getImageIcon("datafiles/movies/" + data.get(1) + ".jpg", 100, 145)), BorderLayout.WEST);
		add(southPanel, BorderLayout.SOUTH);
	}
	
	private void setSeat(){
		JPanel wPanel = new JPanel(new GridLayout(9, 2, 3, 3));
		wPanel.setBackground(Color.white);
		
		JPanel cPanel = new JPanel(new GridLayout(9, 5, 3, 3));
		cPanel.setBackground(Color.white);
		
		JPanel ePanel = new JPanel(new GridLayout(9, 2, 3, 3));
		ePanel.setBackground(Color.white);
		
		for(int i = 65; i < 65 + 9 ;i++) {
			final int index = i;
			for(int j = 0; j < 9; j++) {
				final int jndex = j;
				JLabel l = new JLabel((char) index + "" + (jndex + 1), JLabel.CENTER) { //이거 String 없어도 돼는거
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.drawImage(new ImageIcon("datafiles/icon/좌석.png").getImage(), -2, -2, 40, 40, null);
						g.setFont(font.deriveFont(0, 11));
						g.drawString((char) index + "" + (jndex + 1), 12, 20);
					}
				};
				l.setForeground(Color.white);
				l.setBackground(Color.white);
				l.setOpaque(true);
				l.setVerticalAlignment(JLabel.CENTER);
				l.setPreferredSize(new Dimension(35, 0));
				l.setBorder(createLineBorder(Color.black));
				
				labels[i - 65][j] = l;
				if(j <= 1) wPanel.add(l);
				else if(j >= 7) ePanel.add(l);
				else cPanel.add(l);
			}
		}
		seatPanel.add(wPanel, BorderLayout.WEST);
		seatPanel.add(cPanel, BorderLayout.CENTER);
		seatPanel.add(ePanel, BorderLayout.EAST);
	}
	
	private void setAction() {
		for(int i = 0; i < 9; i++) {
			final int index = i;
			for(int j = 0; j < 9 ;j++) {
				final int jndex = j;
				labels[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						labels[index][jndex].setBackground(Color.lightGray);
						seatTotal.add((char) index + "" + jndex);
						setPrice();
					}
				});
			}
		}
		people.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				seatTotal.clear();
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 9 ;j++) {
						labels[i][j].setBackground(Color.white);
					}
				}
			}
		});
	}
	
	private void setPrice() {
		price.setText(15000 * seatTotal.size() + "원");
	}
	
	public static void main(String[] args) {
		new SeatSelect(Connections.select("SELECT * FROM moviedb.schedule where m_no = 1 and sc_date = \"2025-09-10\" and sc_time = \"00:00:00\";").get(0));
	}
}
