package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

public class FoodInfor extends JFrame{
	Font font = new Font("맑은 고딕", 1, 18);
	JPanel borderPanel = new JPanel(new BorderLayout());
	
	JButton puls = new CustumButton("+");
	JButton minus = new CustumButton("-");
	
	JButton buy = new CustumButton("구매");
	JButton reper = new CustumButton("취소");
	
	JLabel totalLabel = new JLabel("", JLabel.CENTER);
	JLabel priceLabel = new JLabel("");
	int total = 1;
	int price;
	Data data;
	public FoodInfor(Data data) {
		this.data = data;
		price = data.getInt(2);
		setting();
		
		setNorthPanel();
		
		JLabel img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/foods/" + data.getInt(0) + ".jpg").getImage(), 0, 50, getWidth(), getHeight()-100, null);
			}
		};
		img.setBorder(createLineBorder(Color.black));
		borderPanel.add(img);
		
		setSouthPanel();
		
		add(borderPanel);
		
		setAction();
		setFrame.setframe(this, "메뉴 정보", 500, 600);
	}
	
	private void setAction() {
		ActionListener ac = e->{
			if(e.getSource() == puls) total++;
			else total--;
			totalLabel.setText(total + "");
			
			if(total == 1) {
				minus.setEnabled(false);
				total = 1;
			}else {
				minus.setEnabled(true);
			}
			
			if(total == 10) {
				puls.setEnabled(false);
				total = 10;
			}else {
				puls.setEnabled(true);
			}
			
			setPrice();
		};
		
		puls.addActionListener(ac);
		minus.addActionListener(ac);
		
		reper.addActionListener(e->{
			new Kiosc();
			dispose();
		});
		
		buy.addActionListener(e->{
			int result = JOptionPane.showConfirmDialog(null, "결제하시겠습니까?", "결재", JOptionPane.YES_NO_OPTION);
			if(result != JOptionPane.YES_OPTION) return;
			if(price > User.getUser().getInt(5)) {
				getter.mg("잔액이 부족합니다.", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getter.mg("결제가 완료되었습니다.", JOptionPane.INFORMATION_MESSAGE);
			Connections.update("update user set u_price = ? where u_no = ?", User.getUser().getInt(5) - price, User.getUno());
			User.setUser(Connections.select("select * from user where u_no = ?", User.getUno()).get(0));
			Connections.update("insert into fb values(0, ?, ?, ?)", User.getUno(), data.get(0), total);
			new Main();
			dispose();
		});
	}
	private void setSouthPanel() {
		JPanel p = new JPanel(new BorderLayout(5,5));
		p.setBackground(Color.white);
		
		p.add(minus, BorderLayout.WEST);
		p.add(puls, BorderLayout.EAST);
		p.add(totalLabel);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 2, 3, 3));
		gridPanel.add(reper);
		gridPanel.add(buy);
		
		p.add(gridPanel, BorderLayout.SOUTH);
		borderPanel.add(p, BorderLayout.SOUTH);
	}
	private void setNorthPanel() {
		JPanel p = new JPanel(new GridLayout(2, 1, 3, 3));
		p.setBackground(Color.white);
		
		p.add(new JLabel("이름: " + data.get(1)) {{
			setFont(font.deriveFont(22f));
		}});
		p.add(priceLabel);
		
		borderPanel.add(p, BorderLayout.NORTH);
	}
	
	int discount = (LocalDate.parse(User.getUser().get(4).toString()).getDayOfYear() == LocalDate.of(2025, 9, 10).getDayOfYear()) ? 50 : 100;
	private void setPrice() {
		price = (int) ((data.getInt(2) * total) / (100.0 / discount));
		priceLabel.setText("가격: " + getter.df.format(price));
	}
	
	private void setting() {
		borderPanel.setBackground(Color.white);
		borderPanel.setBorder(createEmptyBorder(3,3,3,3));
		
		puls.setFont(font);
		minus.setFont(font);
		minus.setEnabled(false);
		buy.setFont(font);
		buy.setPreferredSize(new Dimension(0, 75));
		reper.setFont(font);
		reper.setPreferredSize(new Dimension(0, 75));
		reper.setBackground(Color.red);
		
		priceLabel.setFont(font.deriveFont(22f));
		setPrice();
		
		totalLabel.setFont(font);
		totalLabel.setText(total + "");
		totalLabel.setPreferredSize(new Dimension(0, 75));
		totalLabel.setBorder(createCompoundBorder(createLineBorder(Color.black), createEmptyBorder(5,5,5,5)));
		
	}
}
