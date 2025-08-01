package main;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;

import utils.Query;
import utils.Row;
import utils.sp;

public class HOME extends JPanel{
	List<JButton> but = new ArrayList<JButton>();
	JComboBox<String> cb = new JComboBox<>() {{
		List<Row> list = Query.category.select();addItem("전체");
		for(int i = 0 ; i < list.size(); i++) {
			addItem(list.get(i).getString(0));
		}
	}};
	int global = 0;int user = 0;
	List<Row> listSave = new ArrayList<Row>();//리스트 110개 다 있는 리스트
	List<Row> allList = new ArrayList<>();//콤보대로 리스트 들어가는데, 반복문으로 10개씩 끊어서 사용
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	JFrame f = null;
	HOME(JFrame f,JPanel p, int user){
		this.f = f;this.user = user;
		this.setLayout(new BorderLayout());
		add(new JPanel(new BorderLayout()) {{
				add(cb, BorderLayout.WEST);setBackground(Color.white);
		}}, BorderLayout.NORTH);
		for(int i = 0; i <= 10; i++) {
			addPanel(cardP,i);
		}
		add(cardP);
		p.add(this, "P1");
		action();
	}
	private void action() {
		cb.addActionListener(e->{
			card.show(cardP, "P"+cb.getSelectedIndex());
		});
		
		
	}
	private void addPanel(JPanel p, int number) {
		if(number == 0) {allList = Query.productCount.select();}
		else {allList = Query.productCountCategory.select(number);}
		p.add(new JPanel(new BorderLayout()) {{ 
			add(new JPanel(new GridLayout(2, 5,15,10)) {{
			setBorder(BorderFactory.createEmptyBorder(10,0, 0, 0));
			setBackground(Color.white);
			for(global = 0; global < 10; global++) {
				listSave.add(allList.get(global));
				List<Row> list = Query.productAll.select(allList.get(global).get(0).toString());
				
				add(new JPanel(new BorderLayout()) {{
					setBackground(Color.white);
					setBorder(sp.line);setBackground(Color.white);
					
					add(new JPanel(new BorderLayout()) {{
						setBackground(Color.white);
						setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
						but.add(new JButton(new ImageIcon(Query.getImge.getImge(allList.get(global).get(0)).getScaledInstance(140, 100, Image.SCALE_SMOOTH))) {{
							this.addActionListener(e->{
								for(int i = 0; i < listSave.size(); i++) {
									if(e.getSource() == but.get(i)) {
										if(user == 1119) {new A_Admin_productChang(listSave.get(i).getInt(0));}
										else if(user != 0 && user != 1119) {new C_Detail(user, listSave.get(i).getInt(0));}
										f.dispose();
									}
								}
							});
						}});
						
						but.get(but.size()-1).setFocusable(false);but.get(but.size()-1).setContentAreaFilled(false);
						add(but.get(but.size()-1), sp.c);
						
						add(new JPanel(new GridLayout(4, 1, 10, 10)) {{
							setBackground(Color.white);
							add(new JLabel("상품명 : " + list.get(0).get(1)));add(new JLabel("가격 : " + new DecimalFormat("###,###").format(list.get(0).get(3))+ "원"));
							add(new JLabel("판매량 : " + list.get(0).getInt(7)));
							if(list.get(0).get(8) != null) {add(new JLabel("평점 : " + list.get(0).get(8).toString().substring(0,3)));
							}else {add(new JLabel("평점 : 0.0"));}
							
						}}, BorderLayout.SOUTH);
					}},BorderLayout.CENTER);
				}});
			}
		}},BorderLayout.CENTER);}}, "P"+number);
	}
}
