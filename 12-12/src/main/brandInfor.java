package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import utils.*;
import utils.sp.*;

public class brandInfor extends BaseFrame{
	
	JPanel borderPanel = new sp.cp(new BorderLayout(), sp.em(10, 10, 10, 5), null);
	JPanel advertisePanel = new cp(new BorderLayout(), sp.em(10, 10, 10, 10), Color.black);
	JPanel jobPanel = new cp(new BorderLayout(), null, null);
	Row list = null;
	List<Row> job = null;
	List<ca> taList = new ArrayList<>();
	int bno = 0, selectJob = -1, r = 0;
	Timer t = new Timer(2000, e->{});
	public brandInfor(int bno) {
		this.bno = bno;
		list = Query.selectText("select * from brand join category on category.cno = brand.cno where bno = ?", bno).get(0);
		job = Query.selectText("select * from job where bno = ?", bno);
		System.out.println(list);
		setFrame("브랜드 정보", 550, 600, ()->{t.stop();});
	}
	@Override
	protected void desing() {
		borderPanel.add(new cp(new BorderLayout(), null, null) {{
			add(new cl(sp.getImg("datafiles/brand/" + bno + ".png", 140, 140)).setBorders(sp.line(sp.color)), sp.w);
			add(new cp(new FlowLayout(FlowLayout.LEFT), null, null) {{
				add(new cl("[" + list.getString(6) + "]").font(sp.font(1, 16)));
			}});
			add(new cp(new BorderLayout(), sp.em(0, 0, 0, 130), null) {{
				add(new cl(list.getString(1)).font(sp.font(1, 30)), sp.w);
			}}, sp.e);
		}}, sp.n);
		
		borderPanel.add(new cp(new BorderLayout(), null, null) {{
			add(new cl("총 " + job.size() + "건의 알바가 있어요!").setHo(JLabel.RIGHT).font(sp.font(1, 18)).fontColor(sp.color), sp.n);
			JPanel p = new cp(new BorderLayout(), null, null);
			JLabel map = new cl(sp.getImg("datafiles/지도.png", 300,  275)) {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(Color.red);
					g.fillOval(list.getInt(3) / 2 - 5, list.getInt(4) / 2 - 5, 10, 10);
				}
			};
			map.setVerticalAlignment(JLabel.TOP);
			p.add(map);
			add(p, sp.w);
			JPanel gridPanel = new cp(new GridLayout(0,1), null, null) {{
				for(int i = 0; i < job.size(); i++) {
					taList.add(new ca(job.get(i).getString(1)) {{
						setPreferredSize(new Dimension(0, 60));
						setBorder(sp.line);
					}}.setting());
					add(taList.get(i));
				}
			}};
			jobPanel.add(new JScrollPane(gridPanel) {{
				setPreferredSize(new Dimension(200, 25));
				setColumnHeaderView(new cl(list.getString(1) + " 알바 목록") {{
					setOpaque(true);
					setBackground(sp.color);
				}}.font(sp.font(1, 16)).fontColor(Color.white).size(200, 25).setBorders(sp.line));
			}});
			add(jobPanel, sp.e);
		}});
		add(borderPanel);
		
		
		setAdverties();
		t.addActionListener(e->{
			setAdverties();
		});
		t.start();
		add(advertisePanel, sp.s);
	}

	private void setAdverties() {
		r = (int) (Math.random() * 200 + 1);
		advertisePanel.removeAll();
		advertisePanel.add(new cl(sp.getImg("datafiles/advertise/" + r + "-1.jpg", 100, 60)), sp.w);
		advertisePanel.add(new cl("  "+Query.selectText("select * from advertise where ano = ?", r).get(0).getString(1)).font(sp.font(Font.ITALIC, 15)).fontColor(Color.white));
		rp();
	}
	@Override
	protected void action() {
		advertisePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new advertise(r);
			}
		});
		for(ca t : taList) {
			t.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for(int i = 0; i < taList.size(); i++) {
						JTextArea s = taList.get(i);
						if(t == s) {
							s.setBackground(Color.yellow);
							if(e.getClickCount() == 2) {
								new JobInfor(job.get(i).getInt(0));
								dispose();
							}
						}
						else s.setBackground(Color.white);
					}
					rp();
				}
			});
		}
	}
	public static void main(String[] args) {
		new brandInfor(1);
	}
}
