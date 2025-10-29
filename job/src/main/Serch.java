package main;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import utils.*;
import utils.sp.cc;
import utils.sp.cl;
import utils.sp.cp;

public class Serch extends BaseFrame{
	JPanel mainPanel = new sp.cp(null, null, null);
	JPanel eastPanel = new sp.cp(new BorderLayout(), sp.em(0, 10, 20, 5), null);
	JPanel categoryPanel = new sp.cp(new BorderLayout(), null, null);
	List<JPanel> panels = new ArrayList<JPanel>();
	List<Row> category = Query.select("SELECT * FROM parttimecat.category;");
	//-------------------------------------- 아래는 포인트(점 관련)
	private static List<Row> list = Query.select("SELECT * FROM parttimecat.brand where cno >= 0 and cno <= 10;");
	private static List<Row> listXY = Query.select("SELECT * FROM parttimecat.brand where cno >= 0 and cno <= 10;");
	private static List<Row> XY = new ArrayList<>();
	int timerCount = 0;
	Timer t;
	private boolean mouse = false;
	private int doubleClick = 0, brand = 0;
	private static boolean[] clickCheck = new boolean[list.size() + 1];
	Serch(){
		setFrame("찾기", 680, 540, () ->{new Main();});
	}
	
	@Override
	protected void desing() {
		System.out.println(listXY);
		clickCheck[0] = true;
		category.add(0, new Row() {{
			add(0); add("전체");
		}});
		mainPanel.add(new point() {{
			setBounds(0, 0, 500, 500);
		}});
		
		categoryPanel.add(new sp.cp(new BorderLayout(), sp.line, null) {{
			add(new sp.cl("직종 카테고리").font(sp.font(1, 12)).setHo(JLabel.CENTER));
		}}, sp.n);
		categoryPanel.add(new sp.cp(new GridLayout(0, 1), null, null) {{
			for(Row row : category) {
				JPanel p = new sp.cp(new BorderLayout(), sp.line, null) {{
					setPreferredSize(new Dimension(150, 30));
					add(new sp.cl(row.getString(1)).font(sp.font(0, 12)));
				}};
				panels.add(p);
				add(panels.get(panels.size()-1));
			}
		}});
		eastPanel.add(categoryPanel, sp.n);
		eastPanel.add(new JLabel(sp.getImg("icon/cat.png", 150, 70)), sp.e);
		add(eastPanel, sp.e);
		add(mainPanel, sp.c);
	}

	@Override
	protected void action() {
		mainPanel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				showPop(e.getX(), e.getY());
			}
		});
		mainPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(doubleClick == 2) {
					new BrandInf(brand);
					dispose();
				}else {
					doubleClick++;
				}
				int x = e.getX();
				int y = e.getY();
				BufferedImage imgs = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
				Graphics g2 = imgs.createGraphics();
				mainPanel.paint(g2);
				for(Row row : XY) {
			    	int w = (int) (row.getInt(0) * (500.0 / 600.0));
					if(w < x && w+10 > x) {
						if(row.getInt(1) < y && row.getInt(1)+10 > y) {
							t = new Timer(20, e1->{
								timerCount += 1;
								try {
									int a = 0 + timerCount, b = 0 + timerCount, c = 500 - (timerCount * 2), d = 500 - (timerCount * 2);
									System.out.println(a + " + " + b + " + " + c + " + " + d);
									BufferedImage resizedImage = imgs.getSubimage(a,b,c,d);
									mainPanel.removeAll();
									mainPanel.add(new JLabel(new ImageIcon(resizedImage.getScaledInstance(500, 500, Image.SCALE_SMOOTH))) {{
										setBounds(0,0, 500, 500);
									}});
									RePaint();
								} catch (Exception e2) {
									System.out.println(e2.getMessage());
								}
							});
							t.start();
						}
					}
				}
			}
		});
		
		panels.get(0).setBackground(Color.red);
		panels.get(0).getComponent(0).setForeground(Color.white);
		for(int j = 0; j < panels.size(); j++) { final int i = j;
			panels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(category.get(i));
					JPanel p = panels.get(i);
					p.setBackground(p.getBackground() == Color.white ? Color.red : Color.white);
					JLabel l = (JLabel) p.getComponent(0);
					l.setForeground(p.getBackground() == Color.white ? Color.black : Color.white);
					clickCheck[i] = p.getBackground() == Color.WHITE ? false : true;
					mainPanel.removeAll();
					mainPanel.add(new point() {{
						setBounds(0, 0, 500, 500);
					}});
					RePaint();
				}
			});
		}
	}
	
	private void showPop(int x, int y) {
		String str = "SELECT * FROM parttimecat.brand where";
		listXY = Query.select("SELECT * FROM parttimecat.brand");
		for(int i = 0; i < clickCheck.length; i++) {
			if(clickCheck[i] && i != 0) {
				str += " cno = " + i + " or ";
				listXY = Query.select(str.substring(0, str.length() -3));
			}
		}
	    JPopupMenu popupMenu = new JPopupMenu();
	    JLabel label = new JLabel();
	    doubleClick = 0;
	    for(Row row : listXY) {
	    	int w = (int) (row.getInt(3) * (500.0 / 600.0));
			if(w < x && w+10 > x) {
				if(row.getInt(4) < y && row.getInt(4)+10 > y) {
					mouse = true; doubleClick = 1; brand = row.getInt(0);
					label.setText(row.getString(1));
					break;
				}
			}
		}
	    popupMenu.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	    popupMenu.setBackground(Color.white);
	    popupMenu.add(label);
	    popupMenu.show(this, x, y+5);
	    popupMenu.setVisible(mouse);
	}
	public static class point extends JPanel{
		@Override
		public void paintComponent(Graphics g) {
			XY = new ArrayList<>();
			g.clearRect(0, 0, 500, 500);
			g.drawImage(sp.getImg("지도.png", 500,500).getImage(),0, 0, this);
			g.setColor(Color.red);
			for(int i = 0; i < clickCheck.length; i++) 
				if(clickCheck[i]) {
					list = Query.select("SELECT * FROM parttimecat.brand where cno >= ? and cno <= ?;",
							i == 0 ? 0 : i, i == 0 ? 10 : i);
					for(Row row : list) {
						int w = (int) (row.getInt(3) * (500.0 / 600.0)), h = row.getInt(4);
						XY.add(new Row() {{  add(w); add(h);  }});
						g.fillOval(w, h, 10, 10);
					}
				}
			if(clickCheck[0]) XY = Query.select("SELECT bxx, byy FROM parttimecat.brand");
		}
	}
	public static void main(String[] args) {
		new Serch();
	}
}
