package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;

public class Brand extends BaseFrame{
	List<Row> brand;
	int max = 0, min = 0;
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 5, 10, 5), null)
		 , brandPanel  = new sp.cp(new GridLayout(0, 3, 15, 15), sp.em(30, 50, 70, 50), null);// img size 100, 100
	JComboBox<String> cb = new JComboBox<String>() {{
		addItem("전체"); for(Row row : Query.selectText("select cname from category")) addItem(row.getString(0));
	}};
	JLabel left  = new sp.cl("<").font(sp.font(1, 20))
		  ,right = new sp.cl(">").font(sp.font(1, 20));
	Brand(){
		setFrame("브랜드", 500, 400, ()->{});
	}
	@Override
	protected void desing() {
		borderPanel.add(new sp.cp(new FlowLayout(FlowLayout.LEFT), null, null) {{ add(cb); }}, sp.n);
		borderPanel.add(left , sp.w);
		borderPanel.add(right, sp.e);
		setbrandPanels();
		borderPanel.add(brandPanel);
		add(borderPanel);
	}

	@Override
	protected void action() {
		left.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				min = min - 6; max --;
				setbrandPanels();
			}
		});
		right.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				min = min + 6; max ++;
				setbrandPanels();
			}
		});
		cb.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				min = 0; max = 0;
				setbrandPanels();
			}
		});
	}
	public void setbrandPanels() {
		int cbindex = cb.getSelectedIndex();
		int[] cno = {cbindex == 0 ? 1  : cbindex
				,	 cbindex == 0 ? 10 : cbindex};
		brand = Query.selectText("SELECT * FROM parttimecat.brand where cno between ? and ?;",cno[0], cno[1]);
		if((max + 1) * 6 >= brand.size()) {
			right.setEnabled(false);
			return;
		}
		if(min < 0 || max < 0) {
			min = 0; max = 0;
			left.setEnabled(false);
			return;
		}
		right.setEnabled(true);
		left.setEnabled(true);
		brandPanel.removeAll();
		for(int i = min; i < (brand.size() < (max + 1) * 6 ? brand.size() : (max + 1) * 6); i++) { final int index = i;
			brandPanel.add(new sp.cl(sp.getImg("datafiles/brand/" + brand.get(i).getInt(0) + ".png", 100, 100)) {{
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						System.out.println(brand.get(index).get(1));
					}
				});
			}}.setBorders(sp.line));
			System.out.println(i);
		}
		rp();
	}
	public static void main(String[] args) {
		new Brand();
	}
}
