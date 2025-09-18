package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import utils.*;

public class Brand extends BaseFrame{
	List<Row> CTlist = Query.category.select();
	List<Row> brandList = Query.Brand.select(1, 10);
	JComboBox<String> category = new JComboBox<String>() {{
		addItem("전체");
		for(Row row : CTlist) addItem(row.getString(1));
	}};
	JPanel centerPanel = new JPanel(new GridLayout(0, 3, 20,20)) {{
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(30,20,50, 20));
	}};
	JButton[] point = new JButton[2];
	int curser = 0;
	Brand(){
		setFrame("브랜드", 500,  400, ()->{});
	}
	
	@Override
	protected void desing() {
		for(int i = 0; i < 2; i++) {
			point[i] = sp.Cb(point[0] == null ? "<" : ">");
			point[i].setFont(sp.font(0,  20));
		}
		add(new JPanel(new FlowLayout(FlowLayout.LEFT)) {{
			setBackground(Color.white);
			setPreferredSize(new Dimension(0, 40));
			setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			add(category);
			
		}}, sp.n);
		add(centerPanel, sp.c);
		add(point[0], sp.w);
		add(point[1], sp.e);
		setCurser();
	}
	
	@Override
	protected void action() {
		for(int i = 0; i < 2; i++) {final int index = i;
			point[i].addActionListener(e->{
				curser += index == 0 ? -1 : 1;
				category.setSelectedIndex(curser);
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
		System.out.println(brandList);
		for(int i = 0; i < (brandList.size() > 6 ? 6 : brandList.size()); i++) {final int index = i;
			centerPanel.add(new JPanel(new BorderLayout()) {{
				JLabel label = new JLabel(new ImageIcon(new ImageIcon("src/brand/" + brandList.get(index).getInt(0) + ".png")
						.getImage().getScaledInstance(100, 110, Image.SCALE_SMOOTH)));
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
