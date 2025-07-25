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
	
	int global = 0;int test = 0;int globali = 0;
	String[] categoryName = "전체,식품,문구,생활용품,뷰티,음료,전자제품,의류,유아,스포츠,도서".split(",");
	JButton[] category = new JButton[11];
	
	JScrollPane scMain = new JScrollPane();
	JScrollPane[] scCate = new JScrollPane[10];
	
	JComboBox<String> cb = new JComboBox<String>("상품명,가격".split(",")) {{setPreferredSize(new Dimension(100, 30));}};
	JTextField product = new JTextField() {{setPreferredSize(new Dimension(650, 30));}};
	JTextField[] price = {new JTextField() {{setPreferredSize(new Dimension(320, 30));}}, new JTextField() {{setPreferredSize(new Dimension(320, 30));}}};
	JLabel 물결 = new JLabel("~");
	JButton[] butB = {new JButton("검색"), new JButton("↑"), new JButton("↓")};
	
	List<JButton> butS = new ArrayList<JButton>();
	List<JButton> butA = new ArrayList<JButton>();// 전체
	List<JButton> butC = new ArrayList<JButton>();// 카테고리
	List<Row> list = Query.productANDreview.select();// ALL 전체에서만 쓴다 지워도 될듯?
	
	List<Row> actionList = new ArrayList<Row>();// 카테고리 리스트들 담는거
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	DecimalFormat DF = new DecimalFormat("###,###");
	Serch(){}
	
	Serch(JPanel p, CardLayout cardHome, int pomNumber){
		for(global = 0; global < 11; global++) {
			category[global] = new JButton(categoryName[global]);
			category[global].setBackground(Color.LIGHT_GRAY);
			category[global].setFont(setBoldFont(20));
			category[global].setBorderPainted(false);category[global].setFont(setBoldFont(20));
			category[global].addActionListener(e->{
				scMain.getVerticalScrollBar().setValue(0);
				for(int i = 0 ; i < 11;i++) {
					if(i >=0 && i <=9)
						scCate[i].getVerticalScrollBar().setValue(0);/// 버튼 누르면 스크롤 가장위로 가게
					
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
		}//// 카테고리 버튼
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
			}}, BorderLayout.NORTH);/// 위에 콤보 텍스트 필드 등
			List<Row> list = Query.category.select();
			add(new JPanel(new GridLayout(11,1)) {{
				setBackground(Color.LIGHT_GRAY);
				for(int i = 0; i < 11; i++) {
					add(category[i]);
				}
			}}, BorderLayout.WEST);///이거 버튼 추가
			
			All();
			
			category();
			add(cardP);
			card.show(cardP, "P0");
		}},BorderLayout.CENTER);
		p.add(this, "P2");
		card.show(cardP, "P"+pomNumber);
		for(int i = 0; i < 11; i++) {
			category[i].setBackground(Color.LIGHT_GRAY);category[i].setForeground(Color.black);
			if(i == pomNumber) {category[i].setBackground(Color.white);category[i].setForeground(Color.blue);}
		}//로그인 했을떄 YES일 경우 그 카테고리 선택
	}
	
	
	public void ALLShow(String string) {
		card.show(cardP, string);
		for(int i = 0; i < 11; i++) {
			category[i].setBackground(Color.LIGHT_GRAY);
			category[i].setForeground(Color.black);
			if(i == 0) {category[i].setBackground(Color.white);category[i].setForeground(Color.blue);}
		}
		scMain.getVerticalScrollBar().setValue(0);
	}
	private Font setBoldFont(int i) {
		return new Font("맑은 고딕", Font.BOLD, i);
	}
	
	
	private void All() {
		butA.clear();actionList.clear();
		for(globali = 1; globali <= 10; globali++) {
			List<Row> list = Query.productANDreviewWCNO.select(globali);
			actionList.addAll(list);
		}
			JPanel Main = new JPanel(new GridLayout(0, 4 ,20, 10)) {{
				setBorder(BorderFactory.createEmptyBorder(0,20,0,10));
					for(global = 1; global <= actionList.size();global++) {//이거 list 보는거 리스트는 처음부터 있음
						panelAdd(this, butA);
					}
					scMain = new JScrollPane(this);
					scMain.setPreferredSize(new Dimension(800, 450));
					scMain.getVerticalScrollBar().setUnitIncrement(20);
					cardP.add(scMain, "P" + 0);
			}};
			actionList.clear();
	}
	private void category() {
		butC.clear();actionList.clear();
		for(globali = 1; globali <= 10; globali++) {
		JPanel Main = new JPanel(new GridLayout(0, 4 ,20, 10)) {{
			setBorder(BorderFactory.createEmptyBorder(0,20,0,10));
			int t = 0;
				List<Row> list = Query.productANDreviewWCNO.select(globali+"");
				actionList.addAll(list);
				for(global = 1+test; global <= list.size()+test;global++) {//이거 list 보는거 리스트는 처음부터 있음
					panelAdd(this, butC);
				}
				test += list.size();
				scCate[globali-1] = new JScrollPane(this);
				scCate[globali-1].setPreferredSize(new Dimension(800, 450));
				scCate[globali-1].getVerticalScrollBar().setUnitIncrement(20);
				cardP.add(scCate[globali-1], "P" + globali);
		}};
		}
	}
	
	public void panelAdd(JPanel p, List<JButton> buts) {
		p.add(new JPanel(new BorderLayout()) {{
			setPreferredSize(new Dimension(100,250));setBorder(BorderFactory.createLineBorder(Color.black));
			add(new JPanel(new BorderLayout()) {{
				setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
				
				buts.add(new JButton(new ImageIcon(Query.getImge.getImge(actionList.get(global-1).getInt(0)).getScaledInstance(140, 120, Image.SCALE_SMOOTH))));
				buts.get(global-1).setBackground(Color.white); buts.get(global-1).setBorder(BorderFactory.createLineBorder(Color.black));
				add(buts.get(global-1), BorderLayout.NORTH);
				add(new JPanel(new GridLayout(4,1, 0, 10)) {{
					add(new JLabel("상품명 : " + actionList.get(global-1).getString(1)));
					add(new JLabel("가격 : " + DF.format(actionList.get(global-1).getInt(3)) + "원"));
					add(new JLabel("판매량 : "));
					if(actionList.get(global-1).get(7) == null) {
						add(new JLabel("평점 : 0.0"));
					}else {
						add(new JLabel("평점 : " + actionList.get(global-1).get(7).toString().substring(0,3)));
					}
				}}, BorderLayout.CENTER);
			}});
		}});
	}
}
