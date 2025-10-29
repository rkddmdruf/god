package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import utils.*;
import utils.sp.cl;

public class Brand extends BaseFrame{
	List<Row> categoryList = Query.category.select();
	List<Row> brandList = Query.Brand.select(1, 10);
	JComboBox<String> category = new JComboBox<String>() {{
		addItem("전체");
		for(Row row : categoryList) addItem(row.getString(1));
	}};
	JPanel centerPanel = new sp.cp(new GridLayout(0, 3, 20,20), sp.em(30, 20, 50, 20), null);
	JLabel[] point = new JLabel[2];
	int curser = 0;
	Brand(){
		setFrame("브랜드", 500,  400, ()->{new Main();});
	}
	
	@Override
	protected void desing() {
		for(int i = 0; i < 2; i++) {
			point[i] = new sp.cl(point[0] == null ? "<" : ">").font(sp.font(0, 30)).size(50, 50).setHo((i+1) * 2);
		}
		add(new sp.cp(new FlowLayout(FlowLayout.LEFT), sp.em(10, 10, 10, 10), null) {{ add(category); }}.size(0, 40), sp.n);
		add(centerPanel, sp.c);
		add(point[0], sp.w);
		add(point[1], sp.e);
		setCurser();
	}
	
	@Override
	protected void action() {
		
		for(int i = 0; i < 2; i++) {final int index = i;
			point[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					curser += index == 0 ? -1 : 1;
					if(curser < 0) curser = 0;
					if(curser > 10) curser = 10;
					category.setSelectedIndex(curser);
					setCurser();
				}
			});
		}
		category.addActionListener(e->{
			curser = category.getSelectedIndex();
			setCurser();
		});
	}
	
	public void setCurser() {
		point[0].setEnabled(curser == 0 ? false : true);
		point[1].setEnabled(curser == 10 ? false : true);
		setcenterPanel();
	}
	
	public void setcenterPanel() {
		brandList.clear();
		centerPanel.removeAll();
		int GSI = category.getSelectedIndex();
		int[] bno = {GSI == 0 ? 1 : GSI, GSI == 0 ? 10 : GSI};
		brandList = Query.Brand.select(bno[0], bno[1]);
		for(int i = 0; i < (brandList.size() > 6 ? 6 : brandList.size()); i++) {final int index = i;
			centerPanel.add(new JPanel(new BorderLayout()) {{
				JLabel label = new sp.cl(sp.getImg("brand/" + brandList.get(index).getInt(0) + ".png", 100, 110));
				label.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						new BrandInf(brandList.get(index).getInt(0));
						dispose();
					}
				});
				add(label);
				setBorder(sp.line);
			}});
		}
		
		RePaint();
	}
	public static void main(String[] args) {
		new Brand();
	}
}
