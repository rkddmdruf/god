package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import utils.Query;
import utils.Row;

public class Serch extends JPanel{
	
	int global = 0;
	String[] str = "상품명,가격".split(",");
	String[] categoryName = "전체,식품,문구,생활용품,뷰티,음료,전자제품,의류,유아,스포츠,도서".split(",");
	
	
	JComboBox<String> cb = new JComboBox<String>(str) {{setPreferredSize(new Dimension(100, 30));}};
	JTextField product = new JTextField() {{setPreferredSize(new Dimension(650, 30));}};
	JTextField[] price = {new JTextField() {{setPreferredSize(new Dimension(320, 30));}}, new JTextField() {{setPreferredSize(new Dimension(320, 30));}}};
	JLabel 물결 = new JLabel("~");
	
	static JButton[] category = new JButton[11];
	List<JButton> butA = new ArrayList<JButton>();
	List<Row> list = Query.productANDreview.select();
	JButton[] butB = {new JButton("검색"), new JButton("↑"), new JButton("↓")};
	
	static CardLayout card = new CardLayout();
	static JPanel cardP = new JPanel(card);
	Serch(){}
	Serch(JPanel p, CardLayout cardHome, int pomNumber){
		for(global = 0; global < 11; global++) {
			category[global] = new JButton(categoryName[global]);
			category[global].setBackground(Color.LIGHT_GRAY);
			category[global].setFont(setBoldFont(20));
			category[global].setBorderPainted(false);category[global].setFont(setBoldFont(20));
			category[global].addActionListener(e->{
				for(int i = 0 ; i < 11;i++) {
					if(category[i].getBackground() == Color.white) {
						category[i].setBackground(Color.LIGHT_GRAY);
						category[i].setForeground(Color.black);
					}
					if(e.getSource() == category[i]) {
						category[i].setBackground(Color.white);category[i].setForeground(Color.blue);
						card.show(cardP, "P"+i);
					}
				}
			});
		}
		add(new JPanel(new BorderLayout()) {{
			setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
			add(new JPanel(new FlowLayout()) {{
				add(cb);
				add(product);
				for(int i = 0; i < 3; i++) {
					add(butB[i]);
				}
				cb.addActionListener(e->{
					for(int i = 0; i < 3; i++) {remove(butB[i]);}
					if(cb.getSelectedItem().equals("가격")) {
						remove(product);add(price[0]);add(물결);add((price[1]));
					}else if(cb.getSelectedItem().equals("상품명")) {
						for(int i = 0; i < 2; i++) {remove(price[i]);}remove(물결);add(product);
					}
					for(int i = 0; i < 3; i++) {add(butB[i]);}
					revalidate();
					repaint();
				});
			}}, BorderLayout.NORTH);
			List<Row> list = Query.category.select();
			add(new JPanel(new GridLayout(11,1)) {{
				setBackground(Color.LIGHT_GRAY);
				for(int i = 0; i < 11; i++) {
					
					add(category[i]);
				}
			}}, BorderLayout.WEST);
			
			All();
			
			for(int i = 1 ; i <= 10;i++) {food(i);}
			add(cardP);
			card.show(cardP, "P0");
		}},BorderLayout.CENTER);
		p.add(this, "P2");
		card.show(cardP, "P"+pomNumber);
		for(int i = 0; i < 11; i++) {
			category[i].setBackground(Color.LIGHT_GRAY);category[i].setForeground(Color.black);
			if(i == pomNumber) {category[i].setBackground(Color.white);category[i].setForeground(Color.blue);}
		}
		category[0].setBackground(Color.white);category[0].setForeground(Color.blue);
	}
	
	
	public static void ALLShow(String string) {
		card.show(cardP, string);
		for(int i = 0; i < 11; i++) {
			category[i].setBackground(Color.LIGHT_GRAY);
			category[i].setForeground(Color.black);
			if(i == 0) {category[i].setBackground(Color.white);category[i].setForeground(Color.blue);}
		}
	}
	private Font setBoldFont(int i) {
		return new Font("맑은 고딕", Font.BOLD, i);
	}
	
