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

import orm.*;
import ormDb.*;
import utils.*;

public class BuyGameFrom extends CFrame{

	public BuyGameFrom(int g_no) {
		Tuple data = Entity2.select(Gameinformation.class, Category.CA_NAME.getNev(), Nev.avg(Comments.C_STAR.getNev(), "a")).from(Gameinformation.class)
				.join(Category.CA_NO.getNev(), Comments.G_NO.getNev())
				.where(Gameinformation.G_NO.eq(g_no)) .group(Comments.G_NO.getNev())
				.push().get(0);
		JPanel p = new JPanel(new BorderLayout(10, 10));
		p.setBackground(Color.white);
		p.setPreferredSize(new Dimension(150, 150));
		p.setBorder(createLineBorder(Color.black));
		
		p.add(new JLabel(getter.getImage("gameimage/" + data.getInt(Gameinformation.G_NO.getName()) + ".jpg", 150, 150)) {{
			setPreferredSize(new Dimension(150, 150));
		}}, BorderLayout.WEST);
		
		UIManager.put("Label.font", new Font("맑은 고딕", 1, 17));
		JPanel inforPanel = new JPanel(new GridLayout(4, 1, 10, 10));
		inforPanel.setBackground(Color.white);
		
		inforPanel.add(new JLabel("게임명 : " + data.get(Gameinformation.G_NAME.getName())));
		inforPanel.add(new JLabel("카테고리 : " + data.get(Category.CA_NAME.getName())));
		inforPanel.add(new JLabel("포인트 : " + getter.df.format(data.getInt(Gameinformation.G_PRICE.getName())) + "포인트"));
		inforPanel.add(new JLabel("평점 : " + data.getString("a").substring(0, 3) + "점"));
		
		p.add(inforPanel);
		add(p, BorderLayout.NORTH);
		
		p.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				new GameInfor(data.getInt(Gameinformation.G_NO.getName()));
			}
		});
		setFrame("구매한 게임", 550, 450);
	}
	
	public static void main(String[] args) {
		new BuyGameFrom(1);
	}
}
