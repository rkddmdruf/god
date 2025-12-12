package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import utils.*;
import utils.sp.*;

public class pass extends BaseFrame{
	
	JPanel mainPanel = new cp(new GridLayout(0,1), sp.em(10, 10, 10, 10), null);
	JScrollPane scroll = new JScrollPane(mainPanel) {{
		setBorder(sp.line(sp.color));
		setPreferredSize(new Dimension(0, 475));
	}};
	JPanel borderPanel = new cp(new BorderLayout(10, 10), sp.em(10, 10, 10, 10), null);
	JPanel northPanel = new cp(new FlowLayout(), null ,null);
	JTextField serch = new JTextField() {
		@Override
		public void paintComponent(Graphics g) {
			if(getText().isEmpty()) {
				g.setColor(Color.black);
				g.setFont(sp.font(1, 16));
				g.drawString("검색", 5, 15);
			}
			super.paintComponent(g);
		}
	};
	JLabel serchBut = new cl(sp.getImg("datafiles/icon/search.png", 55,55));
	JComboBox<String> category = new JComboBox<String>() {{
		addItem("전체");
		for(Row row : Query.selectText("select * from category")) addItem(row.getString(1));
	}};
	JComboBox<String> order_by = new JComboBox<String>("기본순, 인기순, 급여높은순".split(", "));
	pass(){
		setFrame("채용", 550, 650, ()->{});
	}
	@Override
	protected void desing() {
		northPanel.add(new cl(sp.getImg("datafiles/icon/cat.png", 100, 55)));
		serch.setPreferredSize(new Dimension(300, 30));
		northPanel.add(serch);
		northPanel.add(serchBut);
		
		borderPanel.add(new cp(new BorderLayout(), null, null) {{
			add(category, sp.w);
			add(order_by, sp.e);
		}});
		 
		setPanel();
		borderPanel.add(scroll, sp.s);
		borderPanel.add(northPanel, sp.n);
		add(borderPanel);
	}

	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	private void setPanel() {
		mainPanel.removeAll();
		List<Row> list = new ArrayList<Row>();
		int[] cno = {category.getSelectedIndex() == 0 ? 0 : category.getSelectedIndex(),
				category.getSelectedIndex() == 0 ? 11 : category.getSelectedIndex()};
		if(order_by.getSelectedIndex() == 0) {
			list = Query.pass_nomal.select(cno[0], cno[1]);
		}else if(order_by.getSelectedIndex() == 1) {
			list = Query.pass_likes.select(cno[0], cno[1]);
		}else {
			list = Query.pass_money.select(cno[0], cno[1]);
		}
		System.out.println(list);
		rp();
	}
	public static void main(String[] args) {
		new pass();
	}
}