	private void All() {
		JPanel Main = new JPanel(new GridLayout(0, 4 ,20, 10)) {{
			setBorder(BorderFactory.createEmptyBorder(0,20,0,10));
			
			for(global = 1; global <= list.size(); global++) {
				add(new JPanel(new BorderLayout()) {{
					setPreferredSize(new Dimension(100,250));setBorder(BorderFactory.createLineBorder(Color.black));
					add(new JPanel(new BorderLayout()) {{
						setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
						butA.add(new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/img/"+global+".jpg")).getImage().getScaledInstance(140, 120, Image.SCALE_SMOOTH))));
						butA.get(global-1).setBackground(Color.white);
						butA.get(global-1).setBorder(BorderFactory.createLineBorder(Color.black));
						add(butA.get(global-1), BorderLayout.NORTH);
						
						add(new JPanel(new GridLayout(4,1, 0, 10)) {{
							add(new JLabel("상품명 : " + list.get(global-1).getString(1)));
							add(new JLabel("가격 : " + new DecimalFormat("###,###").format(list.get(global-1).getInt(3))+ "원"));
							add(new JLabel("판매량 : "));
							if(list.get(global-1).get(7) == null) {
								add(new JLabel("평점 : 0.0"));
							}else {
								add(new JLabel("평점 : " + list.get(global-1).getString(7).toString().substring(0,3)));
							}
						}}, BorderLayout.CENTER);
					}});
				}});
			}
		}};
		JScrollPane sc = new JScrollPane(Main);
		sc.setPreferredSize(new Dimension(800, 450));
		sc.getVerticalScrollBar().setUnitIncrement(20);
		cardP.add(sc, "P0");
	}
	private void food(int number) {
		JPanel Main = new JPanel(new GridLayout(0, 4 ,20, 10)) {{
			setBorder(BorderFactory.createEmptyBorder(0,20,0,10));
			List<Row> list = Query.productANDreviewWCNO.select(number+"");
			//System.out.println("START : " + list.get(0).getInt(0) + ",  END : " + list.get(list.size()-1).getInt(0));
			for(global = list.get(0).getInt(0); global <= list.get(list.size()-1).getInt(0); global++) {
				add(new JPanel(new BorderLayout()) {{
					setPreferredSize(new Dimension(100,250));setBorder(BorderFactory.createLineBorder(Color.black));
					add(new JPanel(new BorderLayout()) {{
						setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
						add(new JButton() {{
							setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
							setBorder(BorderFactory.createLineBorder(Color.black));
							setBackground(Color.white);
							setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/img/"+global+".jpg")).getImage().getScaledInstance(140, 120, Image.SCALE_SMOOTH)));
						}}, BorderLayout.NORTH);
						add(new JPanel(new GridLayout(4,1, 0, 10)) {{
							add(new JLabel("상품명 : " + list.get(global - list.get(0).getInt(0)).getString(1)));
							add(new JLabel("가격 : " + new DecimalFormat("###,###").format(list.get(global - list.get(0).getInt(0)).getInt(3))+ "원"));
							add(new JLabel("판매량 : "));
							if(list.get(global - list.get(0).getInt(0)).get(7) == null) {
								add(new JLabel("평점 : 0.0"));
							}else {
								add(new JLabel("평점 : " + list.get(global - list.get(0).getInt(0)).getString(7).toString().substring(0,3)));
							}
						}}, BorderLayout.CENTER);
					}});
				}});
			}
		}};
		JScrollPane sc = new JScrollPane(Main);
		sc.setPreferredSize(new Dimension(800, 450));
		sc.getVerticalScrollBar().setUnitIncrement(20);
		cardP.add(sc, "P" + number);
	}
}
