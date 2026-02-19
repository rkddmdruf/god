package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import utils.CFrame;
import utils.Connections;
import utils.Data;
import utils.getter;

public class BuyGameFrom extends CFrame{

	public BuyGameFrom(int g_no) {
		Data data = Connections.select("SELECT gameinformation.g_no, g_name, ca_name, g_price, avg(c_star) FROM game_site.gameinformation\r\n"
				+ "join category on category.ca_no = gameinformation.ca_no join comments on comments.g_no = gameinformation.g_no\r\n"
				+ "where gameinformation.g_no = ? group by comments.g_no", g_no).get(0);
		JPanel p = new JPanel(new BorderLayout(10, 10));
		p.setBackground(Color.white);
		p.setPreferredSize(new Dimension(150, 150));
		p.setBorder(createLineBorder(Color.black));
		
		p.add(new JLabel(getter.getImage("gameimage/" + data.getInt(0) + ".jpg", 150, 150)) {{
			setPreferredSize(new Dimension(150, 150));
		}}, BorderLayout.WEST);
		
		UIManager.put("Label.font", new Font("맑은 고딕", 1, 17));
		JPanel inforPanel = new JPanel(new GridLayout(4, 1, 10, 10));
		inforPanel.setBackground(Color.white);
		
		inforPanel.add(new JLabel("게임명 : " + data.get(1)));
		inforPanel.add(new JLabel("카테고리 : " + data.get(2)));
		inforPanel.add(new JLabel("포인트 : " + getter.df.format(data.getInt(3)) + "포인트"));
		inforPanel.add(new JLabel("평점 : " + data.get(4).toString().substring(0, 3) + "점"));
		
		p.add(inforPanel);
		add(p, BorderLayout.NORTH);
		
		p.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				new GameInfor(data.getInt(0));
			}
		});
		setFrame("구매한 게임", 550, 450);
	}
	
	public static void main(String[] args) {
		new BuyGameFrom(1);
	}
}
