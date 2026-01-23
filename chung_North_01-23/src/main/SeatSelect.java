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
	Font font = new Font("맑은 고딕", 1, 13);
	JPanel mainPanel = new JPanel(new BorderLayout(60,60)) {{
		setBorder(createEmptyBorder(0, 100, 5, 100));
		setBackground(Color.white);
	}};
	JPanel southPanel = new JPanel(new BorderLayout(15,15)) {{
		setPreferredSize(new Dimension(0, 150));
		setBackground(Color.white);
		setBorder(createMatteBorder(1, 0, 0, 0, Color.black));
	}};
	String[] seatEngle = "ABCDEFGHI".split("");
	List<JPanel> seatList = new ArrayList<>();
	List<String> seatNames = new ArrayList<>();
	JComboBox<Object> people = new JComboBox<Object>() {{
		setPreferredSize(new Dimension(60, 25));
		for(int i = 1; i <= 10; i++) addItem(i);
	}};
	
	JLabel priceLabel = new JLabel("0원") {{
		setFont(font.deriveFont(18f));
	}};
	JButton payment = new CustumButton(""){
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.white);
			g.setFont(font.deriveFont(21f));
			g.drawString("좌석", 44, 50);
			g.drawString("결제", 44, 75);
			
		};
	};
	Data data;
	Data movie;
	int x = 1, price = 0;
	SeatSelect(Data data) {
		this.data = data;
		movie = Connections.select("select * from movie where m_no = ?", data.get(2)).get(0);
		setMainPanel();
		setSouthPanel();
		mainPanel.add(new JLabel("스크린", JLabel.CENTER) {{
			setFont(font);
			setBackground(Color.gray);
			setForeground(Color.black);
			setBorder(createLineBorder(Color.black));
			setOpaque(true);
		}}, BorderLayout.NORTH);
		add(mainPanel);
		add(southPanel, BorderLayout.SOUTH);
		payment.setPreferredSize(new Dimension(130, 0));
		setAction();
		setFrame.setframe(this, "좌석 예매", 700, 600);
	}
	
	private void setSouthPanel() {
		JLabel img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/movies/" + data.getInt(2) + ".jpg").getImage(), 3, 3, getWidth()-4, getHeight() - 4, null);
			}
		};
		img.setPreferredSize(new Dimension(110, 100));
		
		JPanel inforPanel = new JPanel(new GridLayout(3, 1));
		inforPanel.setBorder(createEmptyBorder(0,0,50,0));
		inforPanel.setBackground(Color.white);
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.setBackground(Color.white);
		p.add(new JLabel("인원수: ") {{
			setFont(font.deriveFont(18f));
		}});
		p.add(people);
		
		JPanel ePanel = new JPanel(new BorderLayout(5,5));
		ePanel.setBackground(Color.white);
		
		ePanel.add(priceLabel, BorderLayout.NORTH);
		ePanel.add(payment);
		
		inforPanel.add(new JLabel("이름: " + movie.get(1)) {{
			setFont(font.deriveFont(18f));
		}});
		inforPanel.add(new JLabel("일시: " + data.get(3)) {{
			setFont(font.deriveFont(18f));
		}});
		inforPanel.add(p);
		southPanel.add(inforPanel);
		southPanel.add(ePanel, BorderLayout.EAST);
		southPanel.add(img, BorderLayout.WEST);
	}
	private void setMainPanel() {
		JPanel a = new JPanel(new GridLayout(9, 2,5,5));
		a.setBackground(Color.white);
		
		JPanel b = new JPanel(new GridLayout(9, 5,5,5));
		b.setBackground(Color.white);
		
		JPanel c = new JPanel(new GridLayout(9, 2,5,5));
		c.setBackground(Color.white);
		
		mainPanel.add(a, BorderLayout.WEST);
		mainPanel.add(b, BorderLayout.CENTER);
		mainPanel.add(c, BorderLayout.EAST);
		
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				JLabel l = new JLabel(seatEngle[i] + (j + 1),JLabel.CENTER) {
					@Override
					protected void paintComponent(Graphics g) {
						g.drawImage(new ImageIcon("datafiles/icon/좌석.png").getImage(), 0, 2, getWidth(), getHeight(), null);
						super.paintComponent(g);
					}
				};
				l.setFont(font.deriveFont(11f));
				l.setForeground(Color.white);
				l.setVerticalAlignment(JLabel.CENTER);
				l.setBorder(createLineBorder(Color.black));
				l.setPreferredSize(new Dimension(40, 0));
				
				JPanel p = new JPanel(new BorderLayout());
				p.setBackground(Color.white);
				p.add(l);
				seatList.add(p);
				if(j < 2) {
					a.add(p);
				}else if(j < 7) {
					b.add(p);
				}else {
					c.add(p);
				}
			}
		}
	}
	
	LocalDate nows = LocalDate.of(2025, 9, 10);
	LocalDate ageLimit = LocalDate.of(2006, 9, 10);
	LocalDate userD = LocalDate.parse(User.getUser().get(4).toString());
	private void setPrice() {
		int discountPursent = 100;
		if(ageLimit.isBefore(userD)) {
			discountPursent = 90;
		}else if(nows.getDayOfYear() == userD.getDayOfYear()) {
			discountPursent = 80;
		}
		price = (int) ((15000 * seatNames.size()) * (discountPursent / 100.0));
		priceLabel.setText(price + "원");
		revalidate();
		repaint();
	}
	
	private void setAction() {
		for(int i = 0; i < seatList.size(); i++) {
			final int index = i;
			seatList.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int peoples = Integer.parseInt(people.getSelectedItem().toString());
					if(peoples > seatNames.size()) {
						seatList.get(index).setBackground(Color.lightGray);
						seatNames.add(((JLabel) seatList.get(index).getComponent(0)).getText());
						setPrice();
					}
				}
			});
		}
		
		payment.addActionListener(e->{
			if(price > User.getUser().getInt(5)) {
				getter.mg("잔액이 부족합니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int result = JOptionPane.showConfirmDialog(null, "결제하시겠습니까?","결제", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				getter.mg("정보 결제가 완료 되었습니다.", JOptionPane.INFORMATION_MESSAGE);
				Connections.update("update user set u_price = ? where u_no = ?", User.getUser().getInt(5) - price, User.getUno());
				User.setUser(Connections.select("select * from user where u_no = ?", User.getUno()).get(0));
				new Main();
				dispose();
			}
			
		});
		
		people.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				seatNames.clear();
				setPrice();
				for(int i = 0; i < seatList.size(); i++) {
					seatList.get(i).setBackground(Color.white);
				}
			}
		});
	}
	
	public static void main(String[] args) {
		new SeatSelect(Connections.select("select * from schedule where sc_no = 15").get(0));
	}
}
