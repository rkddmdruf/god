package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import utils.*;
import utils.sp.*;
public class Chat extends BaseFrame{
	List<Row> user = Query.selectText("SELECT cname, count(category.cno) as c FROM parttimecat.job \r\n"
			+ "		join brand on brand.bno = job.bno join category on category.cno = brand.cno join apply on apply.jno = job.jno\r\n"
			+ "		where uno = ? group by category.cno order by c desc, category.cno limit 5;", sp.user.get(0));
	List<Row> all = Query.selectText("SELECT brand.bname, count(brand.bno) as c FROM parttimecat.apply \r\n"
			+ "		left join job on job.jno = apply.jno left join brand on job.bno = brand.bno \r\n"
			+ "		group by brand.bno order by c desc, job.bno");
	
	
	JComboBox<String> order = new JComboBox<String>("내가 좋아하는 알바, 인기 많은 브랜드".split(", ")) {{
		setPreferredSize(new Dimension(180, 30));
		setFont(sp.font(0, 15));
	}};
	List<Color> colors = new ArrayList<>() {{
		for(int i = 0; i < 5; i++) {
			int r = (int) (Math.random() * 256), g = (int) (Math.random() * 256), b = (int) (Math.random() * 256);
			add(new Color(r,g,b));
		}
	}};
	double start = 0, end = 0;
	JPanel mainPanel = new cp(null,null,null) {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			double total = 0.0;
			for(int i = 0; i < 5; i++) {
				if(order.getSelectedIndex() == 0)
					total = total + user.get(i).getInt(1);
				else
					total = total + all.get(i).getInt(1);
			}
			for(int i = 0; i < 5; i ++) {
				g.setColor(colors.get(i));
				start = start + end;
				end = (360.0 / total) * ((order.getSelectedIndex() == 0 ? user : all).get(i).getInt(1));
				System.out.println(end);
				Graphics2D g2 = (Graphics2D) g;
				g2.fill(new Arc2D.Double(142, 0, 300, 300, start, end, Arc2D.PIE));
			}
			start = 0; end = 0;
		}
	};
	JPanel southPanel = new cp(new GridLayout(0,5), sp.em(0, 30, 0, 30), null) {{
		for(int i = 0; i < 5; i++) {final int index = i;
			add(new cl("") {
				@Override
				public void paintComponent(Graphics g) {
					g.setColor(colors.get(index));
					g.fillRect(0, 0, 25,25);
					g.setFont(sp.font(1, 10));
					g.drawString((order.getSelectedIndex() == 0 ? user : all).get(index).getString(0), 33, 15);
				}
			});
		}
	}}.size(0, 80); 
	Chat(){
		setFrame("통계", 600, 500, ()->{});
	}
	@Override
	protected void desing() {
		for(Color c : colors) {
			System.out.println(c);
		}
		getContentPane().setBackground(Color.white);
		add(new cp(new FlowLayout(FlowLayout.LEFT), sp.em(10, 10, 10, 10), null) {{
			add(order);
		}}, sp.n);
		add(mainPanel);
		add(southPanel, sp.s);
	}

	@Override
	protected void action() {
		order.addItemListener(e->{
			rp();
		});
	}
	public static void main(String[] args) {
		new Chat();
	}
}
