package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.geom.Arc2D;
import java.util.List;

import static javax.swing.BorderFactory.*;
import javax.swing.*;

import utils.*;

public class Chart extends CFrame{
	Color[] colors = {Color.red, Color.orange, Color.pink, Color.yellow, getter.COLOR};
	JComboBox<Object> cb = new JComboBox<Object>("가장 많이 팔린 게임5,가장 많이 팔린 카테고리5".split(",")) {{
		setFont(new Font("맑은 고딕", 1, 11));
	}};
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	JPanel borderPanel = new JPanel(new BorderLayout(10, 10)) {{
		setBackground(Color.white);
		setBorder(createEmptyBorder(10,10,10,10));
	}};
	
	List<Data> list = Connections.select("SELECT purchasegame.*, count(purchasegame.g_no) as c, gameinformation.g_name FROM game_site.purchasegame\r\n"
			+ "join gameinformation on gameinformation.g_no = purchasegame.g_no\r\n"
			+ "group by purchasegame.g_no order by c desc, purchasegame.g_no limit 5;");
	int total;
	public Chart(){
		borderPanel.add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(cb);
		}}, BorderLayout.NORTH);
		for(Data data : list) {
			total += data.getInt(4);
		}
		borderPanel.add(new JLabel() {
			private double start = 90;
			private double end = 0;
			private double pursent = 100.0 / total;
			private double theta = 360.0 / total;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				for(int i = 0; i < list.size(); i++) {
					g2.setColor(colors[i]);
					start -= end;
					end = list.get(i).getInt(4) * theta;
					System.out.println(start + " + " + end);
					Arc2D arc = new Arc2D.Double(65, 50, 300, 300, start, -end, Arc2D.PIE);
					double d =  Math.toRadians(-(start + (-end / 2)));
					double x = 150 * Math.cos(d);
					double y = 150 * Math.sin(d);
					g2.fill(arc);
					g2.setColor(Color.black);
					String str = list.get(i).getInt(4) * pursent + "";
					g2.drawString(str.substring(0, 4) + "%", (int) x + 200, (int) y + 200);
				}
				
				g2.setColor(Color.white);
				g2.fillOval(65 + 50, 50 + 50, 200, 200);
				
			}
		});
		
		JPanel panel = new JPanel(new GridLayout(5, 1, 3, 3));
		panel.setBorder(createEmptyBorder(100,0,100,0));
		panel.setPreferredSize(new Dimension(200, 0));
		panel.setBackground(Color.white);
			
		for(int i = 0; i < 5; i++) {
			final int index = i;
			JPanel p = new JPanel(new BorderLayout(5, 5));
			p.setBackground(Color.white);
			p.add(new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(colors[index]);
					g.fillOval(0, 0, 30, 30);
					g.setColor(Color.black);
				}
			{setPreferredSize(new Dimension(30, 30));}}, BorderLayout.WEST);
			p.add(new JLabel(list.get(index).get(5).toString()) {{
				setBorder(createEmptyBorder(0,0,10,0));
			}});
			panel.add(p);
		}
		setting();
		//borderPanel.add(panel, BorderLayout.EAST);
		borderPanel.add(cardP);
		add(borderPanel);
		
		cb.addItemListener(e->{
			if(e.getStateChange() == ItemEvent.SELECTED) {
				if(cb.getSelectedIndex() == 0) {
					card.show(cardP, "P1");
				}else {
					card.show(cardP, "P2");
				}
				revalidate();
				repaint();
			}
		});
		
		setFrame("통계", 650, 450);
	}
	
	private void setting() {
		JPanel first = new JPanel(new BorderLayout());
		first.setBackground(Color.white);
		setCharts(first, list, total);
		
		JPanel two= new JPanel(new BorderLayout());
		two.setBackground(Color.white);
		List<Data> list = Connections.select("SELECT purchasegame.*, count(purchasegame.g_no) as c, gameinformation.g_name FROM game_site.purchasegame\r\n"
				+ "join gameinformation on gameinformation.g_no = purchasegame.g_no\r\n"
				+ "group by gameinformation.ca_no order by c desc, purchasegame.g_no limit 5;");
		int total = 0;
		for(int i = 0; i < list.size(); i++)
			total += list.get(i).getInt(4);
		setCharts(two, list, total);
		cardP.add(first, "P1");
		cardP.add(two, "P2");
	}
	
	private void setCharts(JPanel p, List<Data> list, int total) {
		p.add(new JLabel() {
			private double start = 90;
			private double end = 0;
			private double pursent = 100.0 / total;
			private double theta = 360.0 / total;
			@Override
			protected void paintComponent(Graphics g) {
				System.out.println(list);
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				for(int i = 0; i < list.size(); i++) {
					g2.setColor(colors[i]);
					start -= end;
					end = list.get(i).getInt(4) * theta;
					Arc2D arc = new Arc2D.Double(65, 50, 300, 300, start, -end, Arc2D.PIE);
					double d =  Math.toRadians(-(start + (-end / 2)));
					double x = 150 * Math.cos(d);
					double y = 150 * Math.sin(d);
					g2.fill(arc);
					g2.setColor(Color.black);
					String str = list.get(i).getInt(4) * pursent + "";
					g2.drawString(str.substring(0, 4) + "%", (int) x + 200, (int) y + 200);
				}
				
				g2.setColor(Color.white);
				g2.fillOval(65 + 50, 50 + 50, 200, 200);
				
			}
		});
	}

	public static void main(String[] args) {
		new Chart();
	}
}
