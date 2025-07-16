package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Query;
import utils.Row;

public class HOME extends JPanel{
	JComboBox<String> cb = new JComboBox<>() {{
		List<Row> list = Query.category.select();addItem("전체");
		for(int i = 0 ; i < list.size(); i++) {
			addItem(list.get(i).getString(0));
		}
	}};
	int global = 0;
	List<ArrayList<Integer>> allList=  new ArrayList<>() {{
		List<ArrayList<Integer>> list = new ArrayList<>();
		for(int i = 0; i < Query.productCount.select().get(0).getInt(0); i++) {
			ArrayList<Integer> row = new ArrayList<>();
			row.add(i);
			row.add(Query.product_gu_moon_count.select(i+"").get(0).getInt(0));
			list.add(row);
		}
		Collections.sort(list, Comparator.comparingInt((List<Integer> o) -> o.get(1)).reversed());
		for(int i = 0; i < 10; i++) {
			this.add(list.get(i));
		}
	}};
	HOME(JPanel p){
		System.out.println(allList);
		this.setLayout(new BorderLayout());
		add(new JPanel(new BorderLayout()) {{
				add(cb, BorderLayout.WEST);setBackground(Color.white);
			}}, BorderLayout.NORTH);
		
		add(new JPanel(new GridLayout(2, 5,15,10)) {{
				setBorder(BorderFactory.createEmptyBorder(10,0, 0, 0));
				setBackground(Color.white);
				for(global = 0; global < 10; global++) {
					List<Row> list = Query.productAll.select(allList.get(global).get(0).toString());
					List<Row> review = Query.reviewAvg.select(allList.get(global).get(0).toString());
					
					add(new JPanel(new BorderLayout()) {{
						setBackground(Color.white);
						setBorder(BorderFactory.createLineBorder(Color.black));setBackground(Color.white);
						
						add(new JPanel(new BorderLayout()) {{
							setBackground(Color.white);
							setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
							
							add(new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/img/" + allList.get(global).get(0) + ".jpg"))
									.getImage().getScaledInstance(140, 100, Image.SCALE_SMOOTH))) {{setFocusable(false);
									setContentAreaFilled(false);
							}}, BorderLayout.CENTER);
							
							add(new JPanel(new GridLayout(4, 1, 10, 10)) {{
								setBackground(Color.white);
								add(new JLabel("상품명 : " + list.get(0).get(1)));add(new JLabel("가격 : " + new DecimalFormat("###,###").format(list.get(0).get(3))+ "원"));
								add(new JLabel("판매량 : "));
								if(review.get(0).get(0) != null) {add(new JLabel("평점 : " + review.get(0).get(0).toString().substring(0,3)));
								}else {add(new JLabel("평점 : 0.0"));}
								
							}}, BorderLayout.SOUTH);
						}},BorderLayout.CENTER);
					}});
				}
			}},BorderLayout.CENTER);
		p.add(this, "P1");
	}
}
