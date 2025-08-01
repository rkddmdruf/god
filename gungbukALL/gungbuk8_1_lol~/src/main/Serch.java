package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import utils.Query;
import utils.Row;
import utils.sp;

public class Serch extends JPanel{
	
	int global = 0, test = 0, globali = 0, user = 0;JFrame f = null;
	JButton[] category = sp.setButton("전체,식품,문구,생활용품,뷰티,음료,전자제품,의류,유아,스포츠,도서".split(","));
	JLabel 물결 = new JLabel("~");
	JButton[] butB = sp.setButton("전체,↑,↓".split(","));
	
	JScrollPane[] scMain = new JScrollPane[11];
	
	JComboBox<String> cb = new JComboBox<String>("상품명,가격".split(",")) {{setPreferredSize(new Dimension(100, 30));}};
	JTextField product = new JTextField() {{setPreferredSize(new Dimension(650, 30));}};
	JTextField[] price = {new JTextField() {{setPreferredSize(new Dimension(316, 30));}}, new JTextField() {{setPreferredSize(new Dimension(316, 30));}}};
	
	List<JButton> butA = new ArrayList<JButton>(), butC = new ArrayList<JButton>();
				//^^전체^^							//카테고리^^^
	List<Row> actionList = new ArrayList<Row>(), categoryList = new ArrayList<Row>();
	CardLayout card = new CardLayout();JPanel cardP = new JPanel(card);
	DecimalFormat DF = new DecimalFormat("###,###");
	
	Serch(){}
	
	Serch(JFrame f,JPanel p,int user, int pomNumber){
		this.f = f;this.user = user;
		for(global = 0; global < 11; global++) {
			category[global].setBackground(Color.LIGHT_GRAY);category[global].setFont(sp.fontM(1,20));
			category[global].setBorderPainted(false);
			category[global].addActionListener(e->{
				for(int i = 0 ; i < 11;i++) {
					scMain[i].getVerticalScrollBar().setValue(0);// 스크롤 위로 가게 하기
					if(e.getSource() == category[i]) {category[i].setBackground(Color.white);category[i].setForeground(Color.blue);card.show(cardP, "P"+i);
					}else {category[i].setBackground(Color.LIGHT_GRAY);category[i].setForeground(Color.black);}
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
			}}, sp.n);/// 위에 콤보 텍스트 필드 등
			
			add(new JPanel(new GridLayout(11,1)) {{setBackground(Color.LIGHT_GRAY);for(int i = 0; i < 11; i++) {add(category[i]);}}}, sp.c);///이거 버튼 추가
			for(int in = 1; in <= 10; in++) {
				List<Row> list = Query.productANDreviewWCNO.select(in);
				actionList.addAll(list);
			}
			;//category();
			for(int i = 0; i < 11; i++) {
				All(i);
			}
			
			add(cardP, sp.e);
			card.show(cardP, "P0");
		}},BorderLayout.CENTER);
		p.add(this, "P2");
		card.show(cardP, "P"+pomNumber);
		for(int i = 0; i < 11; i++) {
			category[i].setBackground(Color.LIGHT_GRAY);category[i].setForeground(Color.black);
			if(i == pomNumber) {category[i].setBackground(Color.white);category[i].setForeground(Color.blue);
				//card.show(cardP, "P" + pomNumber);
			}
		}//로그인 했을떄 YES일 경우 그 카테고리 선택
	}
	
	
	public void ALLShow(String string) {
		card.show(cardP, string);
		for(int i = 0; i < 11; i++) {
			category[i].setBackground(Color.LIGHT_GRAY);category[i].setForeground(Color.black);
			if(i == 0) {category[i].setBackground(Color.white);category[i].setForeground(Color.blue);}
		}
		scMain[0].getVerticalScrollBar().setValue(0);
	}
	
	public void All(int category) {
		global = 0;
		JPanel Main = new JPanel(new GridLayout(0, 4 ,20, 10)) {{
			setBorder(BorderFactory.createEmptyBorder(0,20,0,10));
			if(category == 0) {
				for(global = 1; global <= actionList.size();global++) {//이거 list 보는거 리스트는 처음부터 있음
					panelAdd(this, butA, global);
				}
			}else {
				categoryList = Query.productANDreviewWCNO.select(category);//카테고리 파라미터가 오면 그거에 맞는 거 리스트 담기
				for(global = 1+test; global <= categoryList.size()+test; global++) {//test는 이전에 리스트의 크기
					panelAdd(this, butC, global);//test는 global 변수를 1~20, 21~40 이런식으로 만들어준다
				}
				test += categoryList.size();// 이전 리스트 크기를 자기자신한테 더하기
			}
			scMain[category] = new JScrollPane(this);
			scMain[category].setPreferredSize(new Dimension(800, 450));
			scMain[category].getVerticalScrollBar().setUnitIncrement(20);
			cardP.add(scMain[category] , "P" + category);
		}};
	}
	
	public void panelAdd(JPanel p, List<JButton> buts, int global) {
		final int globalfinal = this.global;
		p.add(new JPanel(new BorderLayout()) {{
			setPreferredSize(new Dimension(100,215));setBorder(BorderFactory.createLineBorder(Color.black));
			add(new JPanel(new BorderLayout()) {{
				setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
				
				buts.add(new JButton(new ImageIcon(Query.getImge.getImge(actionList.get(globalfinal-1).getInt(0)).getScaledInstance(140, 100, Image.SCALE_SMOOTH))));
				buts.get(global-1).setBackground(Color.white); buts.get(global-1).setBorder(BorderFactory.createLineBorder(Color.black));
				add(buts.get(global-1), BorderLayout.NORTH);
				
				buts.get(global-1).addActionListener(e->{
					for(int i = 0; i < actionList.size(); i++) {
						if(e.getSource() == butA.get(i) || e.getSource() == butC.get(i)) {
							if(user == 1119) {new A_Admin_productChang(actionList.get(i).getInt(0));}
							if(user != 1119 && user != 0) {new C_Detail(user, actionList.get(i).getInt(0));}
							f.dispose();
						}
					}
				});
				
				
				add(new JPanel(new GridLayout(4,1, 0, 10)) {{
					add(new JLabel("상품명 : " + actionList.get(globalfinal-1).getString(1)));
					add(new JLabel("가격 : " + DF.format(actionList.get(globalfinal-1).getInt(3)) + "원"));
					if(actionList.get(globalfinal-1).get(7) == null) {add(new JLabel("판매량 : 0"));
					}else {add(new JLabel("판매량 : " + actionList.get(globalfinal-1).get(8)));}
					if(actionList.get(globalfinal-1).get(7) == null) {add(new JLabel("평점 : 0.0"));
					}else {add(new JLabel("평점 : " + actionList.get(globalfinal-1).get(7).toString().substring(0,3)));}
				}}, BorderLayout.CENTER);
			}});
		}});
	}
	
}