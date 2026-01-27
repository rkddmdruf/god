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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import utils.*;

public class SeatSelect extends JFrame{
	Font font = new Font("맑은 고딕", 1, 18);
	JPanel mainPanel = new JPanel(new BorderLayout(70, 70)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(0, 100, 10, 100));
	}};
	JPanel southPanel = new JPanel(new BorderLayout(10,10)) {{
		setBorder(createMatteBorder(1, 0, 0, 0, Color.black));
		setBackground(Color.white);
		setPreferredSize(new Dimension(0, 150));
	}};
	
	JComboBox<Object> cb = new JComboBox<Object>() {{
		for(int i = 1; i <= 10; i++) addItem(i);
	}};
	
	JLabel priceLabel = new JLabel("0원");
	int price = 0;
	JButton but = new CustumButton("") {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(font);
			g.setColor(Color.white);
			g.drawString("좌석", 37, 47);
			g.drawString("결제", 37, 75);
		};
	};
	List<JPanel> panels = new ArrayList<>();
	List<String> selectSeat = new ArrayList<>();
	
	boolean isAge19Under = (!LocalDate.of(2025-19, 9, 10).isAfter(LocalDate.parse(User.getUser().get(4).toString())));
	boolean userBirth = (LocalDate.of(2025-19, 9, 10).getDayOfYear() == LocalDate.parse(User.getUser().get(4).toString()).getDayOfYear());
	Data movie;
	Data data;
	SeatSelect(Data data){
		this.data = data;
		movie = Connections.select("select * from movie where m_no = ?", data.get(1)).get(0);
		setMainPanel();
		setSouthPanel();
		add(mainPanel );
		add(southPanel, BorderLayout.SOUTH);
		setAction();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new Reservation(data.getInt(2));
				dispose();
			}
		});
		setFrame.setframe(this, "좌석 예매", 710, 600);
	}
	
	private void setSouthPanel() {
		JLabel img = new JLabel(getter.getImage("datafiles/movies/" + movie.getInt(0) + ".jpg", 110, 145));
		img.setBorder(createEmptyBorder(2, 2, 1, 1));
		
		JPanel p = new JPanel(new GridLayout(3, 1, 5, 5));
		p.setBackground(Color.white);
		p.setBorder(createEmptyBorder(0, 0, 50, 0));
		
		JLabel name = new JLabel("이름: " + movie.get(1));
		name.setFont(font);
		
		JLabel date = new JLabel("일시: " + movie.get(6));
		date.setFont(font);
		
		JLabel people = new JLabel("인원수: ");
		people.setFont(font);
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(Color.white);
		panel.add(people);
		panel.add(cb);
		
		p.add(name);
		p.add(date);
		p.add(panel);
		
		but.setPreferredSize(new Dimension(120, 120));
		priceLabel.setFont(font.deriveFont(17f));
		
		southPanel.add(new JPanel(new BorderLayout(5,5)) {{
			setBackground(Color.white);
			add(priceLabel);
			add(but, BorderLayout.SOUTH);
		}}, BorderLayout.EAST);
		southPanel.add(p);
		southPanel.add(img, BorderLayout.WEST);
	}
	
	private void setPrice() {
		price = selectSeat.size() * 15000;
		if(userBirth) {
			price -= selectSeat.size() * 3000;
		}else if(isAge19Under) {
			price -= selectSeat.size() * 1500;
		}
		priceLabel.setText(price + "원");
		revalidate();
		repaint();
	}
	
	private void setAction() {
		but.addActionListener(e->{
			if(price > User.getUser().getInt(5)) {
				getter.mg("잔액이 부족합니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int result = JOptionPane.showConfirmDialog(null, "결제하시겠습니까?", "결제", JOptionPane.YES_NO_OPTION);
			if(result != JOptionPane.YES_OPTION) return;
			getter.mg("결제가 완료 되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			Data user = User.getUser();
			String seatName = "";
			for(int i = 0; i < selectSeat.size(); i++) {
				seatName += selectSeat.get(i);
				if(selectSeat.size() -1 == i)continue;
				seatName += ",";
			}
			Connections.update("insert into reservation values(0, ?, ?, ?, ?, ?, ?, ?);", user.get(0), movie.get(0), seatName, price, selectSeat.size(), data.get(4), data.get(3));
			Connections.update("update user set u_price = ? where u_no = ?", user.getInt(5) - price, user.get(0));
			user.set(5, user.getInt(5) - price);
			User.setUser(user);
			new Main();
			dispose();
		});
		for(int i = 0; i < panels.size(); i++) {
			final int index = i;
			panels.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String seat = ((JLabel) panels.get(index).getComponent(0)).getText();
					if((cb.getSelectedIndex() + 1) <= selectSeat.size() || selectSeat.contains(seat)) return;
					panels.get(index).setBackground(Color.lightGray);
					selectSeat.add(((JLabel) panels.get(index).getComponent(0)).getText());
					setPrice();
				}
			});
		}
		cb.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				selectSeat = new ArrayList<>();
				for(int i = 0; i < panels.size(); i++) {
					panels.get(i).setBackground(Color.white);
				}
				setPrice();
			}
		});
	}
	
	private void setMainPanel() {
		JLabel top = new JLabel("스크린", JLabel.CENTER);
		top.setBorder(createLineBorder(Color.black));
		top.setBackground(Color.gray);
		top.setOpaque(true);
		
		JPanel a = new JPanel(new GridLayout(9, 2, 5, 5));
		a.setBackground(Color.white);
		
		JPanel b = new JPanel(new GridLayout(9, 5, 5, 5));
		b.setBackground(Color.white);
		
		JPanel c = new JPanel(new GridLayout(9, 2, 5, 5));
		c.setBackground(Color.white);
		
		String[] seatE = "ABCDEFGHI".split("");
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j ++) {
				
				JPanel p = new JPanel(new BorderLayout());
				p.setBackground(Color.white);
				
				JLabel l = new JLabel(seatE[i] + (j + 1), JLabel.CENTER) {
					@Override
					protected void paintComponent(Graphics g) {
						g.drawImage(new ImageIcon("datafiles/icon/좌석.png").getImage(), 0, 0, getWidth(), getHeight(), null);
						super.paintComponent(g);
					}
				};
				l.setVerticalAlignment(JLabel.CENTER);
				l.setForeground(Color.white);
				l.setPreferredSize(new Dimension(40,40));
				l.setBorder(createLineBorder(Color.black));
				
				p.add(l);
				panels.add(p);
				if(j < 2) a.add(p);
				else if(j >= 7) c.add(p);
				else b.add(p);
			}
		}
		
		mainPanel.add(top, BorderLayout.NORTH);
		mainPanel.add(a, BorderLayout.WEST);
		mainPanel.add(b);
		mainPanel.add(c, BorderLayout.EAST);
	}
	
}
