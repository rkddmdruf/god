package main;

import java.awt.*;
import java.awt.geom.Arc2D;

import javax.swing.*;
import java.util.List;
import utils.*;
import utils.sp.cp;

public class Chat extends BaseFrame{
	JComboBox<String> cb = new JComboBox<String>("내가 좋아하는 알바, 인기 많은 브랜드".split(", "));
	JPanel borderPanel = new cp(new BorderLayout(), sp.em(15, 5, 15, 5), null);
	CardLayout card = new CardLayout();
	JPanel cps = new sp.cp(card, null, null);
	List<Row> iLike = Query.Chat_User.select(sp.user.get(0));
	List<Row> allLike = Query.Chat_All.select();
	double st = 0;
	Color[] color = new Color[5];
	Chat(){
		setFrame("통계", 600, 450, ()->{});
	}
	@Override
	protected void desing() {
		borderPanel.add(new cp(new FlowLayout(FlowLayout.LEFT), null, null) {{
			add(cb);
		}}, sp.n);
		cps.add(new cp(new BorderLayout(), null, null) {{
			setColor();
			add(new cp(new BorderLayout(), null, null) {{
				add(new chat(iLike));
			}});
			add(new cp(new GridLayout(1, 0), sp.em(0, 30, 0, 30), null) {{
				for(int i = 0; i < 5; i++)  add(new infor(iLike, i)); 
			}}.size(0, 50), sp.s);
		}}, "P0");
		
		cps.add(new cp(new BorderLayout(), null, null) {{
			setColor();
			add(new cp(new BorderLayout(), null, null) {{
				add(new chat(allLike));
			}});
			add(new cp(new GridLayout(1, 0), sp.em(0, 30, 0, 30), null) {{
				for(int i = 0; i < 5; i++)  add(new infor(allLike, i)); 
			}}.size(0, 50), sp.s);
		}}, "P1");
		card.show(cps, "P0");
		borderPanel.add(cps);
		add(borderPanel);
		repaints();
	}

	@Override
	protected void action() {
		cb.addItemListener(e->{ card.show(cps, "P" + cb.getSelectedIndex()); });
	}
	class infor extends JPanel{
		int index = 0;
		List<Row> list;
		infor(List<Row> list, int index){
			this.index = index; this.list = list;
			setBackground(Color.white);
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(color[index]);
			g.fillRect(0, 0, 20, 20);
			g.setFont(sp.font(0, 10));
			g.setColor(Color.black);
			g.drawString(list.get(index).getString(0), 22, 13);
		}
	}
	class chat extends JPanel{
		List<Row> list;
		chat(List<Row> list){
			setBackground(Color.white);
			this.list = list;
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			double one = 0;
			for(Row row : list) one = one + row.getInt(1);
			one = 360.0 / one;
			int i = 0;
			for(Row row : list) {
				g2.setColor(color[i++]);
				g2.fill(new Arc2D.Double((600 - 24) / 2 - 125, 25, 250, 250, st, (row.getInt(1) * one), Arc2D.PIE));
				st = st + (row.getInt(1) * one);
			}
			st = 0;
		}
	}
	private void setColor() {
		int i = 0;

		while(i++ < 5)  {
			int r = (int) ((Math.random() * 255) + 1),
					g2 = (int) ((Math.random() * 255) + 1),
					b = (int) ((Math.random() * 255) + 1);
			color[i-1] = new Color(r, g2, b); 
		}
	}
	public static void main(String[] args) {
		new Chat();
	}
}
