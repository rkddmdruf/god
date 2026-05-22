package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Connections;
import utils.Data;

public class AdminPanel extends JPanel{
	Color[] colors = {
		Color.red,
		Color.orange,
		Color.yellow,
		Color.green,
		Color.blue
	};
	LocalDate now = LocalDate.now();
	JComboBox<String> cb = new JComboBox<String>("월별 게임 총판매량,게임별 총 판매량 TOP5".split(","));
	JLabel chart = new JLabel("") {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	};
	List<Data> list = new ArrayList<>();
	public AdminPanel() {
		setLayout(new BorderLayout(30, 30));
		setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
		add(new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)) {{
			setBackground(Color.white);
			add(cb);
		}}, BorderLayout.NORTH);
		add(chart);
		setChart1();
		setting();
		setBackground(Color.white);
	}

	private void setChart1() {
		remove(chart);
		LocalDate moment4Date = now.minusMonths(4).minusDays(now.getDayOfMonth() - 1);
		List<Integer> size_s = new ArrayList<>();
		List<Integer> moments = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			size_s.add(Connections.select("SELECT * FROM game.buygame where date >= ? and date <= ?;"
					, moment4Date.plusMonths(i)
					, moment4Date.plusMonths(i + 1).minusDays(1)
					).size());
			
			if(size_s.get(i) == 0)
				size_s.set(i, 1);
			moments.add(moment4Date.plusMonths(i).getMonthValue());
		}
		
		int maxTotal = 400;
		for(int i = 0; i < 4; i++)
			if(size_s.get(i) < size_s.get(i + 1))
				maxTotal = size_s.get(i + 1);
		
		final int max = maxTotal;
		System.out.println(size_s);
		chart = new JLabel() {
			private int a = 400 / max;
			private Integer sx = null, sy = null;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				int w = getWidth() / 5;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				for(int i = 0; i < 5; i++) {
					String s = moments.get(i) + "월";
					Font font = new Font("맑은 고딕", 1, 13);
					g2.setFont(font);
					int sw = getFontMetrics(font).stringWidth(s) / 2;
					int sh = getFontMetrics(font).getHeight();
					int ws = w * i;
					g2.setColor(colors[i]);
					g2.fillRect(ws, 0, ws + w, sh * 2);
					g2.setColor(Color.black);
					g2.drawString(s, ws + (w / 2) - sw, sh);
					
					g2.setColor(Color.PINK);
					int ww = ws + (w / 2) - 3;
					int hh = 500 - (((a * size_s.get(i)) + (sh * 2)) - 3);
					g2.fillOval(ww - 3, hh - 3, 6, 6);
					
					if(sx != null && sy != null)
						if(sx <= ww)
							g2.drawLine(sx, sy, ww, hh);
					sx = ww;
					sy = hh;
				}
				
			}
		};
		
		add(chart);
		revalidate();
		repaint();
	}
	private void setChart2() {
		remove(chart);
		
		List<Data> list = Connections.select("SELECT game.*, count(game.gno) as c FROM game.buygame \r\n"
				+ "join game on game.gno = buygame.gno\r\n"
				+ "group by game.gno order by c desc, game.gno limit 5");
		chart = new JLabel() {
			int a = 0;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				a = 400 / list.get(0).getInt(list.get(0).size() - 1);
				Graphics2D g2 = (Graphics2D) g;
				int w = getWidth() / 5;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				for(int i = 0; i < list.size(); i++) {
					g2.setColor(colors[i]);
					int x = (w * i) + (w / 2), y = 500 - (a * list.get(i).getInt(list.get(i).size() - 1));
					g2.fillRect(x - 25, y, 50, getHeight() - y);
					Font font = new Font("맑은 고딕", 1, 14);
					g2.setColor(Color.black);
					g2.drawString(list.get(i).getString(1), x - (getFontMetrics(font).stringWidth(list.get(i).getString(1)) / 2) + 5, y - getFontMetrics(font).getHeight());
					g2.drawImage(new ImageIcon("datafiles/games/" + list.get(i).getInt(0) + ".jpg").getImage()
							, x - (getFontMetrics(font).stringWidth(list.get(i).getString(1)) / 2) + 5
							, y - getFontMetrics(font).stringWidth(list.get(i).getString(1)) / 2 - (getFontMetrics(font).getHeight() * 2)
							, getFontMetrics(font).stringWidth(list.get(i).getString(1))
							, getFontMetrics(font).stringWidth(list.get(i).getString(1)) / 2
							, null);
				}
			}
		};
		
		add(chart);
		revalidate();
		repaint();
	}

	private void setting() {
		cb.setFont(new Font("맑은 고딕", 1, 13));
		cb.addActionListener(e -> {
			if(cb.getSelectedIndex() == 0)
				setChart1();
			else 
				setChart2();
		});
	}
}
