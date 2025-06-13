package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.*;

import utils.BaseFrame;
import utils.ButtonMaker;
import utils.Query;
import utils.Row;

public class MainPom extends BaseFrame{

	List<JPanel> Plist = new ArrayList<JPanel>();
	JButton user = new JButton();
	JTextField tf = new JTextField() {{setPreferredSize(new Dimension(225, 25));}};
	JButton[] but = {new ButtonMaker("검색", Color.blue,Color.white, 70, 25), new ButtonMaker("로그인", Color.blue,Color.white, 75, 25)};
	DefaultMutableTreeNode all = new DefaultMutableTreeNode("전체") {{
		List<Row> list = Query.MainPomTree.select();
		for(int i = 0; i < list.size(); i++) {
			List<Row> list2 = Query.MainPomTree2.select(""+(i+1));
			this.add(new DefaultMutableTreeNode(list.get(i).getString(0)) {{
				for(int i = 0; i < list2.size(); i++) {
					this.add(new DefaultMutableTreeNode(list2.get(i).getString(0)));	
				}
			}});
		}
	}};
	JTree tree = new JTree(all) {{setPreferredSize(new Dimension(100, 450));
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}};
	JButton[] sort = {new ButtonMaker("가격순(↓)", Color.white, Color.black, 90, 35),new ButtonMaker("가격순(↑)", Color.white, Color.black, 90, 35),
			new ButtonMaker("별점순(↓)", Color.white, Color.black, 90, 35)};
	CardLayout card = new CardLayout();
	JPanel productP = new JPanel(new GridLayout(0,4)) {{List<Row> list = Query.MainPomProduct.select();setBackground(Color.white);setLisst(this, list,0);}};
	DecimalFormat comma = new DecimalFormat("###,###");
	MainPom(){
		setFrame("메인", 900, 600, ()->{});
	}
	@Override
	public void desgin() {
		List<Row> lists = Query.MainPomProduct.select();
		System.out.println(65000 - (65000 / 10));
		add(new JPanel(new FlowLayout(0,20,5)) {{
			setBackground(Color.white);
			add(new JLabel("ClothingStore") {{setFont(setBoldFont(28));}});
			add(tf);add(but[0]);
			if(1 > 0) {
				add(new JPanel(new BorderLayout()) {{setPreferredSize(new Dimension(300,30));setBackground(Color.white);
					add(new JPanel(new BorderLayout()) {{setPreferredSize(new Dimension(120,0));setBackground(Color.white);
						user.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/main/1.png")).
								getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
						user.setPreferredSize(new Dimension(30,30));
						add(user,BorderLayout.WEST);
						add(new JPanel(new BorderLayout()) {{
							setBackground(Color.white);
							add(new JLabel("손준서") {{setHorizontalAlignment(JLabel.CENTER);}},BorderLayout.NORTH);
							add(new JLabel(comma.format(69796)+"원") {{setHorizontalAlignment(JLabel.CENTER);}},BorderLayout.SOUTH);
						}});
					}}, BorderLayout.EAST);
				}});
			}else {
				add(but[1]);
			}
		}}, BorderLayout.NORTH);
		
		add(new JPanel(new BorderLayout()) {{
			setBackground(Color.white);
			setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
			add(new JPanel(new BorderLayout()) {{///////////15
				add(new JScrollPane(tree), BorderLayout.WEST);
				JScrollPane js = new JScrollPane(new JPanel(new BorderLayout()) {{/////////10
					setBorder(BorderFactory.createLineBorder(Color.GRAY));
					add(new JPanel(new FlowLayout(10,10,5)) {{
						setBackground(Color.white);
						for(int i = 0; i < 3; i++) {
							add(sort[i]);
						}
					}}, BorderLayout.NORTH);
					add(new JPanel(new BorderLayout()) {{
						setBackground(Color.white);
						setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
						add(productP);
					}});
				}});//////////10
				js.getVerticalScrollBar().setUnitIncrement(30);
				add(js);
			}});//////15
		}}, BorderLayout.CENTER);
	}

