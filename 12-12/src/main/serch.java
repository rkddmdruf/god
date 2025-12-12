package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class serch extends BaseFrame{
	List<Row> list = new ArrayList<>();
	List<JLabel> categoryLabel = new ArrayList<>();
	JPanel eastPanel = new cp(new BorderLayout(), sp.em(0, 10, 20, 10), null) {{
		JPanel category = new cp(new BorderLayout(), null, null);
		category.add(new cl("직종 카테고리").setHo(JLabel.CENTER).setBorders(sp.line).size(180, 25));
		JPanel grid = new cp(new GridLayout(0,1), sp.em(-5, 0, 50, 0), null);
		categoryLabel.add(new cl("전체").setBorders(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.black)));
		grid.add(categoryLabel.get(0));
		for(Row row : Query.selectText("select * from category")) {
			categoryLabel.add(new cl(row.getString(1)).setBorders(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black)).font(sp.font(0, 14)));
			grid.add(categoryLabel.get(categoryLabel.size()-1));
		}
		add(category, sp.n);
		add(grid);
		add(new cl(sp.getImg("datafiles/icon/cat.png", 180, 100)), sp.s);
	}};
	JPanel main = new cp(new BorderLayout(), null, null);
	boolean[] category = new boolean[11];
	serch(){
		setFrame("찾기", 816, 589, ()->{});
	}
	@Override
	protected void desing() {
		for(JLabel l : categoryLabel) l.setOpaque(true);
		category[0] = true;
		setCategory();
		main.add(new points() {{
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					for(Row row : list) {
						if(x > row.getInt(3) && x < row.getInt(3) + 10) {
							if(y > row.getInt(4) && y < row.getInt(4) + 10) {
								if(e.getClickCount() == 2) {
									new brandInfor(row.getInt(0));
								}
							}
						}
					}
				}
			});
		}}, sp.w);
		
		add(main);
		add(eastPanel, sp.e);
	}

	@Override
	protected void action() {
		categoryLabel.get(0).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				category[0] = !category[0];
				for(int i = 1; i < 11; i++) category[i] = false;
				setCategory();
			}
		});
		for(int i = 1; i < 11; i++) {final int index = i;
			categoryLabel.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					category[index] = !category[index];
					if(category[0]) category[0] = false;
					setCategory();
				}
			});
		}
	}
	
	private void setCategory() {
		System.out.println(list);
		for(int i = 0; i < 11; i++) {
			categoryLabel.get(i).setBackground(Color.white);
			categoryLabel.get(i).setForeground(Color.black);
			if(category[i]) {
				categoryLabel.get(i).setBackground(Color.red);
				categoryLabel.get(i).setForeground(Color.white);
			}
		}
		rp();
	}
	class points extends JPanel{
		points(){
			this.setPreferredSize(new Dimension(600, 550));
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(sp.getImg("datafiles/지도.png", 600, 550).getImage(), 0, 0, 600, 550, null);
			String s = "";
			for(int i = 0;i < 11; i++)
				if(category[0] || category[i])
					s = s + i +", ";
			if(!s.isEmpty()) {
				String string = "select * from brand where cno IN (" + s.substring(0, s.length()-2) + ");";
				list = Query.selectText(string);
				for(int i = 0; i < list.size(); i++) {
					g.setColor(Color.red);
					g.fillOval(list.get(i).getInt(3), list.get(i).getInt(4), 10, 10);
				}
			}
		}
	}
	public static void main(String[] args) {
		new serch();
	}
}
