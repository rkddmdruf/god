package main;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import utils.*;
import utils.sp.*;

public class Brand extends BaseFrame{
	JPanel borderPanel = new cp(new BorderLayout(), sp.em(10, 10,10,10), null);
	JComboBox<String> category = new JComboBox<String>() {{
		addItem("전체");
		for(Row row : Query.selectText("select cname from category")) {
			addItem(row.getString(0));
		}
	}};
	List<Row> list = new ArrayList<>();
	int max = 0; int min = 0;
	JLabel left = new cl("<").font(sp.font(1, 20));
	JLabel right = new cl(">").font(sp.font(1, 20));
	
	JPanel mainPanel = new cp(new GridLayout(0, 3, 10, 15), sp.em(30, 30, 100, 30), null);
	Brand(){
		setFrame("브랜드", 500, 450, ()->{});
	}
	@Override
	protected void desing() {
		borderPanel.add(new cp(new FlowLayout(FlowLayout.LEFT), null, null) {{
			add(category);
		}}, sp.n);
		borderPanel.add(new cp(new FlowLayout(FlowLayout.LEFT), sp.em(150, 0, 0, 0), null) {{ add(left); }}, sp.w);
		borderPanel.add(new cp(new FlowLayout(FlowLayout.LEFT), sp.em(150, 0, 0, 0), null) {{ add(right); }}, sp.e);
		borderPanel.add(mainPanel);
		setCategory();
		setP();
		add(borderPanel);
	}

	@Override
	protected void action() {
		left.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				min = min - 1;
				
				if(min < 0) {
					min = 0;
					left.setEnabled(false);
				}else {
					left.setEnabled(true);
					right.setEnabled(true);
				}
				setP();
			}
		});
		right.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				min = min + 1;
				if(min > max) {
					min =  max; 
					right.setEnabled(false);
				}else {
					left.setEnabled(true);
					right.setEnabled(true);
				}
				setP();
			}
		});
		category.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) setCategory();
		});
	}

	private void setCategory() {
		min = 0; max = 0;
		mainPanel.removeAll();
		int[] cno = {category.getSelectedIndex() == 0 ? 1 : category.getSelectedIndex(),
				category.getSelectedIndex() == 0 ? 10 : category.getSelectedIndex()};
		list = Query.selectText("select * from brand where cno between ? and ?", cno[0], cno[1]);
		max = list.size() / 6;
		setP();
	}
	private void setP() {
		mainPanel.removeAll();
		for(int i = min * 6; i < (((min + 1) * 6) > max * 6 ? list.size() : ((min + 1) * 6)); i++) { final int index = i;
			mainPanel.add(new cl(sp.getImg("datafiles/brand/" + list.get(i).getInt(0) + ".png", 110, 105)) {{
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						System.out.println(list.get(index).get(0));
					}
				});
			}}.setBorders(sp.line));
		}
		if(mainPanel.getComponentCount() <= 3)
			for(int i = 0; i < 3; i++)
				mainPanel.add(new cl(""));
		repaints();
	}
	public static void main(String[] args) {
		new Brand();
	}
}