	@Override
	public void action() {
		for(JButton jb : sort) {
			jb.addActionListener(e->{
				if(e.getSource() == sort[0]) {
					List<Row> list = Query.MainPomMoneyDesc.select();
					setLisst(productP, list,10);
				}
			});
		}
		but[0].addActionListener(e->{
			
		});
	}
	
	public void setList(JPanel p, List<Row> list) {
		p.add(new JPanel(new GridLayout(0,4,20,20)) {{
			System.out.println(list.size());
			setBackground(Color.white);
			for(Row i : list) {
				add(new JButton() {{
					add(new JPanel(new BorderLayout()) {{
						setBackground(Color.white);
						add(new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/imgs/상품이미지/"+i.getInt(0)+".jpg")).getImage()
								.getScaledInstance(120, 120, Image.SCALE_SMOOTH))), BorderLayout.CENTER);
						add(new JPanel(new BorderLayout()) {{
							setBackground(Color.white);
							char[] c = i.getString(1).toCharArray();
							char[] cpy = new char[18];
							add(new JLabel(new String(c)) {{setHorizontalAlignment(JLabel.CENTER);}},BorderLayout.NORTH);
							if(c.length >= 15) {for(int i = 0; i < 15; i++) {cpy[i] = c[i];}for(int i = 15; i< 18; i++) {cpy[i] = '.';}
								add(new JLabel(new String(cpy)) {{setHorizontalAlignment(JLabel.CENTER);}},BorderLayout.NORTH);
							}
							add(new JLabel(i.getString(2)+"원") {{setHorizontalAlignment(JLabel.CENTER);}},BorderLayout.SOUTH);
						}}, BorderLayout.SOUTH);
						
					}});
					setPreferredSize(new Dimension(50,200));
					setBorder(BorderFactory.createLineBorder(Color.black));
				}});
			}
		}});
	}
	public void setLisst(JPanel p, List<Row> list, int sele) {
		List<Integer> ints = new ArrayList<>();
		System.out.println(list.get(0).getClass());
		int check = 0;
		for(Row i : list) {
			ints.add(check);
			check++;
		}
		int[] cs = new int[4];
		for(int i = 1; i < (check/4)+1; i++) {
			for(int j = (i*4)-4; j < i*4; j++) {
				cs[(i*4)-j-1] = j;
			}
			Arrays.sort(cs);
			for(int j : cs) {//Plist.get(j).
				Plist.add(new JPanel() {{
					setBackground(Color.white);
					this.add(new JPanel() {{
						setBackground(Color.white);
						add(new JButton() {{
							add(new JPanel(new BorderLayout()) {{
								setBackground(Color.white);
								add(new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/imgs/상품이미지/"+ list.get(j).getInt(0)+".jpg")).getImage()
										.getScaledInstance(120, 120, Image.SCALE_SMOOTH))), BorderLayout.CENTER);
								add(new JPanel(new BorderLayout()) {{
									setBackground(Color.white);
									char[] c = list.get(j).getString(1).toCharArray();
									char[] cpy = new char[18];
									add(new JLabel(new String(c)) {{setHorizontalAlignment(JLabel.CENTER);}},BorderLayout.NORTH);
									if(c.length >= 15) {for(int i = 0; i < 15; i++) {cpy[i] = c[i];}for(int i = 15; i< 18; i++) {cpy[i] = '.';}
										add(new JLabel(new String(cpy)) {{setHorizontalAlignment(JLabel.CENTER);}},BorderLayout.NORTH);
									}
									add(new JLabel(list.get(j).getString(2)+"원") {{setHorizontalAlignment(JLabel.CENTER);}},BorderLayout.SOUTH);
								}}, BorderLayout.SOUTH);
								
							}});
							setPreferredSize(new Dimension(160,200));
							setBackground(Color.white);
							setBorder(BorderFactory.createLineBorder(Color.black));
						}});
					}});
				}});
			}
		}
		for(JPanel ps : Plist) {
			p.add(ps);
		}
		for(JPanel ps : Plist) {
			p.remove(0);
		}
		
	}
	public static void main(String[] args) {
		new MainPom();
	}
}
