package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import utils.CFrame;
import utils.Connections;
import utils.CustumButton;
import utils.Data;
import utils.User;

public class FoodInfor extends CFrame{
	Font font = new Font("맑은 고딕", 1, 26);
	JPanel borderPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(3,3,3,3));
	}};
	
	JLabel priceLabel = new JLabel("sdfsdf") {{
		setFont(font);
	}};
	
	JButton minus = new CustumButton("-") {{
		setEnabled(false);
		setFont(font);
	}};
	JButton puls = new CustumButton("+") {{
		setFont(font);
	}};
	JButton dispose = new CustumButton("취소") {{
		setBackground(Color.red);
		setFont(font);
	}};
	JButton buy = new CustumButton("구매") {{
		setFont(font);
	}};
	JLabel totalLabel = new JLabel("1", JLabel.CENTER) {{
		setPreferredSize(new Dimension(0, 75));
		setBorder(createLineBorder(Color.black));
		setFont(font);
	}};
	LocalDate nows = LocalDate.of(2025, 9, 10);
	LocalDate userD = LocalDate.parse(User.getUser().get(4).toString());
	
	boolean ubirth = nows.getMonthValue() == userD.getMonthValue() &&
					 nows.getDayOfMonth() == nows.getDayOfMonth();
	int fno, price, total = 1;
	Data food;
	FoodInfor(int fno){
		this.fno = fno;
		food = Connections.select("select * from food where f_no = ?", fno).get(0);
		setNorth();
		JLabel img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/foods/" + food.getInt(0) + ".jpg").getImage(), 0, 50, getWidth(), getHeight() - 100, null);
			}
		};
		img.setBorder(createLineBorder(Color.black));
		borderPanel.add(img);
		setMain();
		add(borderPanel);
		setAction();
		setFrame("메뉴 정보", 425, 550);
	}
	
	private void setAction() {
		ActionListener ac = e->{
			total += (e.getSource() == minus ? -1 : 1);
			puls.setEnabled(total != 10);
			minus.setEnabled(total != 1);
			setPrice();
		};
		minus.addActionListener(ac);
		puls.addActionListener(ac);
	}

	private void setMain() {
		JPanel p = new JPanel(new BorderLayout(5, 5));
		p.setBackground(Color.white);
		
		JPanel gridPanel = new JPanel(new GridLayout(0, 2, 5, 5));
		gridPanel.setPreferredSize(new Dimension(0, 75));
		gridPanel.setBackground(Color.white);
		
		gridPanel.add(dispose);
		gridPanel.add(buy);
		
		p.add(gridPanel, BorderLayout.SOUTH);
		p.add(minus, BorderLayout.WEST);
		p.add(puls, BorderLayout.EAST);
		p.add(totalLabel);
		
		borderPanel.add(p, BorderLayout.SOUTH);
	}

	private void setNorth() {
		setPrice();
		JPanel nPanel = new JPanel(new GridLayout(2, 1));
		nPanel.setBackground(Color.white);
		
		nPanel.add(new JLabel("이름: " + food.get(1)) {{
			setFont(font);
		}});
		nPanel.add(priceLabel);
		
		borderPanel.add(nPanel, BorderLayout.NORTH);
	}

	private void setPrice() {
		price = total * food.getInt(2);
		if(ubirth)
			price /= 2;
		totalLabel.setText(total+"");
		priceLabel.setText("가격: " + new DecimalFormat("###,###").format(price));
		revalidate();
		repaint();
	}
}
