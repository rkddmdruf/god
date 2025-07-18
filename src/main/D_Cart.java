package main;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.Query;
import utils.Row;


public class D_Cart extends JPanel{
	int user = 0, global = 0, price = 0;
	JLabel priceJL = new JLabel("총금액 : 0원"){{setFont(setBoldFont(24));setHorizontalAlignment(JLabel.RIGHT);}};
	JCheckBox all = new JCheckBox("전체 선택") {{setBackground(Color.white);}};
	JButton[] but = {new JButton("삭제"), new JButton("구매하기")};
	JPanel MainP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
	D_Cart(JPanel p, int user){
		this.user = user;
		List<Row> lt = Query.cart.select(user+"");
		System.out.println(lt);
		List<JCheckBox> CKB = new ArrayList<JCheckBox>();
		JCheckBox[] CKB1 = new JCheckBox[lt.size()];
		JPanel[] JP = new JPanel[CKB1.length];
		for(int i = 0; i < lt.size(); i++) {
			CKB1[i] = new JCheckBox();CKB1[i].setBackground(Color.white);
			
			JP[i] = new JPanel(new BorderLayout());JP[i].setBackground(Color.white);
		}
		setBackground(Color.white);
		setLayout(new BorderLayout());
		add(new JPanel(new GridLayout(1, 2)) {{
			add(new JLabel("장바구니") {{setFont(setBoldFont(24));}});
			add(priceJL);
		}},BorderLayout.NORTH);
		
		
			MainP.setBorder(BorderFactory.createLineBorder(Color.black));
			if(lt.isEmpty()) {
				MainP.setLayout(new BorderLayout());
				MainP.add(new JLabel("장바구니가 비어있습니다.") {{setFont(setBoldFont(30));setHorizontalAlignment(JLabel.CENTER);}});
			}else {
				for(Row list : lt) {
					JP[global].add(CKB1[global],BorderLayout.WEST);
					JP[global].add(new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/img/" + list.getInt(2) + ".jpg")).getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH))) {{
						setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
						setBorder(BorderFactory.createLineBorder(Color.black));
					}},BorderLayout.CENTER);
					JP[global].add(new JPanel(new GridLayout(4, 1)) {{
						setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
						add(new JLabel("상품명 : " + list.getString(4)) {{setPreferredSize(new Dimension(100,20));}});
						add(new JLabel("가격 : " + new DecimalFormat("###,###").format(list.getInt(5)) + "원"));
						add(new JLabel("수량 : " + list.getInt(3)));
						add(new JLabel("합계 : " + new DecimalFormat("###,###").format(list.getInt(5) * list.getInt(3)) + "원"));
					}}, BorderLayout.EAST);
					MainP.add(JP[global]);
					global++;
				}
				this.remove(JP[1]);revalidate();repaint();
			}
			this.add(MainP, BorderLayout.CENTER);
		
		add(new JPanel(new GridLayout(0,2)) {{
			add(all);
			add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)) {{
				for(JButton b : but) {add(b);}
			}});
		}}, BorderLayout.SOUTH);
		p.add(this, "P3");
		action(JP, CKB1, lt);
		List<Row> lists = Query.category.select();
		
	}
	
	private void action(JPanel[] P, JCheckBox[] CKB, List<Row> list) {
		for(int i = 0 ; i < CKB.length; i++) {
			CKB[i].addItemListener(e->{
				for(int j = 0; j < CKB.length; j++) {
					if(e.getSource() == CKB[j] && CKB[j].isSelected()) {
						price += (list.get(j).getInt(5) * list.get(j).getInt(3));
						priceJL.setText("총금액 : " + new DecimalFormat("###,###").format(price) + "원");
						///////////////
						for(int s = 0; s < CKB.length; s++) {
							all.setSelected(true);
							if(!CKB[s].isSelected()) {
								all.setSelected(false);///////////상품이 다 해제 되면 전체 선택도 해제 로직
								break;
							}
						}
						///////////////////
					}else if(e.getSource() == CKB[j] && !CKB[j].isSelected()) {
						price -= (list.get(j).getInt(5) * list.get(j).getInt(3));
						priceJL.setText("총금액 : " + new DecimalFormat("###,###").format(price) + "원");
						///////////////////
						for(int s = 0; s < CKB.length; s++) {
							all.setSelected(false);
							if(CKB[s].isSelected()) {
								all.setSelected(true);///////////상품이 다 해제 되면 전체 선택도 해제 로직
								break;
							}
						}
						//////////////////
					}
				}
			});
		}
		
		all.addActionListener(e->{
			if(all.isSelected()) {
				for(int i = 0; i < CKB.length; i++) {
					CKB[i].setSelected(true);
				}
			}else if(!all.isSelected()) {
				for(int i = 0; i < CKB.length; i++) {
					CKB[i].setSelected(false);
				}
			}
		});
		
		but[0].addActionListener(e->{
			int number = 0;
			List<Integer> data = new ArrayList<>();
			for(int i = 0; i < CKB.length; i++) {
				if(!CKB[i].isSelected()) {number++;}
				else {data.add(i);}
			}
			if(number == CKB.length) {
				JOptionPane.showMessageDialog(this, "삭제할 상품을 선택하세요.", "경고", JOptionPane.ERROR_MESSAGE);
			}else if(number != CKB.length) {
				for(int i = 0; i < data.size(); i++) {
					// 0, 2
					for(int j = 0; j < CKB.length; j++) {
						if(data.get(i) == j) {
							Query.cartdelete.update(list.get(data.get(i)).getString(0));
							MainP.remove(P[data.get(i)]);revalidate();repaint();
							price -= (list.get(data.get(i)).getInt(5) * list.get(data.get(i)).getInt(3));
							priceJL.setText("총금액 : " + new DecimalFormat("###,###").format(price) + "원");
						}
					}
				}
			}
		});
	}
	
	private Font setBoldFont(int i) {
		return new Font("맑은 고딕", Font.BOLD, i);
	}
}
