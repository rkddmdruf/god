package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import utils.*;
import utils.sp.cp;

public class Chat extends BaseFrame{
	List<Row> list = Query.select("SELECT category.cname, count(category.cno) as co "
			+ "FROM parttimecat.likes\r\n"
			+ "left join job on likes.jno = job.jno "
			+ "left join brand on job.bno = brand.bno "
			+ "left join category on brand.cno = category.cno "
			+ "where uno = ? group by category.cno order by co desc;", sp.user.get(0));
	List<Row> list1 = Query.select("SELECT brand.bname, count(apply.apno) as co FROM parttimecat.apply "
			+ "			left join job on apply.jno = job.jno left join brand on brand.bno = job.bno group by bname order by co desc limit 5;");
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 10, 10), null);
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	JPanel CenterPanel = new sp.cp(new BorderLayout(), null, null);
	JPanel SouthPanel = new sp.cp(new GridLayout(0,5, 30,10), sp.em(0, 50, 0, 50), null) {{
		setPreferredSize(new Dimension(100, 40));
	}};
	JPanel CenterPanel1 = new sp.cp(new BorderLayout(), null, null);
	JPanel SouthPanel1 = new sp.cp(new GridLayout(0,5, 30,10), sp.em(0, 50, 0, 50), null) {{
		setPreferredSize(new Dimension(100, 40));
	}};
	JComboBox<String> cb = new JComboBox<String>("내가 좋아하는 알바,인기 많은 브랜드".split(","));
	
	Color[] color = new Color[5];
	Color[] color1 = new Color[5];
	double starting = 0, end = 0;
	Chat(){
		setFrame("동계", 700, 550, ()->{new Main();});
	}
	@Override
	protected void desing() {
		System.out.println(list1);
		for(int i = 0; i < 5; i++) {
			int r = (int) ((Math.random() * 255) + 1), 
				g = (int) ((Math.random() * 255) + 1), 
				b = (int) ((Math.random() * 255) + 1);
			color[i] = new Color(r,g,b);
			r = (int) ((Math.random() * 255) + 1);
			g = (int) ((Math.random() * 255) + 1);
			b = (int) ((Math.random() * 255) + 1);
			color1[i] = new Color(r,g,b);
			SouthPanel.add(new top5(color[i], list.get(i).getString(0)));
			SouthPanel1.add(new top5(color1[i], list1.get(i).getString(0)));
		}
		borderPanel.add(new sp.cp(new FlowLayout(FlowLayout.LEFT), null, null) {{
			add(cb);
		}}, sp.n);
		
		cardP.add(CenterPanel, "P0");
		CenterPanel.add(new ch(list));
		CenterPanel.add(SouthPanel,sp.s);
		
		
		
		cardP.add(CenterPanel1, "P1");
		CenterPanel1.add(new ch(list1));
		CenterPanel1.add(SouthPanel1,sp.s);
		
		borderPanel.add(cardP, sp.c);
		add(borderPanel);
	}

	@Override
	protected void action() {
		cb.addActionListener(e->{
			if(cb.getSelectedIndex() == 0) {
				card.show(cardP, "P0");
			}else {
				card.show(cardP, "P1");
			}
		});
	}
	
	class ch extends JPanel{
		List<Row> lists = new ArrayList<>();
		ch(List<Row> lists){
			this.lists = lists;
		}
		@Override
		public void paintComponent(Graphics g) {
			setBackground(Color.white);
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			int max = 0;
			for(int i = 0; i < 5; i++) max += lists.get(i).getInt(1); 
			double total = 360.0 / ((double) max);
			
			for(int i = 0; i < 5; i ++) {
				starting += end;
				end = lists.get(i).getInt(1) * total;
				g2.setColor(color[i]);
				Arc2D.Double arc = new Arc2D.Double(200,50, 300, 300, starting, end, Arc2D.PIE);
				g2.fill(arc);
			}
			starting = 0;
			end = 0;
		}
	}
	class top5 extends JPanel{
		Color color;
		String string;
		top5(Color color, String string){
			this.string = string;
			this.color = color;
		}
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(color);
			g.fillRect(0, 0, 20, 20);
			g.setColor(Color.black);
			g.setFont(sp.font(0, 9));
			g.drawString(string, 21, 13);
		}
	}
	public static void main(String[] args) {
		new Chat();
	}
}
