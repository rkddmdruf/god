package main;

import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;

import utils.BaseFrame;
import utils.Query;
import utils.Row;
import utils.sp;

public class AMain extends BaseFrame{// 검색 위에 기능 만들기
	JButton[] but = new JButton[5];
	JButton Login = new JButton();
	JButton TB = new JButton("삭제");
	JLabel name = new JLabel(" ");
	String[] logoName = {"메인","검색","장바구니","구매목록","배송정보"};
	String[] AlogoName = {"메인","검색","등록","배송처리","분석"};
	ImageIcon[] img = new ImageIcon[5];ImageIcon[] imgA = new ImageIcon[5];
	ImageIcon[] imgB = new ImageIcon[5];ImageIcon[] imgAB = new ImageIcon[5];
	
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	
	HOME home = new HOME();
	Serch serch = new Serch();
	F_Roupang f = new F_Roupang();
	int user = 0;
	int Serchs = -1;
	int global = 0;
	
	
	public AMain(int user, int Serchs) {
		this.Serchs = Serchs;
		this.user = user;
		setFrame("메인", 1000, 700, ()->f.timer.stop());
	}
	
	@Override
	public void desgin() {
		add(new JPanel(new BorderLayout()) {{
			setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
			setBackground(Color.white);
			add(new JLabel("Roupang") {{
				setFont(sp.fontM(1,24));
				setHorizontalAlignment(JLabel.CENTER);
			}});
			add(new JPanel(new BorderLayout()) {{
				setBackground(Color.white);
				Login.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/logo/유저.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
				Login.setBackground(Color.white);Login.setFocusable(false);
				Login.setContentAreaFilled(false);Login.setBorderPainted(false);
				if(user!=0) {
					//파란색
					if(user == 1119) {
						name.setText("관리자");
						name.setHorizontalAlignment(JLabel.CENTER);
						add(name, BorderLayout.SOUTH);
					}else {
						name.setText(Query.MainName.select(user+"").get(0).getString(3));
						name.setHorizontalAlignment(JLabel.CENTER);
						add(name,sp.s);
					}
					
					try {
						BufferedImage imgs = ImageIO.read(getClass().getResource("/logo/유저.png"));
						BufferedImage recolored = new BufferedImage(imgs.getWidth(), imgs.getHeight(), BufferedImage.TYPE_INT_ARGB);
						Graphics2D g2 = recolored.createGraphics();
						g2.drawImage(imgs, 0,0,null);
						g2.setComposite(AlphaComposite.SrcAtop);
						g2.setColor(new Color(0,0,255));
						if(user == 1119) {
							g2.setColor(new Color(255,0,0));
						}
						g2.fillRect(0, 0, imgs.getWidth(), imgs.getHeight());
					    g2.dispose();
					    Login.setIcon(new ImageIcon(recolored.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
					} catch (Exception e) {
					}
				}
				add(Login, sp.e);
			}}, sp.e);
		}},sp.n);
		
		cardP.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
		cardP.setBackground(Color.white);
		
		
		new Last(cardP);
		//new A_Admin_deliveryM(cardP);
		//home = new HOME(cardP, user);
		/*if(Serchs == -1) { 
			serch = new Serch(cardP, card, 0); 
		}else {
			serch = new Serch(cardP, card, Serchs); 
			card.show(cardP,"P2"); 
		}
		new D_Cart2(cardP, user);
		new E_GumeList(cardP, user);
		f = new F_Roupang(cardP, user);*/
		add(cardP, BorderLayout.CENTER);
		
		
		add(new JPanel(new GridLayout(0,5, 50, 0)) {{
			setBackground(Color.white);
			setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
			for(int i = 0; i < 5; i++) {
					imgA[i] = sp.getImg("src/logo/" + AlogoName[i] + ".png", 25, 25);
					img[i] = sp.getImg("src/logo/" + logoName[i] + ".png", 25, 25);
				try {
					BufferedImage imgs = ImageIO.read(getClass().getResource("/logo/" + AlogoName[i] +".png"));
					
					BufferedImage recolored = new BufferedImage(imgs.getWidth(), imgs.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2 = recolored.createGraphics();
					g2.drawImage(imgs, 0,0,null);
					g2.setComposite(AlphaComposite.SrcAtop);
					g2.setColor(new Color(0,0,255));
					g2.fillRect(0, 0, imgs.getWidth(), imgs.getHeight());
				    g2.dispose();
					imgAB[i] = new ImageIcon(recolored.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
					
				    imgs = ImageIO.read(getClass().getResource("/logo/" + logoName[i] +".png"));
					recolored = new BufferedImage(imgs.getWidth(), imgs.getHeight(), BufferedImage.TYPE_INT_ARGB);
					g2 = recolored.createGraphics();
					g2.drawImage(imgs, 0,0,null);
					g2.setComposite(AlphaComposite.SrcAtop);
					g2.setColor(new Color(0,0,255));
					g2.fillRect(0, 0, imgs.getWidth(), imgs.getHeight());
				    g2.dispose();
				    
				    imgB[i] = new ImageIcon(recolored.getScaledInstance(25, 25, Image.SCALE_SMOOTH));
				} catch (Exception e) {
				}
				
				
				but[i] = new JButton() {{setPreferredSize(new Dimension(25,25));}};
				but[i].setBackground(Color.white);but[i].setFocusable(false);
				but[i].setContentAreaFilled(false);but[i].setBorderPainted(false);
				if(user == 1119) {but[i].setIcon(imgA[i]);
				}else {but[i].setIcon(img[i]);}
				add(but[i]);
			}
			
			if(Serchs == -1) {but[0].setIcon(imgB[0]);
			}else if(Serchs > 0) {but[1].setIcon(imgB[1]);}
		}}, BorderLayout.SOUTH);
	}

	@Override
	public void action() {
		Login.addActionListener(e->{
			if(user == 0) {
				new BLogin();
				dispose();
			}else {
				JOptionPane.showMessageDialog(this, "로그아웃되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
				card.show(cardP, "P1");
				Login.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/logo/유저.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
				name.setText(" "); user = 0 ;but[0].setIcon(imgB[0]); for(int i = 1; i < 5; i++) {but[i].setIcon(img[i]);}
			}
		});
		
		
		/*for(int i = 0; i < home.allList.size(); i++) {
			home.butA.get(i).addActionListener(e->{
				for(int j = 0 ; j < home.butA.size(); j++) {
					if(e.getSource() == home.butA.get(j) && user != 0 && user != 1119) {
						new C_Detail(user, home.allList.get(j).get(0));dispose();
					}else if(e.getSource() == home.butA.get(j) && user != 0 && user == 1119) {
						new A_Admin_productChang(home.allList.get(j).get(0));dispose();
					}
				}
			});
		}
		for(int i = 0; i < home.butC.size(); i++) {
			home.butC.get(i).addActionListener(e->{
				for(int j = 0 ; j < home.butC.size(); j++) {
					if(e.getSource() == home.butC.get(j) && user != 0 && user != 1119) {
						new C_Detail(user, home.Catelist[home.cb.getSelectedIndex()-1].get(j%10).getInt(0));
						dispose();
					}else if(e.getSource() == home.butA.get(j) && user != 0 && user == 1119) {
						new A_Admin_productChang(1);dispose();
					}
				}
			});
		}
		
		
		for(global = 0; global < serch.butA.size(); global++) {
			serch.butA.get(global).addActionListener(e->{
				for(int i = 0; i < serch.butA.size(); i++) {
					if(e.getSource() == serch.butA.get(i) && user != 0 && user != 1119) {new C_Detail(user, serch.actionList.get(i).getInt(0));dispose();}
					if(e.getSource() == serch.butA.get(i) && user != 0 && user == 1119) {new A_Admin_productChang(serch.actionList.get(i).getInt(0));dispose();}
				}
				
			});
		}
		for(int i = 0; i < serch.butC.size(); i++) {
			serch.butC.get(i).addActionListener(e->{
				for(int j = 0; j < serch.actionList.size(); j++) {
					if(e.getSource() == serch.butC.get(j) && user != 0 && user != 1119) {new C_Detail(user, serch.actionList.get(j).getInt(0));dispose();}
					if(e.getSource() == serch.butC.get(j) && user != 0 && user == 1119) {new A_Admin_productChang(serch.actionList.get(j).getInt(0));dispose();}
				}
			});
		}
		
		serch.butB[0].addActionListener(e->{
			//serch.remove(serch.cardP);
			//repaint();revalidate();
			if(serch.cb.getSelectedIndex() == 0) {
				if(serch.product.getText().equals("")) {
					System.out.println("비어있음");
				}else if(Query.productSerchisEm.select(serch.product.getText()).isEmpty()){
					sp.ErrMes("검색 결과가 없습니다.");
					serch.product.setText("");
				}else {
					List<Row> list = Query.productSerchisEm.select(serch.product.getText());
					serch.cardP.remove(serch.scMain);
					JPanel serchP = new JPanel(new GridLayout(0, 4 ,20, 10));
					for(int i = 0; i < list.size(); i++) {
						serch.panelAdd(serchP, serch.butS);
					}
					serch.cardP.add(serchP, "P0");
					//serch.cardP.add(serch.scMain, "P0");
				}
			}
			
			if(serch.cb.getSelectedIndex() == 1) {
				try {
					int first = Integer.parseInt(serch.price[0].getText());
					int last = Integer.parseInt(serch.price[1].getText());
					
				} catch (Exception e2) {
					sp.ErrMes("가격을 숫자로 입력하세요");serch.price[0].setText("");serch.price[1].setText("");
				}
			}
		});
		
		*/
		for(int i = 0; i < 5; i++) {final int index = i;
			but[i].addActionListener(e->{
				if(user <= 0) {
					if(index < 2) {
						int PM = 1;if(index == 0) {PM = 1;}else {PM = -1;}
						card.show(cardP, "P" + (index+1));but[index].setIcon(imgB[index]);but[index+PM].setIcon(img[index+PM]);}
					else if(index >= 2) {sp.ErrMes("로그인 후 사용 가능합니다.");}
				}else if(user == 1119) {
					if(index < 2) {
						int PM = 1;if(index == 0) {PM = 1;}else {PM = -1;}
						card.show(cardP, "P" + (index+1));but[index].setIcon(imgAB[index]);but[index+PM].setIcon(imgA[index+PM]);}
					else if(index >= 2) {
						if(index == 2) {
							new A_Admin_productChang(0);dispose();
						}else if(index == 3) {
							
						}
					}
				}else {
					card.show(cardP, "P" + (index+1));
					for(int j = 0; j < 5; j++) {if(j == index) {but[j].setIcon(imgB[j]);}else {but[j].setIcon(img[j]);}}
				}
			});
		}
	}
	public static void main(String[] args) {
		new AMain(1119, -1);
	}
}
