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
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import static javax.swing.BorderFactory.*;

import utils.*;

public class SeatSelect2 extends CFrame{
	JPanel southPanel = new JPanel(new BorderLayout(20, 20)) {{
		setPreferredSize(new Dimension(100, 150));
		setBorder(createMatteBorder(1, 0, 0, 0, Color.black));
		setBackground(Color.white);
	}};
	Data data;
	Data movie;
	JComboBox<Integer> people = new JComboBox<Integer>() {{
		for(int i = 1; i <= 10; i++)
			addItem(i);
	}};
	JLabel priceLabel = new JLabel("0원");
	JButton priceButton = new CButton("<html><font color='white'>좌석<br>결제</font color><html>") {{
		setFont(new Font("맑은 고딕", 1, 20));
		setPreferredSize(new Dimension(150, 150));
	}};
	List<JLabel> labels = new ArrayList<>();
	List<String> seats = new ArrayList<>();//setBorder(createEmptyBorder(0, 60, 20, 60));//40, 50
	CPanel mainPanel = new CPanel(625 - 120) {{
		setBorder(createEmptyBorder(0, 60, 20, 60));
	}};
	List<Data> reservationList;
	int price = 0;
	
	boolean under19;
	boolean birth;
	private void setPrice() {
		price = 15000 * seats.size();
		if(under19) {
			price -= (seats.size() * 1500);
		}else if(birth) {
			price -= (seats.size() * 3000);
		}
		priceLabel.setText(price + "원");
	}
	public SeatSelect2(Data data) {
		this.data = data;
		
		LocalDate d_Day = LocalDate.parse(data.getString(3)).minusYears(19);
		LocalDate uDay = User.getBirth();
		under19 = !d_Day.isAfter(uDay);
		birth = d_Day.getMonthValue() == uDay.getMonthValue() && d_Day.getDayOfMonth() == uDay.getDayOfMonth();
		
		movie = Connections.select("SELECT * FROM movie where m_no = ?;", data.get(1)).get(0);
		reservationList = Connections.select("SELECT * FROM moviedb.reservation where m_no = ? and r_date = ? and r_time = ?;", movie.get(0), data.get(3), data.get(4));
		
		setSouthPanel();
		setMainPanel();
		borderPanel.add(mainPanel);
		borderPanel.add(southPanel, BorderLayout.SOUTH);
		
		people.addItemListener(e -> { reSet(); });
		
		priceButton.addActionListener(e -> {
			if(price > User.getUser().getInt(5)) {
				getter.err("잔액이 부족합니다.");
				return;
			}
			if(JOptionPane.showConfirmDialog(null, "결제하시겠습니까?", "결제", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				getter.infor("결제가 완료되었습니다.");
				getter.frames.clear();
				Connections.update("update user set u_price = ? where u_no = ?", User.getUser().getInt(5) - price, User.getUno());
				String seatString = String.join(",", seats.stream().map(c -> c).collect(Collectors.toList()));
				Connections.update("insert into reservation values(0, ?, ?, ?, ?, ?, ?, ?);", User.getUno(), movie.get(0), seatString, price, seats.size(), data.get(4), data.get(3));
				//new Main();
				dispose();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				new Reservation(movie.getInt(0));
			};
		});
		setFrame("좌석 선택", 625, 650);
	}
	
	private void setSouthPanel() {
		southPanel.add(new JLabel(getter.getImage("datafiles/movies/" + data.getInt(1) + ".jpg", 120, 150)), BorderLayout.WEST);
		JPanel inforPanel = new JPanel(new GridLayout(3, 1));
		inforPanel.setBackground(Color.white);
		inforPanel.add(new JLabel("이름: " + movie.get(1)));
		inforPanel.add(new JLabel("일시: " + data.getString(3)));
		inforPanel.add(new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {{
			setBackground(Color.white);
			add(new JLabel("인원수:  "));
			add(people);
		}});
		inforPanel.setBorder(createEmptyBorder(0, 0, 30, 0));
		southPanel.add(inforPanel);
		
		JPanel payPanel = new JPanel(new BorderLayout());
		payPanel.setBackground(Color.white);
		payPanel.add(priceLabel, BorderLayout.NORTH);
		priceLabel.setPreferredSize(new Dimension(120, 30));
		priceButton.setPreferredSize(new Dimension(120, 120));
		payPanel.add(priceButton);
		
		southPanel.add(payPanel, BorderLayout.EAST);
	}
	
	private void setMainPanel() {
		mainPanel.add(625 - 120, 20, new JLabel("스크린", JLabel.CENTER) {{
			setBorder(createLineBorder(Color.black));
			setBackground(Color.gray);
			setOpaque(true);
		}});
		mainPanel.nextLine(40);
		
		for(String s : "ABCDEFGHI".split("")) {
			for(int j = 0; j < 9; j++) {
				JLabel label = new JLabel(s + (j + 1), JLabel.CENTER) {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
					}
				};
				label.setForeground(Color.white);
				label.setBackground(Color.white);
				label.setOpaque(true);
				label.setIcon(getter.getImage("datafiles/icon/좌석.png", 40, 40));
				label.setPreferredSize(new Dimension(40, 40));
				System.out.println(label.getWidth());
				label.setBorder(createLineBorder(Color.black));
				
				label.setHorizontalTextPosition(JLabel.CENTER);
				label.setHorizontalAlignment(JLabel.CENTER);
				
				label.setVerticalTextPosition(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				
				label.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(!label.isEnabled()) return;
						if(seats.size() >= people.getSelectedIndex() + 1) return;
						if(label.getBackground() != Color.white) return;
						label.setBackground(Color.LIGHT_GRAY);
						seats.add(label.getText());
						setPrice();
					}
				});
				labels.add(label);
				mainPanel.add(40, 40, label);
				if(j != 8) mainPanel.gap(7, 7);
				if(j == 1) mainPanel.gap(30, 1);
				if(j == 6) mainPanel.gap(30, 1);
			}
			mainPanel.nextLine(7);
		}
		
		reSet();
	}
	
	private void reSet() {
		seats.clear();
		labels.forEach(e ->{ 
			JLabel l = e;
			e.setBackground(Color.white); 
			for(Data data : reservationList) {
				String[] str = data.getString(3).split(",");
				for(String s : str)
					if(l.getText().equals(s))
						e.setEnabled(false);
			}
		});
		setPrice();
		
	}
	public static void main(String[] args) {
		new SeatSelect2(Connections.select("SELECT * FROM moviedb.schedule where sc_no = 187;").get(0));
	}
}
