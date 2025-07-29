package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import utils.sp;

public class HOME extends JPanel{
	List<JButton> butA = new ArrayList<JButton>();
	List<JButton> butC = new ArrayList<JButton>();
	JComboBox<String> cb = new JComboBox<>() {{
		List<Row> list = Query.category.select();addItem("전체");
		for(int i = 0 ; i < list.size(); i++) {
			addItem(list.get(i).getString(0));
		}
	}};
	int global = 0;int user = 0;
	List<ArrayList<Integer>> allList = new ArrayList<>() {{
		List<Row> list = Query.productCount.select();
		for(global = 0; global < 10; global++) {
			this.add(new ArrayList<Integer>() {{
				this.add(list.get(global).getInt(0));
				this.add(list.get(global).getInt(1));
			}});
		}
	}};
	List<Row>[] Catelist = new ArrayList[10];
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	HOME(){};
	HOME(JPanel p, int user){
		this.user = user;
		this.setLayout(new BorderLayout());
		add(new JPanel(new BorderLayout()) {{
				add(cb, BorderLayout.WEST);setBackground(Color.white);
			}}, BorderLayout.NORTH);
		
		JPanel ALLP = new JPanel(new BorderLayout());
		ALLP.add(new JPanel(new GridLayout(2, 5,15,10)) {{
				setBorder(BorderFactory.createEmptyBorder(10,0, 0, 0));
				setBackground(Color.white);
				for(global = 0; global < 10; global++) {
					List<Row> list = Query.productAll.select(allList.get(global).get(0).toString());
					List<Row> review = Query.reviewAvg.select(allList.get(global).get(0).toString());
					System.out.println(list);
					add(new JPanel(new BorderLayout()) {{
						setBackground(Color.white);
						setBorder(BorderFactory.createLineBorder(Color.black));setBackground(Color.white);
						
						add(new JPanel(new BorderLayout()) {{
							setBackground(Color.white);
							setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
							
							butA.add(new JButton(new ImageIcon(Query.getImge.getImge(allList.get(global).get(0)).getScaledInstance(140, 100, Image.SCALE_SMOOTH))));
							butA.get(global).setFocusable(false);butA.get(global).setContentAreaFilled(false);
							
							add(butA.get(global), BorderLayout.CENTER);
							
							
							add(new JPanel(new GridLayout(4, 1, 10, 10)) {{
								setBackground(Color.white);
								add(new JLabel("상품명 : " + list.get(0).get(1)));add(new JLabel("가격 : " + new DecimalFormat("###,###").format(list.get(0).get(3))+ "원"));
								add(new JLabel("판매량 : " + list.get(0).getInt(7)));
								if(review.get(0).get(0) != null) {add(new JLabel("평점 : " + review.get(0).get(0).toString().substring(0,3)));
								}else {add(new JLabel("평점 : 0.0"));}
								
							}}, BorderLayout.SOUTH);
						}},BorderLayout.CENTER);
					}});
				}
			}},BorderLayout.CENTER);
		cardP.add(ALLP, "P0");
		for(int i = 0; i < 10; i++) {
			category(i);
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
	private void category(int number) {
		Catelist[number] = Query.productCountCategory.select((number+1)+"");
		System.out.println(Catelist[number]);
		cardP.add(new JPanel(new BorderLayout()) {{ 
			add(new JPanel(new GridLayout(2, 5,15,10)) {{
			setBorder(BorderFactory.createEmptyBorder(10,0, 0, 0));
			setBackground(Color.white);
			for(global = 0; global < 10; global++) {
				List<Row> list = Query.productAll.select(Catelist[number].get(global).get(0).toString());
				List<Row> review = Query.reviewAvg.select(Catelist[number].get(global).get(0).toString());
				
				add(new JPanel(new BorderLayout()) {{
					setBackground(Color.white);
					setBorder(sp.line);setBackground(Color.white);
					
					add(new JPanel(new BorderLayout()) {{
						setBackground(Color.white);
						setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
						Query.getImge.getImge(Catelist[number].get(global).get(0)).getScaledInstance(140, 100, Image.SCALE_SMOOTH);
						butC.add(new JButton(new ImageIcon(Query.getImge.getImge(Catelist[number].get(global).get(0)).getScaledInstance(140, 100, Image.SCALE_SMOOTH))));
						butC.get(butC.size()-1).setFocusable(false);butC.get(butC.size()-1).setContentAreaFilled(false);
						add(butC.get(butC.size()-1), BorderLayout.CENTER);
						
						add(new JPanel(new GridLayout(4, 1, 10, 10)) {{
							setBackground(Color.white);
							add(new JLabel("상품명 : " + list.get(0).get(1)));add(new JLabel("가격 : " + new DecimalFormat("###,###").format(list.get(0).get(3))+ "원"));
							add(new JLabel("판매량 : " + list.get(0).getInt(7)));
							if(review.get(0).get(0) != null) {add(new JLabel("평점 : " + review.get(0).get(0).toString().substring(0,3)));
							}else {add(new JLabel("평점 : 0.0"));}
							
						}}, BorderLayout.SOUTH);
					}},BorderLayout.CENTER);
				}});
			}
		}},BorderLayout.CENTER);}}, "P"+(number+1));
	}
}
