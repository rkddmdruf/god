package main;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.Query;
import utils.Row;


public class D_Cart2 extends JPanel{
	int user = 0, global = 0, price = 0;
	JLabel priceJL = new JLabel("총금액 : 0원"){{setFont(setBoldFont(24));setHorizontalAlignment(JLabel.RIGHT);}};
	JCheckBox all = new JCheckBox("전체 선택") {{setBackground(Color.white);}};
	JButton[] but = {new JButton("삭제"), new JButton("구매하기")};
	JPanel MainP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
	
	List<JCheckBox> CKB = new ArrayList<JCheckBox>();
	List<JPanel> JP = new ArrayList<JPanel>();
	
	
	D_Cart2(JPanel p, int user){
		this.user = user;
		List<Row> lt = Query.cart.select(user+"");
		JCheckBox[] CKB1 = new JCheckBox[lt.size()];
		JPanel[] JP1 = new JPanel[CKB1.length];
		
		for(int i = 0; i < lt.size(); i++) {
			CKB.add(new JCheckBox()); CKB.get(i).setBackground(Color.white);
			JP.add(new JPanel());JP.get(i).setBackground(Color.white);
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
					JP.get(global).add(CKB.get(global),BorderLayout.WEST);
					JP.get(global).add(new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/img/" + list.getInt(2) + ".jpg")).getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH))) {{
						setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
						setBorder(BorderFactory.createLineBorder(Color.black));
					}},BorderLayout.CENTER);
					JP.get(global).add(new JPanel(new GridLayout(4, 1)) {{
						setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
						add(new JLabel("상품명 : " + list.getString(4)) {{setPreferredSize(new Dimension(85,20));}});
						add(new JLabel("가격 : " + new DecimalFormat("###,###").format(list.getInt(5)) + "원"));
						add(new JLabel("수량 : " + list.getInt(3)));
						add(new JLabel("합계 : " + new DecimalFormat("###,###").format(list.getInt(5) * list.getInt(3)) + "원"));
					}}, BorderLayout.EAST);
					MainP.add(JP.get(global));
					global++;
				}
			}
			this.add(MainP, BorderLayout.CENTER);
		
		add(new JPanel(new GridLayout(0,2)) {{
			add(all);
			add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)) {{
				for(JButton b : but) {add(b);}
			}});
		}}, BorderLayout.SOUTH);
		p.add(this, "P3");
		action(JP, CKB, lt);
		List<Row> lists = Query.category.select();
		
	}
	
	private void action(List<JPanel> P, List<JCheckBox> CKB, List<Row> list) {
		for(int i = 0 ; i < CKB.size(); i++) {
			CKB.get(i).addItemListener(e->{
				for(int j = 0; j < CKB.size(); j++) {
					if(e.getSource() == CKB.get(j) && CKB.get(j).isSelected()) {
						price += (list.get(j).getInt(5) * list.get(j).getInt(3));
						priceJL.setText("총금액 : " + new DecimalFormat("###,###").format(price) + "원");
						///////////////
						for(int s = 0; s < CKB.size(); s++) {
							all.setSelected(true);
							if(!CKB.get(s).isSelected()) {
								all.setSelected(false);
								break;
							}
						}
						///////////////////
					}else if(e.getSource() == CKB.get(j) && !CKB.get(j).isSelected()) {
						price -= (list.get(j).getInt(5) * list.get(j).getInt(3));
						priceJL.setText("총금액 : " + new DecimalFormat("###,###").format(price) + "원");
						all.setSelected(false);
					}
				}
			});
		}
		
		all.addActionListener(e->{
			if(all.isSelected()) {
				for(int i = 0; i < CKB.size(); i++) {
					CKB.get(i).setSelected(true);
				}
			}else if(!all.isSelected()) {
				for(int i = 0; i < CKB.size(); i++) {
					CKB.get(i).setSelected(false);
				}
			}
		});
		
		but[0].addActionListener(e->{
			int number = 0;
			List<Integer> data = new ArrayList<>();
			for(int i = 0; i < CKB.size(); i++) {
				if(CKB.get(i).isSelected()) {data.add(i);}//선택돼 있으면 data에 몇번째 인지 추가
				else {number++;}
			}
			if(number == CKB.size()) {
				JOptionPane.showMessageDialog(this, "삭제할 상품을 선택하세요.", "경고", JOptionPane.ERROR_MESSAGE);
			}else if(number != CKB.size()) {
				for(int i = data.size()-1; i >= 0; i--) {//// 선택상품 사이즈 -1(두개면 2-1 = 1 -> for돌때 1, 0두번 돔)
					int CKBS = CKB.size()-1;////이거도 맞찬가지
					for(int j = CKBS; j >= 0; j--) {//뒤에서 부터 삭제
						if(data.get(i) == j) {
							System.out.println(list.get(data.get(i)).toString());
							Query.cartdelete.update(list.get(data.get(i)).getString(0));
							MainP.remove(P.get(data.get(i)));
							CKB.get(data.get(i)).setSelected(false);
							P.remove(Integer.parseInt(data.get(i).toString()));
							CKB.remove(Integer.parseInt(data.get(i).toString()));
							revalidate();repaint();
							priceJL.setText("총금액 : " + new DecimalFormat("###,###").format(price) + "원");
						}
					}
				}
			}

			if(MainP.getComponentCount() == 0) {
				MainP.setLayout(new BorderLayout());
				MainP.add(new JLabel("장바구니가 비어있습니다.") {{setFont(setBoldFont(30));setHorizontalAlignment(JLabel.CENTER);}});
			}
		});/*List<Row> quantity = new ArrayList<>();
		for(int i = 0; i < list.size(); i++) {
			Query.cart개수.select(user+"");
		}*/
		but[1].addActionListener(e->{
			int number = 0;
			List<Integer> data = new ArrayList<>();
			for(int i = 0; i < CKB.size(); i++) {
				if(CKB.get(i).isSelected()) {data.add(i);}//선택돼 있으면 data에 몇번째 인지 추가
				else {number++;}
			}
			if(number == CKB.size()) {
				JOptionPane.showMessageDialog(this, "구매할 상품을 선택하세요.", "경고", JOptionPane.ERROR_MESSAGE);
			}else if(number != CKB.size()) {
				for(int i = data.size()-1; i >= 0; i--) {//// 선택상품 사이즈 -1(두개면 2-1 = 1 -> for돌때 1, 0두번 돔)
					int CKBS = CKB.size()-1;////이거도 맞찬가지
					for(int j = CKBS; j >= 0; j--) {//뒤에서 부터 삭제
						if(data.get(i) == j) {
							if(Query.cart개수.select(list.get(j).getString(2)).get(0).getInt(0)-list.get(j).getInt(3) >= 0){
								System.out.println(list.get(data.get(i)).toString());
								Query.cartdelete.update(list.get(data.get(i)).getString(0));
								MainP.remove(P.get(data.get(i)));
								CKB.get(data.get(i)).setSelected(false);
								P.remove(Integer.parseInt(data.get(i).toString()));
								CKB.remove(Integer.parseInt(data.get(i).toString()));
								revalidate();repaint();
								priceJL.setText("총금액 : " + new DecimalFormat("###,###").format(price) + "원");
								JOptionPane.showMessageDialog(this, "구매가 완료되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
								Query.orderinsert.update(user+"", list.get(j).getString(2), list.get(j).getString(3), LocalDate.now()+"");
								Query.cartdelete.update(""+ list.get(j).getString(0));
							}else{
								JOptionPane.showMessageDialog(this, "재고가 부족합니다.", "경고", JOptionPane.ERROR_MESSAGE);
							}
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
