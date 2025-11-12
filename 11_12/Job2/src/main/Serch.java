package main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.util.List;
import utils.*;
import utils.sp.cl;
import utils.sp.cp;

public class Serch extends BaseFrame{
	Timer t = null;
	int nx1 = 0, ny1 = 0, nx2 = 0, ny2 = 0;
	List<Row> brands;
	List<Row> category = Query.selectText("select cname from category");
	JPanel mainPanel = new sp.cp(new BorderLayout(), null, null).size(500, 500);
	JPanel categoryPanel = new cp(new GridLayout(0, 1), null, null);
	ToolTipManager m = ToolTipManager.sharedInstance();
	JPanel eastPanel = new sp.cp(new BorderLayout(60,60), sp.em(0, 10, 10, 10), null) {{
		category.add(0, new Row() {{ add("전체"); }});
		add(new sp.cp(new BorderLayout(), BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black), null) {{
			add(new sp.cl("직종 카테고리").setBorders(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)).setHo(JLabel.CENTER),sp.n);
			for(Row row : category) 
				categoryPanel.add(new sp.cl(row.getString(0)) {{ 
					setOpaque(true); 
					setBackground(Color.white);
				}} .setBorders(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)).setHo(JLabel.LEFT),sp.n);
			add(categoryPanel);
		}});
		add(new sp.cl(sp.getImg("icon/cat.png", 120, 70)), sp.s);
	}}.size(175, 10);
	
	boolean[] check = new boolean[category.size()];
	Serch(){
		setFrame("찾기", 700, 540, ()->{});
	}

	@Override
	protected void desing() {
		check[0] = true;
		setCPanel();
		mainPanel.add(new point());
		add(eastPanel, sp.e);
		add(mainPanel, sp.w);
	}

	@Override
	protected void action() {
		for(int j = 0; j < category.size(); j++) { final int i = j;
			categoryPanel.getComponent(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					check[i] = categoryPanel.getComponent(i).getBackground() == Color.white ? true : false;
					if(i == 0 && categoryPanel.getComponent(0).getBackground() == Color.white) {
						for(int s = 0; s < check.length; s++) check[s] = false;
						check[0] = true;
					}
					if(i != 0 && categoryPanel.getComponent(0).getBackground() == Color.red) {
						check[0] = false;
					}
					setCPanel();
				}
			});
		}
		mainPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(mouseIn(e.getX(), e.getY())) {
					System.out.println(e.getX() + " + " + (500 - e.getX()));
					int wh = 500;
					BufferedImage img = new BufferedImage(wh, wh, BufferedImage.TYPE_INT_ARGB);
					Graphics2D g = img.createGraphics();
					g.setColor(Color.white);
					g.clearRect(0, 0, wh, wh);
					mainPanel.paintAll(g);
					g.dispose();
					Row row = Query.selectText("select * from brand where bno = ?", sp.serch).get(0);
					int x = row.getInt(3) - (row.getInt(3) < 19 ? row.getInt(3) : 20), y = row.getInt(4);
					BufferedImage i = img.getSubimage(0, 0, wh, wh);
					Timer t = new Timer(10, s->{
						BufferedImage f = null;
						f = i.getSubimage(nx1,0,500 - nx2,500);// 0 500, 50 400, 100 300 150, 
						System.out.println(nx1 + " + " + (500 - nx2 * 2));
						mainPanel.removeAll();
						mainPanel.add(new sp.cl(new ImageIcon(f.getScaledInstance(500, 500, Image.SCALE_SMOOTH))));
						nx1 = nx1 + 1;
						if(nx1 > x) nx1 = x;
						nx2 = nx2 + 1;
						if(nx2 > (500 - (x + (x == 0 ? 0 : 40)))) nx2 = (500 - (x + (x == 0 ? 0 : 40)));
						repaints();// 157 93 
					});
					t.start();
					repaints();
				}
			}
		});
		mainPanel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(mouseIn(e.getX(), e.getY())) {
					mainPanel.setToolTipText(Query.selectText("select bname from brand where bno = ?", sp.serch).get(0).getString(0));
					m.setInitialDelay(0);
					m.setEnabled(false);
					m.setEnabled(true);
				}else {mainPanel.setToolTipText(""); m.setEnabled(false);}
			}
		});
		mainPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					if(mouseIn(e.getX(), e.getY())) {
						new BrandInfor(sp.serch);
						dispose();
					}
				}
			}
		});
	}
	
	public void setCPanel() {
		for(int i = 0; i < category.size(); i++) {
			JLabel l = (JLabel) categoryPanel.getComponent(i);
			l.setBackground(check[i] ? Color.red   : Color.white);
			l.setForeground(check[i] ? Color.white : Color.black);
		}
		mainPanel.removeAll();
		mainPanel.add(new point());
		repaints();
	}
	
	class point extends JPanel{
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g.create();
		    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    
			g2.drawImage(sp.getImg("지도.png", 500, 500).getImage(), 0, 0, 500, 500, null);
			g2.setColor(Color.red);
			for(int i = 0; i < check.length; i++) 
				if(check[i]) {
					int[] cno = {i == 0 ? 1 : i, i == 0 ? 10 : i};
					brands = Query.selectText("SELECT * FROM parttimecat.brand where cno between ? and ?;", cno[0], cno[1]);
					for(int j = 0; j < brands.size(); j++) 
						g2.fillOval((int) (brands.get(j).getInt(3) * (500.0 / 600.0)) - 4, brands.get(j).getInt(4) - 4, 8, 8);
				}
		}
	}
	
	public boolean mouseIn(int x, int y) {
		for(Row row : brands){
			int xx = (int) (row.getInt(3) * (500.0 / 600.0));
			int yy = row.getInt(4);
			if(x > xx-4 && x < xx+4)
				if(y > yy-4 && y < yy+4) {
					sp.serch = row.getInt(0);
					return true;
				}
		}
		return false;
	}
	public static void main(String[] args) {
		new Serch();
	}
}
