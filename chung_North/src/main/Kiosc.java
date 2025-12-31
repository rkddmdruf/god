package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class Kiosc extends BaseFrame{

	JPanel mainPanel = new cp(new BorderLayout(5,5), null, null);
	JPanel foodPanel = new cp(new GridLayout(0, 3, 0, 0), null, null);
	List<Row> food = Query.select("select * from food");
	List<JLabel> foodLabel = new ArrayList<>();
	JComboBox<String> order = new JComboBox<String>("전체,세트O,세트X".split(","));
	Kiosc(){
		setFrame("키오스크", 425 + 16, 677 + 39, ()->{});
	}
	@Override
	protected void desing() {
		System.out.println(food.get(0));
		mainPanel.add(new cl(sp.getImg("datafiles/로고2.jpg", 425, 160)) {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setFont(sp.font(1, 26));
				g.setColor(Color.black);
				g.drawString("키오스크", 160, 100);
			}
		}, sp.n);
		setPanel();
		setAction();
		mainPanel.add(new JScrollPane(foodPanel));
		add(mainPanel);
		add(new cp(new BorderLayout(), null ,null) {{ add(order, sp.e); }}, sp.s);
	}
	
	private void setPanel() {
		foodLabel.clear();
		foodPanel.removeAll();
		food = Query.select("select * from food");
		if(order.getSelectedIndex() != 0) {
			food = Query.select("select * from food where f_set = ? order by f_no", order.getSelectedIndex() == 1 ? 1 : 0);
		}
		for(int i = 0; i < food.size(); i++) {final int index = i;
			foodPanel.add(new cp(new BorderLayout(), null ,null) {{
				JLabel l = new cl(sp.getImg("datafiles/foods/" + food.get(index).getInt(0) + ".jpg", 90, 110));
				foodLabel.add(l);
				add(foodLabel.get(foodLabel.size() - 1));
				add(new cp(new GridLayout(3,1), null, null) {{
					add(new cl(food.get(index).getString(1)).setHo(JLabel.CENTER).font(sp.font(1, 12)) , sp.n);
					add(new cl(food.get(index).getString(3)).setHo(JLabel.CENTER).font(sp.font(1, 12)) );
					add(new cl(food.get(index).getString(2)).setHo(JLabel.CENTER).font(sp.font(1, 12)) , sp.s);
				}}, sp.s);
			}});
		}
		if(food.size() < 9) {
			for(int i = 0; i < 9-food.size(); i++) {
				foodPanel.add(new cl(""));
			}
		}
		RePaint();
	}
	@Override
	protected void action() {
		order.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				setPanel();
				setAction();
			}
		});
	}
	
	private void setAction() {
		for(int i = 0; i < food.size(); i++) {final int index = i;
			foodLabel.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new MenuInfor(food.get(index).getInt(0));
					dispose();
				}
			});
		}
	}
	public static void main(String[] args) {
		new Kiosc();
	}
}
