package main;

import java.awt.*;
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

public class AMain extends BaseFrame{
	JButton[] but = new JButton[5];
	JButton Login = new JButton();
	JLabel name = new JLabel(" ");
	String[] logoName = {"메인","검색","장바구니","구매목록","배송정보"};
	ImageIcon[] img = new ImageIcon[5];
	ImageIcon[] imgB = new ImageIcon[5];
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	int user = 0;
	int Serchs = -1;
	int global = 0;
	public AMain(int user, int Serchs) {
		this.Serchs = Serchs;
		this.user = user;
		setFrame("메인", 1000, 700, ()->{});
	}
	@Override
	public void desgin() {
		UIManager.put("OptionPane.background", new ColorUIResource(Color.white));
		UIManager.put("Panel.background", new ColorUIResource(Color.white));
		add(new JPanel(new BorderLayout()) {{
			setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
			setBackground(Color.white);
			add(new JLabel("Roupang") {{
				setFont(setBoldFont(24));
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
						add(name,BorderLayout.SOUTH);
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
				add(Login, BorderLayout.EAST);
			}}, BorderLayout.EAST);
		}},BorderLayout.NORTH);
		
		
		
		
		
		
		cardP.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
		cardP.setBackground(Color.white);
		
		
		if(Serchs == -1) {
			new HOME(cardP);
			new Serch(cardP, card, 0);
			add(cardP, BorderLayout.CENTER);
			
		}else {
			new HOME(cardP);
			new Serch(cardP, card, Serchs);
			add(cardP, BorderLayout.CENTER);
			card.show(cardP, "P2");
		}
		
		
		
		
		
		
		
		
		add(new JPanel(new GridLayout(0,5, 50, 0)) {{
			setBackground(Color.white);
			setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
			for(int i = 0; i < 5; i++) {
				img[i] = new ImageIcon(new ImageIcon(getClass().getResource("/logo/" + logoName[i] +".png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
				try {
					BufferedImage imgs = ImageIO.read(getClass().getResource("/logo/" + logoName[i] +".png"));
					BufferedImage recolored = new BufferedImage(imgs.getWidth(), imgs.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2 = recolored.createGraphics();
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
				but[i].setIcon(img[i]);add(but[i]);
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
		for(int i = 0; i < 5; i++) {
			but[i].addActionListener(e->{
				if(e.getSource() == but[0]) {
					card.show(cardP, "P1");
					Serch.ALLShow("P0");
				}if(e.getSource() == but[1]) {
					card.show(cardP, "P2");
				}
				if(user == 0 && (e.getSource() == but[2] || e.getSource() == but[3] || e.getSource() == but[4])) {
						JOptionPane.showMessageDialog(this, "로그인 후 사용 가능합니다.", "경고", JOptionPane.ERROR_MESSAGE);
				}else{
					for(int j = 0; j < 5; j++) {
						if(e.getSource() == but[j]) {but[j].setIcon(imgB[j]);}else {but[j].setIcon(img[j]);}
					}
				}
			});
		}
	}
	public static void main(String[] args) {
		new AMain(1, -1);
	}
}
