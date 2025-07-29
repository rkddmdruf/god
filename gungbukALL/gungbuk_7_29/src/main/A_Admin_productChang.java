package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;

import utils.BaseFrame;
import utils.Query;
import utils.Row;
import utils.sp;

public class A_Admin_productChang extends BaseFrame{
	int number = 0;int global = 1;
	String[] label = "카테고리 상품명 설명 가격 수량".split(" ");
	JTextField[] tf = new JTextField[4];
	JLabel imgL = new JLabel() {{setPreferredSize(new Dimension(265, 180));setBackground(Color.white);}};
	
	JButton[] but = {new JButton("수정"), new JButton("등록")};
	
	String[] cbName = "식품 문구 생활용품 뷰티 음료 전자제품 의류 유아 스포츠 도서".split(" ");
	JComboBox<String> cb = new JComboBox<String>(cbName);
	List<Row> list = new ArrayList<>();
	
	public A_Admin_productChang(int number) {
		this.number = number;
		setFrame("상품수정", 300, 500, ()->{});
	}
	
	
	@Override
	protected void desgin() {
		System.out.println(number);
		if(number != 0) {
			imgL.setIcon(new ImageIcon(Query.getImge.getImge(number).getScaledInstance(265, 180, Image.SCALE_SMOOTH)));
			list = Query.productAll.select(number);
		}else {cb = new JComboBox<String>(" , , , , , , , , , ".split(","));}
		
			add(new JPanel(new BorderLayout()) {{
				setBackground(Color.white);
				sp.setBorderIE(this,10, 10, 10, 10);
				sp.setBorderLINE(imgL);
				add(imgL, sp.n);
				add(new JPanel(new BorderLayout()) {{
					add(new JPanel(new GridLayout(5,0, 0, 15)) {{
						setBackground(Color.white);sp.setBorderIE(this, 10, 5, 10, 0);for(String s : label) {add(new JLabel(s) {{setFont(sp.fontM(1, 15));}});}
					}}, sp.w);
					add(new JPanel(new GridLayout(5,0, 0, 15)) {{setBackground(Color.white);
						sp.setBorderIE(this, 10, 5, 10, 0);add(cb);
						if(number != 0) {cb.setSelectedIndex(list.get(0).getInt(5)-1);}
						for(int i = 0; i < 4; i++) {
							if(number != 0) {tf[i] = new JTextField(list.get(0).getString(i+1));
							}else {tf[i] = new JTextField();}
							add(tf[i]);
						}
					}}, sp.c);
					if(number != 0) {add(but[0], sp.s);}else {add(but[1], sp.s);}
					
				}}, sp.c);
			}});
		
	}

	@Override
	protected void action() {
		for(int i = 0; i < 2; i++) {final int in = i;
			but[i].addActionListener(e->{
				int a = 0, b = 0; boolean c = true;
				for(int j = 0; j <= 2; j+=2) {
					if(tf[j].getText().equals("") || tf[j+1].getText().equals("")) {c = false;}}
				if(!c) {sp.ErrMes("빈칸이 있습니다.");
				}else{
					try {
						a = Integer.parseInt(tf[2].getText()); b = Integer.parseInt(tf[3].getText());
						if(a <= 0 || b <= 0) {Integer.parseInt("ERROR");}
						else if(in == 0){
							Query.productUpdate.update(cb.getSelectedIndex(), tf[0].getText(), tf[1].getText(), tf[2].getText(), tf[3].getText(), number);
							sp.InforMes("수정이 완료되었습니다.");new AMain(1119, -1);dispose();
						}else if(in == 1) {
							if(!Query.product글자비교.select(tf[0].getText()).isEmpty()) {
								sp.ErrMes("이미존재하는 상품입니다.");
							}else {
								Query.productinsert.update(tf[0].getText(), tf[1].getText(), tf[2].getText(), tf[3].getText(), cb.getSelectedItem());
								
							}
						}
					} catch (Exception e2) {
						System.out.println(e2.getMessage());
						sp.ErrMes("가격과 수량을 1이상의 숫자로 입력하세요.");
					}
				}
			});
		}
		cb.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				cb.removeAllItems();
				for(String s : cbName) {cb.addItem(s);}
			}
		});
	}
	public static void main(String[] args) {
		new A_Admin_productChang(0);
	}
	
}
